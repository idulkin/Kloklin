package com.idulkin.kloklin.data

import android.database.Cursor
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program

/**
 * Created by igor.dulkin on 9/24/17.
 */
class DBUtils {

    /**
     * Read the DB and return a list of programs
     */
    fun programsFromCursor(cursor: Cursor): List<Program> {

        var name = ""
        val desc = ""
        var intervals = ArrayList<Interval>()

        while (cursor.moveToNext()) {
            val columnName = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.COLUMN_NAME))
            if (columnName != name) { //New program
                intervals.removeAll(intervals)
            }
            val intervalTime = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.COLUMN_INTERVAL))
            val intervalAction = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.COLUMN_ACTION))
            intervals.add(Interval(intervalTime, intervalAction))
        }

    }
}