package com.idulkin.kloklin.adapters

import android.app.ListFragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.Program
import com.idulkin.kloklin.snack
import kotlinx.android.synthetic.main.program_list_entry.view.*

/**
 * Created by igor.dulkin on 8/1/17.
 */
class ProgramRecyclerAdapter(var programs: ArrayList<Program>) : RecyclerView.Adapter<ProgramRecyclerAdapter.ProgramViewHolder>() {

    inner class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * Bind a holder. Sets click listeners to launch clock and open overflow menu
         */
        fun bind(program: Program) {
            itemView.program_name.text = program.name
            itemView.program_desc.text = program.desc

            itemView.setOnClickListener {
                val activity = itemView.context as KloklinActivity
                activity.openClock(program)
            }

            //Overflow menu
            val overflowView = itemView.program_overflow
            overflowView.setOnClickListener {
                val popupMenu = PopupMenu(overflowView.context, overflowView)
                popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.program_edit -> editProgram(program)
                        R.id.program_move -> overflowClick(overflowView)
                        R.id.program_delete -> overflowClick(overflowView)
                        else -> overflowClick(overflowView)
                    }
                }

                popupMenu.inflate(R.menu.menu_program)
                popupMenu.show()
            }
        }

        //Handles a click on this holder's overflow menu item
        fun overflowClick(view: View): Boolean {
            view.snack("TODO: Implement this menu action")
            //TODO: Implement menu actions
            return true
        }

        fun editProgram(program: Program): Boolean {
            val activity = itemView.context as KloklinActivity
            activity.model.editProgram(program)
            return true
        }
    }

    override fun onBindViewHolder(holder: ProgramViewHolder?, position: Int) {
        holder?.bind(programs[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProgramViewHolder {
        return ProgramViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.program_list_entry, parent, false))
    }

    override fun getItemCount(): Int {
        return programs.size
    }
}