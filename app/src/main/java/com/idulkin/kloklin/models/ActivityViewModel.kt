package com.idulkin.kloklin.models

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.view.MenuItem
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.fragments.ClockFragment
import com.idulkin.kloklin.fragments.EditFragment
import com.idulkin.kloklin.fragments.ListFragment
import com.idulkin.kloklin.fragments.SettingsFragment
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


    val clockFragment = ClockFragment()
    val listFragment = ListFragment()
    val settingsFragment = SettingsFragment()
    val editFragment = EditFragment()

    val backStack = Stack<Int>() //Tracks previous fragments for the back button

    fun onMenuItemClicked(item: MenuItem, currentPage: Int): Int {
        backStack.push(currentPage)
        return when (item.itemId) {
            R.id.action_list -> PAGE.LIST.position
            R.id.action_clock -> PAGE.CLOCK.position
            R.id.action_settings -> PAGE.SETTINGS.position
            else -> PAGE.CLOCK.position
        }
    }

    fun previousPage(): Int {
        return if (backStack.empty()) -1
        else backStack.pop()
    }

    fun startNewProgram(program: Program, currentPage: Int): Int {
        clockFragment.model?.newProgram(program)
        backStack.push(currentPage)
        return PAGE.CLOCK.position
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