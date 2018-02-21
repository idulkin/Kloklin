package com.idulkin.kloklin.objects

import android.arch.persistence.room.*

/**
 * Created by igor on 2/20/18.
 *
 * Room Entity and DAO for a table where every row is a program
 */
@Entity class DBProgram (
        @PrimaryKey val uid: Int,
        @ColumnInfo val name: String,
        @ColumnInfo val desc: String,
        @Embedded val intervals: ArrayList<Interval>
    )

@Dao interface ProgramDao {
    @Query("SELECT * FROM programs")
    fun allPrograms(): ArrayList<DBProgram>

    @Query("SELECT * FROM programs WHERE name LIKE :name")
    fun programByName(name: String): DBProgram

    @Insert
    fun addProgram(program: DBProgram)

    @Update
    fun updateProgram(program: DBProgram)

    @Delete
    fun deleteProgram(program: DBProgram)
}

@Database(entities = [(DBProgram::class)], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun programDao(): ProgramDao
}
