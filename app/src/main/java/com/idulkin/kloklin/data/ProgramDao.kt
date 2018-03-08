package com.idulkin.kloklin.data

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*

/**
 * Created by igor on 2/20/18.
 *
 * Data Access Object for all database interactions
 */

@Dao
interface ProgramDao {

    @Query("SELECT * FROM programs")
    fun allPrograms(): List<DBProgram>

    @Query("SELECT * FROM programs WHERE name LIKE :name")
    fun programByName(name: String): DBProgram

    @Insert
    fun addProgram(program: DBProgram)

    @Update
    fun updateProgram(program: DBProgram)

    @Delete
    fun deleteProgram(program: DBProgram)
}
