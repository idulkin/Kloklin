package com.idulkin.kloklin.data

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask

/**
 * Created by igor.dulkin on 8/5/17.
 */
class DatabaseManager(context: Context): AsyncTask<String, Int, Cursor>() {

    val timerDBHelper = TimerDBHelper(context)
    val allColumns = arrayOf(
        timerDBHelper.COLUMN_ID,
        timerDBHelper.COLUMN_NAME,
        timerDBHelper.COLUMN_INTERVAL,
        timerDBHelper.COLUMN_ACTION
    )

    override fun doInBackground(vararg args: String?): Cursor {
        val db = timerDBHelper.readableDatabase
        var selectionArgs = arrayOf("")

        return db.query(args[0], allColumns, args[1], selectionArgs, null, null, args[3])

    }

    fun queryAllTimers () : Cursor {

        return doInBackground(
            timerDBHelper.TABLE_TIMERS
        )
    }
}