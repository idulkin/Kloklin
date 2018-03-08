package com.idulkin.kloklin.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.media.MediaPlayer
//TODO: create an interface for the countdown to avoid this import
import android.os.CountDownTimer
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.data.Program
import com.idulkin.kloklin.objects.Interval


/**
 * Created by igor.dulkin on 9/15/17.
 *
 * A view model for the clock fragment. Keeps clock data correct regardless of fragment state
 */
class ClockViewModel : ViewModel() {

    /**
     * Companion object to instantiate a persistent model
     */
    companion object {
        fun create(activity: KloklinActivity): ClockViewModel {
            return ViewModelProviders.of(activity).get(ClockViewModel::class.java)
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
            mediaPlayer?.start()

            position++
            if (position == program.intervals.count()) {
                //End of the last interval. Set the play button to restart the program
                playing.value = false
                description.value = "FINISH"
            }

            startInterval()
        }
    }

    /** LiveData Observables **/
    var time: MutableLiveData<Long> = MutableLiveData() //Time remaining in seconds
    var title: MutableLiveData<String> = MutableLiveData() //Program name
    var description: MutableLiveData<String> = MutableLiveData() //Current interval text
    var playing: MutableLiveData<Boolean> = MutableLiveData() //Is the countdown running?

    /** Not observable vars **/
    var program = Program("One Minute", "Placeholder Minute Timer", arrayListOf(Interval(60, "")))
    var countDown = CountDown(0, 1) //Initialize countdown at 0 seconds
    var position = 0 //Current interval in program
    var mediaPlayer: MediaPlayer? = null //Plays a beep at the end of an interval, set in fragment onCreate

    fun newProgram(newProgram: Program) {
        this.program = newProgram
        countDown.cancel()
        position = 0

        title.value = program.name
        time.value = program.intervals[position].seconds
        description.value = program.intervals[position].action
        playing.value = false

        countDown = CountDown(time.value!! * 1000, 1000)
    }

    fun startInterval() {
        if (position >= program.intervals.count()) {
            //Keep the position in bounds, and don't start playing past the end of the list
            position = program.intervals.count() - 1
        } else {
            if (position < 0) position = 0

            val interval = program.intervals[position]
            description.value = interval.action
            time.value = interval.seconds
            val startTime = time.value!! * 1000 + 999 //Give the timer an extra second to display the start time

            countDown.cancel()
            countDown = CountDown(startTime, 1000)
            if (playing.value!!) {
                countDown.start()
            }
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
}