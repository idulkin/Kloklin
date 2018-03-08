package com.idulkin.kloklin

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.idulkin.kloklin.data.DBProgram
import com.idulkin.kloklin.objects.Interval

/**
 * Created by igor.dulkin on 8/13/17.
 *
 * Handy functions and vals to inline
 */


// Inlines creating a snackbar, so that I only have to call .snack on a view
inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}

// List of default values to populate the DB if it's empty
val defaults: List<DBProgram> = listOf(
        DBProgram("Test", "Test Description", arrayListOf(Interval(5, "First"), Interval(5, "Second"), Interval(5, "Third"), Interval(5, "Fourth"), Interval(5, "Fifth"))),
        DBProgram("Test", "Test Description", arrayListOf(Interval(5, "First"), Interval(5, "Second"), Interval(5, "Third"), Interval(5, "Fourth"), Interval(5, "Fifth")))
)
