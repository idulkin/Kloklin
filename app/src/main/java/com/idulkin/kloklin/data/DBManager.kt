package com.idulkin.kloklin.data

import android.arch.persistence.room.Room
import android.os.AsyncTask
import android.content.Context
import com.idulkin.kloklin.objects.Program

/**
 * Created by igor on 2/21/18.
 */
class DBManager(context: Context) : AsyncTask<String, Int, ArrayList<Program>>() {

    val db = Room
            .databaseBuilder(context, AppDatabase::class.java, "programs")
            .build()

    override fun doInBackground(vararg params: String?): ArrayList<Program> {
        val programsFromDB = db.programDao().allPrograms()
        val out = ArrayList<Program>()

        programsFromDB.mapTo(out) { it.getProgram() }

        return out
    }

    fun queryPrograms(): ArrayList<Program> {
        return doInBackground()
    }
}