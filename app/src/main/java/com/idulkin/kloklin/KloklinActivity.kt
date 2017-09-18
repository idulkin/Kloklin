package com.idulkin.kloklin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.Menu
import android.view.MenuItem
import com.idulkin.kloklin.models.ClockViewModel
import com.idulkin.kloklin.fragments.*
import com.idulkin.kloklin.models.ActivityViewModel
import com.idulkin.kloklin.objects.Program
import kotlinx.android.synthetic.main.activity_kloklin.*
import java.util.*

class KloklinActivity : FragmentActivity() {

    var model: ActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kloklin)
        setActionBar(kloklin_toolbar)
        action_menu.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        model = ActivityViewModel.create(this)
        pager.adapter = FragmentAdapter(supportFragmentManager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_kloklin, action_menu.menu)
        return true
    }

    /**
     * Action bar icons
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        pager.currentItem = model?.onMenuItemClicked(item, pager.currentItem) ?: pager.currentItem
        return true
    }

    /**
     * Back button opens previous fragment
     */
    override fun onBackPressed() {
        val prev = model?.previousPage()
        if (prev!! < 0) {
            super.onBackPressed()
        } else {
            pager.currentItem = prev
        }
    }

    /**
     * Open the clock fragment with the provided program
     */
    fun openClock(program: Program) {
        //Replace the current clock fragment
        pager.currentItem = model?.startNewProgram(program, pager.currentItem) ?: pager.currentItem
    }

    /**
     * Adapter class for the view pager
     */
    inner class FragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount() = 4

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> model?.editFragment!!
                1 -> model?.listFragment!!
                2 -> model?.clockFragment!!
                3 -> model?.settingsFragment!!
                else -> model?.clockFragment!!
            }
        }
    }
}


