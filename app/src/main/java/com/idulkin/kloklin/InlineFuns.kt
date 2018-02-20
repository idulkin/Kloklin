package com.idulkin.kloklin

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

/**
 * Created by igor.dulkin on 8/13/17.
 *
 * Handy functions to inline
 */


/*
 * Inlines creating a snackbar, so that I only have to call .snack on a view
 */
inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}
