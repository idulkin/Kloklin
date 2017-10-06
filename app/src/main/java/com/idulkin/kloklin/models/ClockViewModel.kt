package com.idulkin.kloklin.models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.CountDownTimer
import com.idulkin.kloklin.fragments.ClockFragment
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program


/**
 * Created by igor.dulkin on 9/15/17.
 *
 * A view model for the clock fragment. Keeps clock data correct regardless of fragment state
 */
class ClockViewModel : ViewModel() {

    var program = Program("One Minute", "Placeholder Minute Timer", arrayListOf(Interval(60, "")))

    var time: MutableLiveData<Long> = MutableLiveData() //Time remaining in seconds
    var title: MutableLiveData<String> = MutableLiveData() //Program name
    var description: MutableLiveData<String> = MutableLiveData() //Current interval text
    var playing: MutableLiveData<Boolean> = MutableLiveData() //Is the countdown running?

    var countDown = CountDown(0, 1) //Initialize countdown at 0 seconds
    var position = 0 //Current interval in program

    fun newProgram(newProgram: Program) {
        this.program = newProgram

        countDown.cancel()
        position = 0
        playing.value = false
        time.value = program.intervals[position].seconds
        title.value = program.name
        description.value = program.intervals[position].action
        countDown = CountDown(time.value!! * 1000, 1000)
    }

    fun startInterval() {
        //Keep the position in bounds, and don't start playing past the end of the list
        if (position >= program.intervals.count()) {
            position = program.intervals.count() - 1
        } else {
            if (position < 0) position = 0

            val interval = program.intervals[position]
            time.value = interval.seconds

            countDown.cancel()
            countDown = CountDown(time.value!! * 1000, 1000)
            countDown.start()
            playing.value = true
        }
    }

    fun onPlayButtonClicked() {
        if ((position == program.intervals.size - 1) && time.value == 0L) {
            //End of the program, reset
            newProgram(program)
        } else {
            if (playing.value == true) {
                countDown.cancel()
                playing.value = false
            } else {
                countDown = CountDown(time.value!! * 1000, 1000)
                countDown.start()
                playing.value = true
            }
        }
    }

    fun onSkipForwardClicked() {
        position++
        startInterval()
    }

    fun onSkipBackClicked() {
        val elapsedTime = time.value
        if (elapsedTime!! < program.intervals[position].seconds - 2) {
            //More than 2 seconds into the timer, reset current interval
            startInterval()
        } else {
            //Less than 2 seconds into the timer, skip to previous interval
            position--
            startInterval()
        }
    }

    /**
     * Companion object to instantiate a persistent model
     */
    companion object {
        fun create(fragment: ClockFragment): ClockViewModel {
            return ViewModelProviders.of(fragment).get(ClockViewModel::class.java)
        }
    }

    /**
     * Inner class to track the countdown
     */
    inner class CountDown(millisInFuture: Long, countDownInterval: Long)
        : CountDownTimer(millisInFuture, countDownInterval / 10) {
        override fun onTick(millisUntilFinished: Long) {
            time.value = millisUntilFinished / 1000
        }

        override fun onFinish() {
            position++
            if (position == program.intervals.count()) {
                //End of the last interval. Set the play button to restart the program
                playing.value = false
                description.value = "FINISH"
            }

            startInterval()
        }
    }
}