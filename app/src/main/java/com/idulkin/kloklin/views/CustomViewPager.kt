package com.idulkin.kloklin.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by igor.dulkin on 9/18/17.
 *
 * Extend ViewPager to disable swipes
 */
class CustomViewPager(context: Context, attrs: AttributeSet): ViewPager(context, attrs) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}