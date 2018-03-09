package com.idulkin.kloklin

import android.support.design.widget.Snackbar
import android.view.View
import com.idulkin.kloklin.data.Program

/**
 * Created by igor.dulkin on 8/13/17.
 *
 * Everything that doesn't deserve its own file
 */

// POJO for an interval
data class Interval(val seconds: Long, val action: String)

// Inlines creating a snackbar, so that I only have to call .snack on a view
inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}

// List of default values to populate the DB if it's empty
val defaults: List<Program> = listOf(
        Program(0, "Test", "Test Description", arrayListOf(Interval(5, "First"), Interval(5, "Second"), Interval(5, "Third"), Interval(5, "Fourth"), Interval(5, "Fifth"))),
        Program(1, "TestOne", "Test Description", arrayListOf(Interval(5, "First"), Interval(5, "Second"), Interval(5, "Third"), Interval(5, "Fourth"), Interval(5, "Fifth"))),
        Program(2, "TestTwo", "Test Description", arrayListOf(Interval(5, "First"), Interval(5, "Second"), Interval(5, "Third"), Interval(5, "Fourth"), Interval(5, "Fifth")))
)

// Enum to track page numbers for the ViewPager
enum class PAGE(val pos: Int) {
    EDIT(0),
    LIST(1),
    CLOCK(2),
    SETTINGS(3)
}