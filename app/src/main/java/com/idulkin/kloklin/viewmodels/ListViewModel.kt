package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.KloklinApplication
import com.idulkin.kloklin.data.DBProgram
import com.idulkin.kloklin.defaults
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program

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
            var programsFromDB = dao.allPrograms()

            when (params[0]) {
                "delete" -> dao.deleteProgram(DBProgram(programs.value!![params[1]!!.toInt()]))
            }

            //Empty database, fill it with defaults
            if (programsFromDB.isEmpty()) {
                for (program in defaults) {
                    dao.addProgram(program)
                }
                programsFromDB = dao.allPrograms()
            }

            programsFromDB.mapTo(out) { it.getProgram() }

            return out
        }

        override fun onPostExecute(result: ArrayList<Program>?) {
            programs.value = result
        }
    }

    fun init() {
        //Get the program list from the database
        DBManager().execute("init")
    }

    /**
     * Delete a program from the list
     */
    fun deleteProgram(program: Program) {
        DBManager().execute("delete", programs.value!!.indexOf(program).toString())
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