package com.idulkin.kloklin.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.JsonReader
import android.util.Log
import com.idulkin.kloklin.R
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by igor.dulkin on 8/3/17.
 */
class TimerDBHelper(val context: Context) : SQLiteOpenHelper(context, "intervals.db", null, 1) {

    //Database columns
    val TABLE_TIMERS = "intervals"
    val COLUMN_ID = "_id"
    val COLUMN_NAME = "name"
    val COLUMN_INTERVAL = "interval"
    val COLUMN_ACTION = "action"

    //DB creation sql statement
    val DB_CREATE = "create table $TABLE_TIMERS " +
            "( $COLUMN_ID integer primary key autoincrement, " +
            "$COLUMN_NAME text not null, " +
            " $COLUMN_INTERVAL integer not null, " +
            " $COLUMN_ACTION text not null)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DB_CREATE)
        timersFromResources(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMERS)
        onCreate(db)
    }

    /**
     * Populate a fresh database from a JSON object
     */
    fun timersFromResources(db: SQLiteDatabase) {
        val jsonReader = JsonReader(InputStreamReader(context.resources.openRawResource(R.raw.timers)))
        jsonReader.use {
            jsonReader.beginObject()
            val jsonName = jsonReader.nextName()
            if (jsonName == "intervals") {
                jsonReader.beginArray()
                while (jsonReader.hasNext()) {
                    readTimer(jsonReader, db)
                }
            }
        }
    }

    /**
     * Read a Interval and insert it into the database
     */
    fun readTimer(jsonReader: JsonReader, db: SQLiteDatabase) {
        val values = ContentValues()
        var name = "None"

        jsonReader.beginObject()
        while (jsonReader.hasNext()) {
            val jsonName = jsonReader.nextName()
            if (jsonName == "name") {
                name = jsonReader.nextString()
            }

            values.put(COLUMN_NAME, name)
            values.put(COLUMN_INTERVAL, jsonReader.nextInt())
            values.put(COLUMN_ACTION, jsonReader.nextString())

            db.insert(TABLE_TIMERS, null, values)
        }
        jsonReader.endObject()
    }
}
