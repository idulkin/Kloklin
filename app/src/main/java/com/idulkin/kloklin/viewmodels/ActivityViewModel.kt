package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.view.MenuItem
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.PAGE
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program
import java.util.*

/**
 * Created by igor.dulkin on 9/18/17.
 *
 * ViewModel for the main activity. Tracks the position of the ViewPager and
 * interacts with the database to maintain the list of all programs
 */
class ActivityViewModel : ViewModel() {

    var page: MutableLiveData<Int> = MutableLiveData()
    private val backStack = Stack<Int>() //Tracks previous fragments for the back button

    //Action bar icon navigation by ViewPager
    fun onMenuItemClicked(item: MenuItem) {
        backStack.push(page.value)
        page.value = when (item.itemId) {
            R.id.action_list -> PAGE.LIST.pos
            R.id.action_clock -> PAGE.CLOCK.pos
            R.id.action_settings -> PAGE.SETTINGS.pos
            else -> PAGE.CLOCK.pos
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

    var playingProgram = Program("Placeholder", "", arrayListOf(Interval(60, "")))
    var editedProgram = playingProgram

    fun startNewProgram(program: Program) {
        playingProgram = program
        backStack.push(page.value)
        page.value = PAGE.CLOCK.pos
    }

    fun editProgram(program: Program) {
        editedProgram = program
        backStack.push(page.value)
        page.value = PAGE.EDIT.pos
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