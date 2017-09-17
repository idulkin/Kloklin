package com.idulkin.kloklin.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.media.RingtoneManager
import android.os.CountDownTimer
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.IntervalAction
import com.idulkin.kloklin.objects.Program
import javax.inject.Inject


/**
 * Created by igor.dulkin on 9/15/17.
 *
 * A view model for the clock fragment. Keeps clock data correct regardless of fragment state
 */
class ClockViewModel : ViewModel() {

    var program = Program("Minute", "Placeholder Minute Timer", arrayListOf(IntervalAction(60, "")))

    var time: MutableLiveData<Long> = MutableLiveData() //Time remaining in seconds
    var title: MutableLiveData<String> = MutableLiveData() //Program name
    var description: MutableLiveData<String> = MutableLiveData() //Current interval text
    var playing: MutableLiveData<Boolean> = MutableLiveData() //Is the countdown running?

    var countDown = CountDown(0,1) //Initialize countdown at 0 seconds
    var position = 0 //Current interval in program

    fun init(program: Program?) {
        this.program = program ?: this.program
    }

    //Start a new program
    fun newProgram(newProgram: Program?) {
        program = newProgram ?: program
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

    /**
     * Inner class to track the countdown
     */
    inner class CountDown(millisInFuture: Long, countDownInterval: Long)
        : CountDownTimer(millisInFuture, countDownInterval / 10) {
        override fun onTick(millisUntilFinished: Long) {
            time.value = millisUntilFinished / 1000
        }

        override fun onFinish() {
//            val beeper = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//            RingtoneManager.getRingtone(context, beeper).play()

            position++
            if (position == program.intervals.count()) {
                //End of the last interval. Set the play button to restart the program
//                finishProgram()
                description.value = "FINISH"
            }

            startInterval()
        }
    }
}