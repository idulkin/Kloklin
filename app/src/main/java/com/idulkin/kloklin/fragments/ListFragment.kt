package com.idulkin.kloklin.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.IntervalAction
import com.idulkin.kloklin.objects.Program
import com.idulkin.kloklin.adapters.ProgramRecyclerAdapter

/**
 * Display a list of programs, with a quick timer button at the top
 */
class ListFragment : Fragment() {

    val timerArrayList = ArrayList<Program>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Test values
        val timer = Interval("Every Minute", arrayListOf(IntervalAction(60, "Work")))
        var i = 0
        while (i < 5) {
            timerArrayList.add(Program("Test", "Test Description", arrayListOf(timer, timer, timer, timer, timer)))
            i++
        }
        //TODO:Replace previous with real list
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.fragment_program_list, container, false) as View

        //Set the RecyclerView
        val programListView = view.findViewById<RecyclerView>(R.id.program_recycler)
        programListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        programListView.adapter = ProgramRecyclerAdapter(timerArrayList)
        programListView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

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

    fun makeTimerArrayList(){
        timerArrayList.clear()
    }
}

