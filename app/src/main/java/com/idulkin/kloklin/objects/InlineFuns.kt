package com.idulkin.kloklin.objects

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

/**
 * Created by igor.dulkin on 8/13/17.
 */

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}
