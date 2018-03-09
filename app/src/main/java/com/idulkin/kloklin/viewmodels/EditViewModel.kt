package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import com.idulkin.kloklin.Interval
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.data.Program

/**
 * Created by igor on 2/20/18.
 */
class EditViewModel : ViewModel() {

    companion object {
        fun create(activity: KloklinActivity): EditViewModel {
            return ViewModelProviders.of(activity).get(EditViewModel::class.java)
        }
    }


    var program = Program(0, "Placeholder", "", arrayListOf(Interval(10, "")))
}