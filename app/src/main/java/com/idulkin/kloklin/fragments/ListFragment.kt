package com.idulkin.kloklin.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.adapters.ProgramRecyclerAdapter
import com.idulkin.kloklin.data.Program
import com.idulkin.kloklin.placeholder
import com.idulkin.kloklin.viewmodels.ActivityViewModel
import kotlinx.android.synthetic.main.fragment_program_list.*

/**
 * Display a list of programs, with a quick timer button at the top
 */
class ListFragment : Fragment() {

    private val model: ActivityViewModel by lazy {
        ActivityViewModel.create(activity as KloklinActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_program_list, container, false) as View
    }

    override fun onStart() {
        super.onStart()

        //Set the RecyclerView
        program_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        program_recycler.adapter = ProgramRecyclerAdapter(model.programs.value ?: arrayListOf<Program>())
        program_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        model.programs.observe(this, Observer {
            //Workaround for notifyDataSetChanged not updating async
            program_recycler.swapAdapter(ProgramRecyclerAdapter(model.programs.value!!), true)
            //program_recycler.adapter.notifyDataSetChanged()
        })

        add_program_fab.setOnClickListener {
            model.addProgram()
            program_recycler.adapter.notifyDataSetChanged()
            model.editProgram(model.programs.value!!.last())
        }
    }
}

