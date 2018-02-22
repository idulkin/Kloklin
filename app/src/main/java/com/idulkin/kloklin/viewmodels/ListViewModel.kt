package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.KloklinApplication
import com.idulkin.kloklin.data.DBManager
import com.idulkin.kloklin.objects.Program
import kotlinx.coroutines.experimental.async

/**
 * Created by igor.dulkin on 9/18/17.
 */
class ListViewModel : ViewModel() {

    var programs: MutableLiveData<ArrayList<Program>> = MutableLiveData()
//    private var dbManager: DatabaseManager? = null
    private lateinit var dbManager: DBManager

    /**
     * Initialize a list for the recycler adapter.
     */
    fun init(context: Context) {
        //Get the program list from the database
//        dbManager = DatabaseManager(context)
//        programs.value = dbManager?.queryPrograms() ?: programs.value
//        val db = Room
//                .databaseBuilder(context, AppDatabase::class.java, "programs")
//                .build()
//        val programsFromDB = db.programDao().allPrograms()
//        for (program in programsFromDB) {
//            programs.value?.add(program.getProgram())
//        }

//        dbManager = DBManager(context)
//        programs.value = dbManager.queryPrograms()

        programs.value = arrayListOf()

        async {
            val dbPrograms = KloklinApplication.db.programDao().allPrograms()
            for (program in dbPrograms) {
                programs.value?.add(program.getProgram())
            }
        }
    }

    /**
     * Delete a program from the list
     */
    fun deleteProgram(program: Program) {
        programs.value?.remove(program)
//        dbManager?.deleteProgram(program.name)
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