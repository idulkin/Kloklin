package com.idulkin.kloklin.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment and set the recycler list
        return inflater.inflate(R.layout.fragment_settings, container, false) as View
    }
}
