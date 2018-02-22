package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.view.MenuItem
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.data.AppDatabase
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program
import java.util.*

/**
 * Created by igor.dulkin on 9/18/17.
 */
class ActivityViewModel : ViewModel() {

     // Enum to track page numbers for the ViewPager
    enum class PAGE(val position: Int) {
        EDIT(0),
        LIST(1),
        CLOCK(2),
        SETTINGS(3)
    }

    var page: MutableLiveData<Int> = MutableLiveData()
    var playingProgram = Program("Placeholder", "", arrayListOf(Interval(60, "")))
    var editedProgram = playingProgram

//    var database: AppDatabase? = null
//    val clockFragment = ClockFragment()
//    val editFragment = EditFragment()

    val backStack = Stack<Int>() //Tracks previous fragments for the back button

    fun onMenuItemClicked(item: MenuItem) {
        backStack.push(page.value)
        page.value = when (item.itemId) {
            R.id.action_list -> PAGE.LIST.position
            R.id.action_clock -> PAGE.CLOCK.position
            R.id.action_settings -> PAGE.SETTINGS.position
            else -> PAGE.CLOCK.position
        }
    }

    //Opens the previous page, or returns false if there isn't one
    fun previousPage(): Boolean {
        return if (backStack.empty()) {
            false
        } else {
            val prev = backStack.pop()
            if (prev != 0) { //Previous is Edit fragment. Skip to the next value
                page.value = prev
                true
            } else {
                previousPage()
            }
        }
    }

    fun startNewProgram(program: Program) {
        playingProgram = program
//        clockFragment.model.newProgram(program)
        backStack.push(page.value)
        page.value = PAGE.CLOCK.position
    }

    fun editProgram(program: Program) {
        editedProgram = program
        backStack.push(page.value)
        page.value = PAGE.EDIT.position
    }

   /**
     * Companion object to instantiate a persistent model
     */
    companion object {
        fun create(activity: KloklinActivity): ActivityViewModel {
            return ViewModelProviders.of(activity).get(ActivityViewModel::class.java)
        }
    }
}