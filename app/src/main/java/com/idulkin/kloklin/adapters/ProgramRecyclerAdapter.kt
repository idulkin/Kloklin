package com.idulkin.kloklin.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.Program
import com.idulkin.kloklin.objects.snack

/**
 * Created by igor.dulkin on 8/1/17.
 */
class ProgramRecyclerAdapter(var programs: ArrayList<Program>) : RecyclerView.Adapter<ProgramRecyclerAdapter.ProgramViewHolder>() {

    class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.program_name)
        val descView = itemView.findViewById<TextView>(R.id.program_desc)
        val overflowView = itemView.findViewById<ImageView>(R.id.program_overflow)

        /**
         * Bind a holder. Sets click listeners to launch clock and open overflow menu
         */
        fun bind(program: Program) {
            nameView.text = program.name
            descView.text = program.desc

            itemView.setOnClickListener {
                val activity = itemView.context as KloklinActivity
                activity.openClock(program)
            }

            //Overflow menu
            overflowView.setOnClickListener {
                val menuContext = overflowView.context
                val popupMenu = PopupMenu(menuContext, overflowView)
                popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.program_edit -> overflowClick(overflowView)
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