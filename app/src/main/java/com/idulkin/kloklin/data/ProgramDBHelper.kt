package com.idulkin.kloklin.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.JsonReader
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.Program
import java.io.InputStreamReader

/**
 * Created by igor.dulkin on 8/3/17.
 */
class ProgramDBHelper(val context: Context) : SQLiteOpenHelper(context, "programs.db", null, 1) {

    //DB creation sql statement
    val DB_CREATE = "create table ${DBContract.TABLE_PROGRAMS} " +
            "( ${DBContract.COLUMN_ID} integer primary key autoincrement, " +
            "${DBContract.COLUMN_NAME}text not null, " +
            " ${DBContract.COLUMN_INTERVAL} integer not null, " +
            " ${DBContract.COLUMN_ACTION} text not null)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DB_CREATE)
        programsFromResources(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + DBContract.TABLE_PROGRAMS)
        onCreate(db)
    }

    /**
     * Populate a fresh database from a JSON object
     */
    fun programsFromResources(db: SQLiteDatabase) {
        val jsonReader = JsonReader(InputStreamReader(context.resources.openRawResource(R.raw.programs)))
        jsonReader.use {
            jsonReader.beginObject()
            val jsonName = jsonReader.nextName()
            if (jsonName == "programs") {
                jsonReader.beginArray()
                while (jsonReader.hasNext()) {
                    programFromJson(jsonReader, db)
                }
            }
        }
    }

    /**
     * Read a program from JSON and insert it into the database
     */
    fun programFromJson(jsonReader: JsonReader, db: SQLiteDatabase) {
        val values = ContentValues()
        var name = "None"

        jsonReader.beginObject()
        while (jsonReader.hasNext()) {
            val jsonName = jsonReader.nextName()
            if (jsonName == "name") {
                name = jsonReader.nextString()
            }

            values.put(DBContract.COLUMN_NAME, name)
            values.put(DBContract.COLUMN_INTERVAL, jsonReader.nextInt())
            values.put(DBContract.COLUMN_ACTION, jsonReader.nextString())

            db.insert(DBContract.TABLE_PROGRAMS, null, values)
        }
        jsonReader.endObject()
    }

    /**
     * Insert a new program into the database
     */
    fun writeProgram(program: Program, db: SQLiteDatabase) {
        val values = ContentValues()
        val name = program.name

        for ((seconds, action) in program.intervals) {
            values.put(DBContract.COLUMN_NAME, name)
            values.put(DBContract.COLUMN_INTERVAL, seconds)
            values.put(DBContract.COLUMN_ACTION, action)

            db.insert(DBContract.TABLE_PROGRAMS, null, values)
        }
    }

    /**
     * Remove a program from the database
     */
    fun deleteProgram(name: String, db: SQLiteDatabase) {

    }
}
