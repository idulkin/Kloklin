package com.idulkin.kloklin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ActionMenuView
import android.widget.Toast
import com.idulkin.kloklin.fragments.ClockFragment
import com.idulkin.kloklin.fragments.SettingsFragment
import com.idulkin.kloklin.fragments.ListFragment

class KloklinActivity: FragmentActivity() {

    //Private global fragments to prevent instantiating new fragments for the manager
    private val clockFragment = ClockFragment()
    private val listFragment = ListFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kloklin)
        setActionBar(findViewById(R.id.kloklin_toolbar))
        findViewById<ActionMenuView>(R.id.action_menu).setOnMenuItemClickListener {
            menuItem -> onOptionsItemSelected(menuItem)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_kloklin, menu)
        menuInflater.inflate(R.menu.menu_kloklin, findViewById<ActionMenuView>(R.id.action_menu).menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        when (item.itemId) {
            R.id.action_list -> return openFragment(listFragment)
            R.id.action_settings -> return openFragment(settingsFragment)
            R.id.action_clock -> return openFragment(clockFragment)
            else -> return super.onOptionsItemSelected(item)
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

    private fun openFragment(fragment: Fragment): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        return true
    }


}
