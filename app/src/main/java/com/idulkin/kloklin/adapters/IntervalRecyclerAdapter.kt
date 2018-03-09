package com.idulkin.kloklin.adapters

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.Interval
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.snack
import kotlinx.android.synthetic.main.interval_list_entry.view.*

/**
 * Created by igor on 2/19/18.
 */
class IntervalRecyclerAdapter(var intervals: ArrayList<Interval>) : RecyclerView.Adapter<IntervalRecyclerAdapter.IntervalViewHolder>() {

    inner class IntervalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(interval: Interval) {
            itemView.interval_name.text = interval.action
            itemView.interval_time.text = String.format("%02d:%02d", interval.seconds / 60, interval.seconds % 60)

            itemView.setOnClickListener {
                //Open edit dialog
            }

            //Overflow menu
            val overflowView = itemView.interval_overflow
            overflowView.setOnClickListener {
                val popupMenu = PopupMenu(overflowView.context, overflowView)
                popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.program_edit -> overflowClick(overflowView)
                        R.id.program_move -> overflowClick(overflowView)
                        R.id.program_delete -> deleteInterval(interval)
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

        fun deleteInterval(interval: Interval): Boolean {
            val activity = itemView.context as KloklinActivity
            activity.model.editedProgram.intervals.remove(interval)
            notifyDataSetChanged()
            return true
        }
    }

    override fun onBindViewHolder(holder: IntervalViewHolder?, position: Int) {
        holder?.bind(intervals[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IntervalViewHolder {
        return IntervalViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.interval_list_entry, parent, false))
    }

    override fun getItemCount(): Int {
        return intervals.size
    }

}