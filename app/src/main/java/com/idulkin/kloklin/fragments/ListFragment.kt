package com.idulkin.kloklin.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.R
import com.idulkin.kloklin.adapters.ProgramRecyclerAdapter
import com.idulkin.kloklin.models.ListViewModel
import com.idulkin.kloklin.objects.Program
import kotlinx.android.synthetic.main.fragment_program_list.*

/**
 * Display a list of programs, with a quick timer button at the top
 */
class ListFragment : Fragment() {

    private val model: ListViewModel by lazy {
        ListViewModel.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            model.init(context)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_program_list, container, false) as View
    }

    override fun onStart() {
        super.onStart()

        //Set the RecyclerView
        program_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        program_recycler.adapter = ProgramRecyclerAdapter(model.programs.value!!)
        program_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        model.programs.observe(this, Observer {
            program_recycler.adapter.notifyDataSetChanged()
        })
    }
}

