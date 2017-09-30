package com.idulkin.kloklin.data

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask

/**
 * Created by igor.dulkin on 8/5/17.
 */
class DatabaseManager(context: Context): AsyncTask<String, Int, Cursor>() {

    val programDBHelper = ProgramDBHelper(context)
    val allColumns = arrayOf(
        programDBHelper.COLUMN_ID,
        programDBHelper.COLUMN_NAME,
        programDBHelper.COLUMN_INTERVAL,
        programDBHelper.COLUMN_ACTION
    )

    override fun doInBackground(vararg args: String?): Cursor {
        val db = programDBHelper.readableDatabase
        val selectionArgs = arrayOf("")

        return db.query(args[0], allColumns, args[1], selectionArgs, null, null, args[3])
    }

    /**
     * Returns the whole table as a cursor, to populate a list on activity create
     */
    fun queryAllPrograms() : Cursor {
        return doInBackground(
            programDBHelper.TABLE_PROGRAMS
        )
    }


}