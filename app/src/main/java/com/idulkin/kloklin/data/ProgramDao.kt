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

    @Query("SELECT * FROM programs ORDER BY pos")
    fun allPrograms(): List<Program>

    @Query("SELECT * FROM programs WHERE name LIKE :name")
    fun programByName(name: String): Program

    @Insert
    fun addProgram(program: Program)

    @Update
    fun updateProgram(program: Program)

    @Delete
    fun deleteProgram(program: Program)
}
