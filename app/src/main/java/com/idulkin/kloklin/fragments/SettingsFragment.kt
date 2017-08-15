package com.idulkin.kloklin.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.R

/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class SettingsFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment and set the recycler list
        val view = inflater?.inflate(R.layout.fragment_settings, container, false) as View

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
   }

    override fun onDetach() {
        super.onDetach()
    }

}
