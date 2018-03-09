package com.idulkin.kloklin

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.idulkin.kloklin.data.Program
import com.idulkin.kloklin.fragments.ClockFragment
import com.idulkin.kloklin.fragments.EditFragment
import com.idulkin.kloklin.fragments.ListFragment
import com.idulkin.kloklin.fragments.SettingsFragment
import com.idulkin.kloklin.viewmodels.ActivityViewModel
import kotlinx.android.synthetic.main.activity_kloklin.*

/**
 * UI for the main activity. Shows a ViewPager of fragments
 * and interacts with the ActivityViewModel
 */
class KloklinActivity : FragmentActivity() {

    val model: ActivityViewModel by lazy {
        ActivityViewModel.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kloklin)

        setActionBar(kloklin_toolbar)
        action_menu.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        //Initialize shared preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        //ViewPager contains all fragments
        pager.adapter = FragmentAdapter(supportFragmentManager)
        if (savedInstanceState == null) {
            pager.currentItem = PAGE.LIST.pos
        }

        model.page.observe(this, Observer<Int> { page ->
            if (page != null) {
                pager.currentItem = page
            }
        })

        //Load program list from database
        model.initDB()

        //Restore last program from shared prefs, or use a default placeholder
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val json = sharedPrefs.getString("CurrentProgram", "")
        model.playingProgram = Gson().fromJson(json, Program::class.java)
                ?: Program(0, "One Minute", "Placeholder Minute Timer", arrayListOf(Interval(60, "")))
    }

    override fun onStop() {
        super.onStop()

        model.updateProgram()
    }

    /**
     *Inflate the menu; this adds items to the action bar if it is present.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_kloklin, action_menu.menu)
        return true
    }

    /**
     * Action bar icons
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        model.onMenuItemClicked(item)
        return true
    }

    /**
     * Back button opens previous fragment, or closes the app if there isn't one
     */
    override fun onBackPressed() {
        if (!model.previousPage()) {
            super.onBackPressed()
        }
    }

    /**
     * Open the clock fragment with the provided program
     */
    fun openClock(program: Program) {
        model.startNewProgram(program)
    }

    /**
     * Adapter class for the view pager
     */
    inner class FragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount() = 4 //There are four lights

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> EditFragment()
                1 -> ListFragment()
                2 -> ClockFragment()
                3 -> SettingsFragment()
                else -> ClockFragment()
            }
        }
    }
}


