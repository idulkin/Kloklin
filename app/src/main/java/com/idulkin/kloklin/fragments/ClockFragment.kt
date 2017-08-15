package com.idulkin.kloklin.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.idulkin.kloklin.R
import com.idulkin.kloklin.objects.IntervalAction
import com.idulkin.kloklin.objects.Interval


/**
 * A countdown timer
 */
class ClockFragment : Fragment() {

    //TODO: Replace this timer with one from an intent
    var timer = Interval("Every Minute", arrayListOf(IntervalAction(60, "Work")))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.fragment_clock, container, false)

        val clockface = view?.findViewById<TextView>(R.id.clock_face)
        val title = view?.findViewById<TextView>(R.id.clock_title)
        val action = view?.findViewById<TextView>(R.id.action_text)
        val playbutton = view?.findViewById<ImageButton>(R.id.play_button)

        //Display the title and bottom margin only in portrait orientation
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            title?.text = timer.name
        } else {
            title?.visibility = View.GONE
            view?.findViewById<View>(R.id.the_bottom)?.visibility = View.GONE
        }

        action?.text = timer.sets[0].action

        if(!activity.isChangingConfigurations) {
            //Set the start time and create a countdown object
            var time = timer.sets[0].duration
            clockface?.text = String.format("%02d:%02d", time / 60, time % 60)
            var countDown = CountDown(clockface, time.toLong() * 1000, 1000)

            //Play button control
            var playing = false
            playbutton?.setOnClickListener {
                if (!playing) {
                    playbutton.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
                    countDown = CountDown(clockface, time.toLong() * 1000, 1000)
                    countDown.start()
                    playing = true
                } else {
                    playbutton.setImageDrawable(resources.getDrawable(R.drawable.big_play_button, resources.newTheme()))
                    countDown.cancel()
                    time = countDown.secondsRemaining
                    playing = false
                }
            }
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * Inner class to track remaining time
     */
    class CountDown(val clockface: TextView?, millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        var secondsRemaining = millisInFuture.toInt() / 1000

        override fun onTick(millisUntilFinished: Long) {
            val min = millisUntilFinished / 60000
            val sec = millisUntilFinished / 1000 % 60
            clockface?.text = String.format("%02d:%02d", min, sec)
            secondsRemaining--
        }

        override fun onFinish() {
            //TODO: Beep
        }
    }

}
