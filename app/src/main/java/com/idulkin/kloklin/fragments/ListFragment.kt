package com.idulkin.kloklin.fragments
import kotlinx.android.synthetic.main.fragment_program_list.*

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.IntervalAction
import com.idulkin.kloklin.objects.Program
import com.idulkin.kloklin.adapters.ProgramRecyclerAdapter

/**
 * Display a list of programs, with a quick timer button at the top
 */
class ListFragment : Fragment() {

    val programs = ArrayList<Program>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Test values
        val timer = IntervalAction(5, "Test")
        var i = 0
        while (i < 5) {
            programs.add(Program("Test", "Test Description", arrayListOf(timer, timer, timer, timer, timer)))
            i++
        }
        //TODO:Replace previous with real list
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_program_list, container, false) as View
    }

    override fun onStart() {
        super.onStart()

        //Set the RecyclerView
        program_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        program_recycler.adapter = ProgramRecyclerAdapter(programs)
        program_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    /**
     * Create an ArrayList from the database
     */

    fun makeProgramList(){
        programs.clear()
    }
}

