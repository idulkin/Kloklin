package com.idulkin.kloklin.models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.data.DatabaseManager
import com.idulkin.kloklin.fragments.ListFragment
import com.idulkin.kloklin.objects.Program

/**
 * Created by igor.dulkin on 9/18/17.
 */
class ListViewModel : ViewModel() {

//    var programs = ArrayList<Program>()
    var programs: MutableLiveData<ArrayList<Program>> = MutableLiveData()
    private var dbManager: DatabaseManager? = null

    /**
     * Initialize a list for the recycler adapter.
     */
    fun init(context: Context) {
        //Get the program list from the database
        dbManager = DatabaseManager(context)
        programs.value = dbManager?.queryPrograms() ?: programs.value
    }

    /**
     * Delete a program from the list
     */
    fun deleteProgram(program: Program) {
        programs.value?.remove(program)
        dbManager?.deleteProgram(program.name)
    }

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