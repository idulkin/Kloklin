package com.idulkin.kloklin.models

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.idulkin.kloklin.data.DatabaseManager
import com.idulkin.kloklin.fragments.ClockFragment
import com.idulkin.kloklin.fragments.ListFragment
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program

/**
 * Created by igor.dulkin on 9/18/17.
 */
class ListViewModel : ViewModel() {

    var programs = ArrayList<Program>()
    var dbManager: DatabaseManager? = null

    /**
     * Initialize a list for the recycler adapter.
     */
    fun init(context: Context) {
        //Default list values if the DB is empty
        programs.add(Program("One Minute", "Placeholder Minute Timer", arrayListOf(Interval(60, ""))))
        val timer = Interval(5, "Test")
        var i = 0
        while (i < 5) {
            programs.add(Program("Test", "Test Description", arrayListOf(timer, timer, timer, timer, timer)))
            i++
        }

        //Get the full list from the database
        dbManager = DatabaseManager(context)
        programs = dbManager?.queryPrograms() ?: programs
    }

    /**
     * Companion object to instantiate a persistent model
     */
    companion object {
        fun create(fragment: ListFragment): ListViewModel{
            return ViewModelProviders.of(fragment).get(ListViewModel::class.java)
        }
    }
}