package com.idulkin.kloklin.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

/**
 * Created by igor on 2/20/18.
 */

@Database(entities = [(DBProgram::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun programDao(): ProgramDao
}