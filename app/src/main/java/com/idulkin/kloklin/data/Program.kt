package com.idulkin.kloklin.data

import android.arch.persistence.room.*
import com.idulkin.kloklin.Interval

/**
 * Created by igor on 2/20/18.
 *
 * Room Entity for a table where every row is a program
 */
@Entity(tableName = "programs") data class Program (
        val name: String,
        val desc: String,
        val intervals: ArrayList<Interval>
    ) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}

