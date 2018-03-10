package com.idulkin.kloklin.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.idulkin.kloklin.Interval
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.adapters.IntervalRecyclerAdapter
import com.idulkin.kloklin.viewmodels.ActivityViewModel
import kotlinx.android.synthetic.main.fragment_edit.*

/**
 * Fragment for editing a program. A program is an arraylist of intervals,
 * and this shows a recyclerview of that array list, with an add button as
 * the last element.
 */
class EditFragment : Fragment() {

    val model: ActivityViewModel by lazy {
        ActivityViewModel.create(activity as KloklinActivity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false) as View
    }

    override fun onStart() {
        super.onStart()

        var intervals = arrayListOf<Interval>()
        if (model.programs.value != null) {
            intervals = model.programs.value!![model.editedProgram].intervals
        }

        userVisibleHint = false
        interval_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        interval_recycler.adapter = IntervalRecyclerAdapter(intervals)
        interval_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        add_interval_fab.setOnClickListener {
            model.programs.value!![model.editedProgram].intervals.add(Interval(5, "New"))
            interval_recycler.adapter.notifyDataSetChanged()
        }
    }

    override fun onStop() {
        super.onStop()

        model.updateProgram()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        // Sets the title and interval list when this fragment becomes visible
        if (userVisibleHint) {
            val program = model.programs.value!![model.editedProgram]
            program_title.setText(program.name, TextView.BufferType.EDITABLE)
            interval_recycler.adapter = IntervalRecyclerAdapter(program.intervals)
            interval_recycler.adapter.notifyDataSetChanged()
        } else {
            //Close soft keyboard when leaving this fragment
            if (view != null) {
                val imm = (activity as KloklinActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
        }
    }
}
