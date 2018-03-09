package com.idulkin.kloklin.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.adapters.IntervalRecyclerAdapter
import com.idulkin.kloklin.viewmodels.EditViewModel
import kotlinx.android.synthetic.main.fragment_timer_list.*

/**
 * Fragment for editing a program. A program is an arraylist of intervals,
 * and this shows a recyclerview of that array list, with an add button as
 * the last element.
 */
class EditFragment: Fragment() {

    val model: EditViewModel by lazy {
        EditViewModel.create(activity as KloklinActivity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer_list, container,false) as View
    }

    override fun onStart() {
        super.onStart()

        val activity = activity as KloklinActivity

        userVisibleHint = false
        interval_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        interval_recycler.adapter = IntervalRecyclerAdapter(activity.model.editedProgram.intervals)
        interval_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    /*
     * Sets the title and interval list when this fragment becomes visible
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            val program = (activity as KloklinActivity).model.editedProgram
            program_title.text = program.name
            interval_recycler.adapter = IntervalRecyclerAdapter(program.intervals)
            interval_recycler.adapter.notifyDataSetChanged()
        }
    }
}
