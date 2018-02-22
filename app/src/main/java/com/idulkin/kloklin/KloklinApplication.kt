package com.idulkin.kloklin

import android.app.Application
import android.arch.persistence.room.Room
import com.idulkin.kloklin.data.AppDatabase

/**
 * Created by igor on 2/21/18.
 */
class KloklinApplication: Application() {
    companion object Database {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        KloklinApplication.db = Room.databaseBuilder(this, AppDatabase::class.java, "programs").build()
    }
}