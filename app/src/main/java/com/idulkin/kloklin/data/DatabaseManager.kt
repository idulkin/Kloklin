package com.idulkin.kloklin.data

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program

/**
 * Created by igor.dulkin on 8/5/17.
 */
class DatabaseManager(context: Context) : AsyncTask<String, Int, Cursor>() {

    val programDBHelper = ProgramDBHelper(context)
    val allColumns = arrayOf(
            DBContract.COLUMN_ID,
            DBContract.COLUMN_NAME,
            DBContract.COLUMN_INTERVAL,
            DBContract.COLUMN_ACTION
    )

    override fun doInBackground(vararg args: String?): Cursor {
        val db = programDBHelper.readableDatabase
        val selectionArgs = if (args[2] != null) {
            arrayOf(args[2])
        } else {
            null
        }

        return db.query(
                args[0],        //Table to query
                allColumns,     //Columns to return
                args[1],        //Columns for WHERE clause
                selectionArgs,  //From args[2]
                null,
                null,
                args[3]         //Sort order
        )
    }

    /**
     * Moves through
     *
     * Returns the whole table as a list of programs
     */
    fun queryAllPrograms(): Cursor {
        return doInBackground(
                DBContract.TABLE_PROGRAMS
        )
    }

    fun queryPrograms(): ArrayList<Program> {
        val list = ArrayList<Program>()
        val programs = ArrayList<String>()
        var programName = ""

        val selection = DBContract.COLUMN_NAME + " = ?"
        val cursor = doInBackground(
                DBContract.TABLE_PROGRAMS,
                null,
                null,
                null
        )

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.COLUMN_NAME))
            if (name != programName) {
                programName = name
                programs.add(programName)
            }
        }
        cursor.close()

        programs.mapTo(list) { programByName(it) }
        return list
    }

    /**
     * Query the DB for a program by name
     */
    fun programByName(name: String): Program {
        val selection = DBContract.COLUMN_NAME + " = ?"
        val selectionArgs = name
        val sortOrder = DBContract.COLUMN_ID + " DESC"

        val cursor = doInBackground(
                DBContract.TABLE_PROGRAMS,
                selection,
                selectionArgs,
                sortOrder
        )

        val intervals = ArrayList<Interval>()
        while (cursor.moveToNext()) {
            intervals.add(Interval(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.COLUMN_INTERVAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBContract.COLUMN_ACTION))
            ))
        }
        cursor.close()

        return Program(name, "", intervals)
    }


}