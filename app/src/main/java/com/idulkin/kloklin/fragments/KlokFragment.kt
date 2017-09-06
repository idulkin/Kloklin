package com.idulkin.kloklin.fragments

import kotlinx.android.synthetic.main.fragment_clock.*

import android.content.res.Configuration
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.IntervalAction
import com.idulkin.kloklin.objects.Program
import java.util.concurrent.TimeUnit

/**
 * Created by igor.dulkin on 8/28/17.
 */

class KlokFragment : Fragment() {

    var program = Program("Minute", "Placeholder Minute Timer", arrayListOf(IntervalAction(60, "")))
    var time: Long = 0 //Time remaining in seconds
    var position = 0 //Current interval in program

    var countDown = CountDown(TimeUnit.SECONDS.toMillis(time), 1000)
    var playing = false //Is the countdown running?

    fun recreateFragment(){
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putLong("Time", time)
        outState?.putInt("Position", position)
        outState?.putBoolean("Playing", playing)

        //Save the current program as a shared preference
        val editor = context.getSharedPreferences("", 0).edit()
        val json = Gson().toJson(program)
        editor.putString("CurrentProgram", json)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Restore last program from shared prefs, if null keep current program
        val json = context.getSharedPreferences("", 0).getString("CurrentProgram", "")
        program = Gson().fromJson(json, Program::class.java) ?: program

        if (savedInstanceState != null) {
            time = savedInstanceState.getLong("Time")
            position = savedInstanceState.getInt("Position")
            playing = savedInstanceState.getBoolean("Playing")
        } else {
            time = program.intervals[position].seconds
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_clock, container, false)
    }

    /**
     * Button listeners set onStart
     */
    override fun onStart() {
        super.onStart()

        //Display the title only in portrait orientation
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            clock_title.visibility = View.VISIBLE
            clock_title.text = program.name
            bottom_margin.visibility = View.VISIBLE
        } else {
            clock_title.visibility = View.GONE
            bottom_margin.visibility = View.GONE
        }

        if (time == 0L && position == program.intervals.count() - 1) {
            //The timer is finished, display restart button
            finishProgram()
        } else {
            //Display current interval's text and time
            action_text.text = program.intervals[position].action
            clock_face.text = String.format("%02d:%02d", time / 60, time % 60)

            countDown = CountDown(TimeUnit.SECONDS.toMillis(time), 1000)
            if (playing) {
                countDown.start()
                play_button.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
            }

            // Pause/Play button
            play_button.setOnClickListener {
                playing = if (playing) {
                    countDown.cancel()
                    play_button.setImageDrawable(resources.getDrawable(R.drawable.big_play_button, resources.newTheme()))
                    false
                } else {
                    countDown.start()
                    play_button.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
                    true
                }
            }

            // Skip Forward button
            skip_forward_button.setOnClickListener {
                position++
                startInterval()
            }

            // Skip Back button
            skip_back_button.setOnClickListener {
                if (time < program.intervals[position].seconds - 2) {
                    //More than 2 seconds into the timer, reset current interval
                    startInterval()
                } else {
                    //Less than 2 seconds into the timer, skip to previous interval
                    position--
                    startInterval()
                }
            }
        }
    }

    /**
     * Pause the countdown when the fragment pauses
     */
    override fun onPause() {
        super.onPause()
        countDown.cancel()
        play_button.setImageDrawable(resources.getDrawable(R.drawable.big_play_button, resources.newTheme()))
        playing = false
    }

    fun pauseClock() {
        countDown.cancel()
        play_button.setImageDrawable(resources.getDrawable(R.drawable.big_play_button, resources.newTheme()))
        playing = false
    }

    fun startClock() {
        countDown.start()
        play_button.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
        playing = true
    }

    /**
     * Start the current interval
     *
     * Set title and description text
     * Reset the countdown for the current interval and start it
     */
    fun startInterval() {
        //Keep the position in bounds, and don't start playing past the end of the list
        if (position >= program.intervals.count()) {
            position = program.intervals.count() - 1
        } else {
            if (position < 0) position = 0

            val interval = program.intervals[position]
            time = interval.seconds
            action_text.text = interval.action

            countDown.cancel()
            countDown = CountDown(TimeUnit.SECONDS.toMillis(time), 1000)
            countDown.start()
            playing = true
            play_button.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
        }
    }

    /**
     * When at the end of the program, set the screen to restart
     */
    fun finishProgram() {
        clock_face.text = String.format("%02d:%02d", time / 60, time % 60)
        action_text.text = getString(R.string.finish)
        skip_forward_button.visibility = View.GONE
        skip_back_button.visibility = View.GONE
        play_button.setImageDrawable(resources.getDrawable(R.drawable.big_restart_button, resources.newTheme()))
        play_button.setOnClickListener {
            skip_forward_button.visibility = View.VISIBLE
            skip_back_button.visibility = View.VISIBLE
            play_button.setImageDrawable(resources.getDrawable(R.drawable.big_play_button, resources.newTheme()))
            time = program.intervals[position].seconds
            position = 0
            playing = false
            onStart()
        }
    }

    /**
     * Inner class to track the countdown
     */
    inner class CountDown(millisInFuture: Long, countDownInterval: Long)
        : CountDownTimer(millisInFuture, countDownInterval / 10) {
        var count = 0
        override fun onTick(millisUntilFinished: Long) {
            time = millisUntilFinished / 1000
            //For precision, ticks ten times per update
            if (++count == 10) {
                clock_face.text = String.format("%02d:%02d", time / 60, time % 60)
                count = 0
            }
        }

        override fun onFinish() {
            clock_face.text = String.format("%02d:%02d", time / 60, time % 60)
            val beeper = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            RingtoneManager.getRingtone(context, beeper).play()

            position++
            if (position == program.intervals.count()) {
                //End of the last interval. Set the play button to restart the program
                finishProgram()
            }

            startInterval()
        }
    }
}