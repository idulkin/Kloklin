package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import com.idulkin.kloklin.KloklinActivity

/**
 * Created by igor.dulkin on 9/18/17.
 */
class ListViewModel : ViewModel() {


    /**
     * Companion object returns the view model from ViewModelProviders,
     * making this a singleton
     */
    companion object {
        fun create(activity: KloklinActivity): ListViewModel {
            return ViewModelProviders.of(activity).get(ListViewModel::class.java)
        }
    }
}