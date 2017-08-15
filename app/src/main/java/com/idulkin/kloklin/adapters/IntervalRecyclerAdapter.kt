package com.idulkin.kloklin.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.objects.Interval

/**
 * Created by igor.dulkin on 8/1/17.
 */
class IntervalRecyclerAdapter(var intervals: ArrayList<Interval>): RecyclerView.Adapter<IntervalRecyclerAdapter.IntervalViewHolder>() {

    class IntervalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: IntervalViewHolder?, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IntervalViewHolder {
        return IntervalViewHolder(LayoutInflater.from(parent?.context).inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int {
        return intervals.size
    }


}