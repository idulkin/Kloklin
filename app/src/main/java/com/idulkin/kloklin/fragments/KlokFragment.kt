package com.idulkin.kloklin.fragments

import android.content.res.Configuration
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.IntervalAction
import com.idulkin.kloklin.objects.Program
import kotlinx.android.synthetic.main.fragment_clock.*
import java.util.concurrent.TimeUnit

/**
 * Created by igor.dulkin on 8/28/17.
 */

class KlokFragment() : Fragment() {

    var program = Program("Minute", "Placeholder Minute Timer", arrayListOf(IntervalAction(60, "")))
    var time: Long = 0 //Time remaining in seconds
    var position = 0 //Current interval in program
    var playing = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_clock, container, false)


        //Display the title only in portrait orientation
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            clock_title.visibility = View.VISIBLE
            clock_title.text = program.name
        } else {
            clock_title.visibility = View.GONE
        }

        //Display current interval's text
        action_text.text = program.intervals[position].action

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        time = program.intervals[position].seconds


        var countDown = CountDown(this::nextInterval, this::tick, TimeUnit.SECONDS.toMillis(time), 1000)

        // Pause/Play button
        play_button.setOnClickListener {
            if (playing) {
                countDown.cancel()
                play_button.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
                playing = false
            } else {
                countDown.start()
                play_button.setImageDrawable(resources.getDrawable(R.drawable.big_play_button, resources.newTheme()))
                playing = true
            }
        }

        // Skip Forward button
        skip_forward_button.setOnClickListener {

        }

        // Skip Back button
        skip_back_button.setOnClickListener {

        }
    }

    /** Count Down Timer and related functions **/

    /**
     * One tick of the countdown timer
     */
    private fun tick(): Boolean {
        time++
        clock_face.text = String.format("%02d:%02d", time / 60, time % 60)

        return true
    }

    /**
     * The countdown timer finished
     */
    private fun nextInterval(): IntervalAction {
        position++

        //Beep
        val beeper = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(context, beeper).play()

        return program.intervals[position]
    }

    /**
     * Companion object to track the countdown
     */
    class CountDown(val nextInterval: () -> IntervalAction, val tick: () -> Boolean, millisInFuture: Long, countDownInterval: Long)
        : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            tick()
        }

        override fun onFinish() {
            nextInterval()
        }
    }
}