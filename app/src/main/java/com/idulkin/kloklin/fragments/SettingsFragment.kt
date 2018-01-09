package com.idulkin.kloklin.fragments

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.R

/**
 * Loads the preference screen xml
 */
class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

        val spListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
                val boop = sharedPrefs.getInt("pref_beep", R.raw.chime)
                val player = MediaPlayer.create(context, boop)
                player.start()
        }

        sharedPrefs.registerOnSharedPreferenceChangeListener(spListener)


    }


}
