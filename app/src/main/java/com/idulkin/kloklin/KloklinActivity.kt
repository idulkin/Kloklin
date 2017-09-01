package com.idulkin.kloklin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import com.idulkin.kloklin.fragments.KlokFragment
import com.idulkin.kloklin.fragments.ListFragment
import com.idulkin.kloklin.fragments.SettingsFragment
import com.idulkin.kloklin.objects.Program
import kotlinx.android.synthetic.main.activity_kloklin.*

class KloklinActivity : FragmentActivity() {

    private var clockFragment = KlokFragment() //The current clock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kloklin)
        setActionBar(kloklin_toolbar)
        action_menu.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }
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
        return when (item.itemId) {
            R.id.action_list -> openFragment(ListFragment())
            R.id.action_settings -> openFragment(SettingsFragment())
            R.id.action_clock -> openFragment(clockFragment)
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Back button opens previous fragment
     */
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Open the clock fragment with the provided program
     */
    fun openClock(program: Program) {
        clockFragment = KlokFragment()
        clockFragment.program = program
        openFragment(clockFragment)
    }

    private fun openFragment(fragment: Fragment): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        return true
    }


}
