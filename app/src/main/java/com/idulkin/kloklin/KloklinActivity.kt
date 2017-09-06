package com.idulkin.kloklin

import android.app.KeyguardManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.idulkin.kloklin.fragments.EditFragment
import com.idulkin.kloklin.fragments.KlokFragment
import com.idulkin.kloklin.fragments.ListFragment
import com.idulkin.kloklin.fragments.SettingsFragment
import com.idulkin.kloklin.objects.Program
import kotlinx.android.synthetic.main.activity_kloklin.*
import kotlinx.android.synthetic.main.fragment_clock.*
import kotlinx.android.synthetic.main.fragment_clock.*
import java.util.*

class KloklinActivity : FragmentActivity() {

    private var clockFragment = KlokFragment() //The current clock
    private val backStack = Stack<Int>() //Tracks previous fragments for the back button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kloklin)
        setActionBar(kloklin_toolbar)
        action_menu.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

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
        pager.currentItem = when (item.itemId) {
            R.id.action_list -> PAGE.LIST.position
            R.id.action_settings -> PAGE.SETTINGS.position
            R.id.action_clock -> PAGE.CLOCK.position
            else -> PAGE.CLOCK.position
        }
        backStack.push(pager.currentItem)
        return true
    }

    /**
     * Back button opens previous fragment
     */
    override fun onBackPressed() {
        if (backStack.empty()) {
            super.onBackPressed()
        } else {
            pager.currentItem = backStack.pop()
        }
    }

    /**
     * Open the clock fragment with the provided program
     */
    fun openClock(program: Program) {
        //Replace the current clock fragment
        clockFragment = KlokFragment()
        clockFragment.program = program
        val transaction = supportFragmentManager.beginTransaction()
        transaction.detach(clockFragment)
        transaction.attach(clockFragment)
        transaction.commit()

        pager.adapter.notifyDataSetChanged()
        pager.currentItem = PAGE.CLOCK.position
        backStack.push(pager.currentItem)
    }

    inner class FragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount() = 4

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> EditFragment()
                1 -> ListFragment()
                2 -> clockFragment
                3 -> SettingsFragment()
                else -> clockFragment
            }
        }
    }

    enum class PAGE(val position: Int) {
        EDIT(0),
        LIST(1),
        CLOCK(2),
        SETTINGS(3)
    }
}


