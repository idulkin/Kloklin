package com.idulkin.kloklin

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.Menu
import android.view.MenuItem
import com.idulkin.kloklin.models.ActivityViewModel
import com.idulkin.kloklin.objects.Program
import kotlinx.android.synthetic.main.activity_kloklin.*

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

        //ViewPager contains all fragments
        pager.adapter = FragmentAdapter(supportFragmentManager)
        if (savedInstanceState == null) {
            pager.currentItem = ActivityViewModel.PAGE.LIST.position
        }

        model.page.observe(this, Observer<Int> { page ->
            if (page != null) {
                pager.currentItem = page
            }
        })
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
                0 -> model.editFragment
                1 -> model.listFragment
                2 -> model.clockFragment
                3 -> model.settingsFragment
                else -> model.clockFragment
            }
        }
    }
}


