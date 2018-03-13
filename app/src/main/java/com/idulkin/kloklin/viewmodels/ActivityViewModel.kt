package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.view.MenuItem
import com.idulkin.kloklin.*
import com.idulkin.kloklin.data.Program
import java.util.*

/**
 * Created by igor.dulkin on 9/18/17.
 *
 * ViewModel for the main activity. Tracks the position of the ViewPager and
 * interacts with the database to maintain the list of all programs
 */
class ActivityViewModel : ViewModel() {

    // Action bar icon navigation by ViewPager
    var page: MutableLiveData<Int> = MutableLiveData()
    private val backStack = Stack<Int>() //Tracks previous fragments for the back button

    fun onMenuItemClicked(item: MenuItem) {
        if (page.value == PAGE.EDIT.pos) {
            updateProgram()
            editedProgram = 0
        }

        backStack.push(page.value)

        page.value = when (item.itemId) {
            R.id.action_list -> PAGE.LIST.pos
            R.id.action_clock -> PAGE.CLOCK.pos
            R.id.action_settings -> PAGE.SETTINGS.pos
            else -> PAGE.CLOCK.pos
        }
    }

    //Opens the previous page, or returns false if there isn't one
    fun previousPage(): Boolean {
        return if (backStack.empty()) {
            false
        } else {
            val prev = backStack.pop()
            if (prev != 0) { //Previous is Edit fragment. Skip to the next value
                page.value = prev
                true
            } else {
                previousPage()
            }
        }
    }

    // Observable list of programs
    var programs: MutableLiveData<ArrayList<Program>> = MutableLiveData()
    var playingProgram = placeholder
    var editedProgram = playingProgram.pos

    fun startNewProgram(program: Program) {
        playingProgram = program
        backStack.push(page.value)
        page.value = PAGE.CLOCK.pos
    }

    fun editProgram(program: Program) {
        editedProgram = program.pos
        backStack.push(page.value)
        page.value = PAGE.EDIT.pos
    }


    /**
     * Inner class to manage async calls to the database
     */
    inner class DBManager : AsyncTask<String, Void, ArrayList<Program>>() {

        override fun doInBackground(vararg params: String?): ArrayList<Program> {
            val out = ArrayList<Program>()
            val dao = KloklinApplication.db.programDao()

            when (params[0]) {
                "delete" -> dao.deleteByPos(params[1]!!.toInt())
                "update" -> dao.updateProgram(programs.value!![editedProgram])
                "add" -> dao.addProgram(programs.value!!.last())
            }

            var programsFromDB = dao.allPrograms()

            //Empty database, fill it with defaults
            if (programsFromDB.isEmpty()) {
                for (program in defaults) {
                    dao.addProgram(program)
                }
                programsFromDB = dao.allPrograms()
            }

            programsFromDB.mapTo(out) { it }
            return out
        }

        override fun onPostExecute(result: ArrayList<Program>?) {
            programs.value = result
        }
    }

    fun initDB() {
        //Get the program list from the database
        DBManager().execute("init")
    }

    fun deleteProgram(program: Program) {
        DBManager().execute("delete", program.pos.toString())
    }

    fun updateProgram() {
        DBManager().execute("update")
    }

    fun addProgram() {
        programs.value?.add(Program(programs.value!!.size, "New Program","", arrayListOf()))
        DBManager().execute("add")
    }

    /**
     * Companion object to instantiate a persistent model
     */
    companion object {
        fun create(activity: KloklinActivity): ActivityViewModel {
            return ViewModelProviders.of(activity).get(ActivityViewModel::class.java)
        }
    }
}