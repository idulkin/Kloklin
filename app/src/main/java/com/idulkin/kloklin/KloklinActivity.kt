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

        model?.page?.observe(this, Observer<Int> { page ->
            if (page != null) {
                pager.currentItem = page
            }
        })
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
        model?.onMenuItemClicked(item)
        return true
    }

    /**
     * Back button opens previous fragment, or closes the app if there isn't one
     */
    override fun onBackPressed() {
        if (model?.previousPage() == false) {
            super.onBackPressed()
        }
    }

    /**
     * Open the clock fragment with the provided program
     */
    fun openClock(program: Program) {
        //Replace the current clock fragment
        model?.startNewProgram(program)
    }

    /**
     * Adapter class for the view pager
     */
    inner class FragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount() = 3

        override fun getItem(position: Int): Fragment {
            return when (position) {
                -1 -> model?.editFragment!! //May need to take this out of the viewpager
                0 -> model?.listFragment!!
                1 -> model?.clockFragment!!
                2 -> model?.settingsFragment!!
                else -> model?.editFragment!!
            }
        }
    }
}


