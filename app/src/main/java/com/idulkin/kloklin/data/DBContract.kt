package com.idulkin.kloklin.data

import android.provider.BaseColumns

/**
 * Created by igor.dulkin on 9/30/17.
 *
 * Defines the database schema
 */
class DBContract {
    companion object : BaseColumns {
        //Database columns
        val TABLE_PROGRAMS = "programs"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_INTERVAL = "interval"
        val COLUMN_ACTION = "action"
    }
}