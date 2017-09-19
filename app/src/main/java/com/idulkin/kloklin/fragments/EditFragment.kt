package com.idulkin.kloklin.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.Interval

/**
 * Fragment for editing a program. A program is an arraylist of intervals,
 * and this shows a recyclerview of that array list, with an add button as
 * the last element.
 */
class EditFragment: Fragment() {

    val timerArrayList = ArrayList<Interval>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment and set the recycler list
        val view = inflater?.inflate(R.layout.fragment_timer_list, container, false) as View
        val timerList = view.findViewById<RecyclerView>(R.id.timer_recycler)
        timerList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
   }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * Create an ArrayList from the database
     */

}
