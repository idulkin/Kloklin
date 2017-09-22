package com.idulkin.kloklin.models

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import com.idulkin.kloklin.fragments.ClockFragment
import com.idulkin.kloklin.fragments.ListFragment
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program

/**
 * Created by igor.dulkin on 9/18/17.
 */
class ListViewModel : ViewModel() {

    val programs = ArrayList<Program>()

    fun init() {
        //Test values
        programs.add(Program("One Minute", "Placeholder Minute Timer", arrayListOf(Interval(60, ""))))
        val timer = Interval(5, "Test")
        var i = 0
        while (i < 5) {
            programs.add(Program("Test", "Test Description", arrayListOf(timer, timer, timer, timer, timer)))
            i++
        }
        //TODO:Replace previous with real list
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