package com.idulkin.kloklin.fragments

import android.content.Context
import android.content.res.Configuration
import android.media.RingtoneManager
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
import com.idulkin.kloklin.objects.Program


/**
 * The face of the interval timer
 */
class ClockFragment : Fragment() {

    private var interval = Interval("One Minute", arrayListOf(IntervalAction(60, "Go")))
    var program = Program("Minute", "Placeholder Minute Timer", arrayListOf(interval))
    var time = 0 //Current time remaining on the clock
    var pos = 0 //Current position in the program
    var rootView: View? = null

    var countDown = CountDown(rootView?.findViewById(R.id.clock_face), time.toLong() * 1000, 1000, this::nextInterval)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.fragment_clock, container, false)
        rootView = view

        setClock()
        setPlayButton(false)

        //Skip forward button
        rootView?.findViewById<ImageButton>(R.id.skip_forward_button)?.setOnClickListener {
            nextInterval()
        }

        //Skip backward button
        rootView?.findViewById<ImageButton>(R.id.skip_back_button)?.setOnClickListener {
            pos--
            nextInterval()
        }

        return view
    }

    //Start the next interval in the program
    fun nextInterval(): Boolean {
        pos++
        if (pos > program.intervals.size) {
            //end of the program
            pos--
            return false
        } else {
            interval = program.intervals[pos]

            setClock()
            setPlayButton(true)

            val clockface = rootView?.findViewById<TextView>(R.id.clock_face)
            countDown = CountDown(clockface, time.toLong() * 1000, 1000, this::nextInterval)
            countDown.start()

            return true
        }
    }

    //Sets the text for the clock, title, and description
    fun setClock() {
        val clockface = rootView?.findViewById<TextView>(R.id.clock_face)
        val title = rootView?.findViewById<TextView>(R.id.clock_title)
        val action = rootView?.findViewById<TextView>(R.id.action_text)

        time = interval.sets[0].duration
        clockface?.text = String.format("%02d:%02d", time / 60, time % 60)
        //Display the title only in portrait orientation
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            title?.text = interval.name
        } else {
            title?.visibility = View.GONE
            view?.findViewById<View>(R.id.the_bottom)?.visibility = View.GONE
        }

        action?.text = interval.sets[0].action
    }

    // Pause/Play control for the play button
    fun setPlayButton(isPlaying: Boolean) {
        val playbutton = rootView?.findViewById<ImageButton>(R.id.play_button)
        val clockface = rootView?.findViewById<TextView>(R.id.clock_face)
        var playing = isPlaying
        playbutton?.setOnClickListener {
            if (!playing) {
                playbutton.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
                countDown = CountDown(clockface, time.toLong() * 1000, 1000, this::nextInterval)
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

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//    }

    /**
     * Inner class to track remaining time
     */
    class CountDown(val clockface: TextView?, millisInFuture: Long, countDownInterval: Long, val nextInterval: () -> Boolean) : CountDownTimer(millisInFuture, countDownInterval) {
        var secondsRemaining = millisInFuture.toInt() / 1000

        override fun onTick(millisUntilFinished: Long) {
            val min = millisUntilFinished / 60000
            val sec = millisUntilFinished / 1000 % 60
            clockface?.text = String.format("%02d:%02d", min, sec)
            secondsRemaining--
        }

        override fun onFinish() {
            //Beep
            val beeper = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            RingtoneManager.getRingtone(clockface?.context, beeper).play()

            //Start the next interval
            nextInterval()
        }
    }

}
