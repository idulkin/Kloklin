package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.KloklinApplication
import com.idulkin.kloklin.data.DBManager
import com.idulkin.kloklin.data.DBProgram
import com.idulkin.kloklin.defaults
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program
import kotlinx.coroutines.experimental.async

/**
 * Created by igor.dulkin on 9/18/17.
 */
class ListViewModel : ViewModel() {

    var programs: MutableLiveData<ArrayList<Program>> = MutableLiveData()

    /**
     * Inner class to manage async calls to the database
     */
    inner class DBManager : AsyncTask<String, Void, ArrayList<Program>>() {

        override fun doInBackground(vararg params: String?): ArrayList<Program> {
            val out = ArrayList<Program>()
            val dao = KloklinApplication.db.programDao()
            val programsFromDB = dao.allPrograms()

            //Empty database, fill it with defaults
            if (programsFromDB.isEmpty()) {
                for (program in defaults) {
                    dao.addProgram(program)
                }
            }

            programsFromDB.mapTo(out) { it.getProgram() }

            return out
        }

        override fun onPostExecute(result: ArrayList<Program>?) {
            super.onPostExecute(result)

            programs.value = result
        }
    }

    fun init() {
        //Get the program list from the database
        DBManager().execute()
    }

    fun populateDB() {
        val dao = KloklinApplication.db.programDao()
        val defaults: List<DBProgram> = listOf(
                DBProgram("Test", "Test Description", arrayListOf(Interval(5, "First"), Interval(5, "Second"), Interval(5, "Third"), Interval(5, "Fourth"), Interval(5, "Fifth")))
        )

        for (program in defaults) {
            dao.addProgram(program)
        }
    }

    /**
     * Delete a program from the list
     */
    fun deleteProgram(program: Program) {
        programs.value?.remove(program)
//        dbManager?.deleteProgram(program.name)
    }

    /**
     * Companion object returns the view model from ViewModelProviders,
     * making this a singleton
     */
    companion object {
        fun create(activity: KloklinActivity): ListViewModel {
            return ViewModelProviders.of(activity).get(ListViewModel::class.java)
        }
    }
}