package com.idulkin.kloklin.data

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*

/**
 * Created by igor on 2/20/18.
 */

@Dao
interface ProgramDao {
    //Getter function because suspend keyword doesn't work well with Room queries
//    suspend fun getPrograms(): List<DBProgram> = allPrograms()

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
