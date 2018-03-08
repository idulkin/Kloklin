package com.idulkin.kloklin.data

import android.arch.persistence.room.*
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program

/**
 * Created by igor on 2/20/18.
 *
 * Room Entity and DAO for a table where every row is a program
 */
@Entity(tableName = "programs") class DBProgram (
//        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "desc") val desc: String,
        @Embedded val intervals: ArrayList<Interval>?
    ) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    fun getProgram(): Program{
        return Program(name, desc, intervals ?: arrayListOf())
    }
}

