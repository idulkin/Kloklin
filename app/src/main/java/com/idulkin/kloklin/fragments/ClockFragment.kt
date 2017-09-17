package com.idulkin.kloklin.fragments

import kotlinx.android.synthetic.main.fragment_clock.*
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.idulkin.kloklin.R
import com.idulkin.kloklin.data.ClockViewModel
import com.idulkin.kloklin.objects.Program

/**
 * The face of the interval timer
 */
class ClockFragment : Fragment() {

//        var model:  ClockViewModel? = null
    var model = ClockViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(ClockViewModel::class.java)

        //Restore last program from shared prefs
        val json = activity.getSharedPreferences("", 0).getString("CurrentProgram", "")
        val program = Gson().fromJson(json, Program::class.java)

        model.init(program)

        //Set LiveData observables
        model.time.observe(this, Observer<Long> { time ->
            if (time != null) {
                clock_face.text = String.format("%02d:%02d", time / 60, time % 60)
            }
        })

        model.title.observe(this, Observer<String> { title ->
            clock_title.text = title
        })

        model.description.observe(this, Observer<String> { text ->
            if (text != null) {
                action_text.text = text

                if (text == "FINISH") {
                    //Reached the end of this program
                    finishProgram()
                }
            }
        })

        model.playing.observe(this, Observer<Boolean> { playing ->
            if (playing != null) {
                if (playing) {
                    play_button.setImageDrawable(resources.getDrawable(R.drawable.big_pause_button, resources.newTheme()))
                } else {
                    play_button.setImageDrawable(resources.getDrawable(R.drawable.big_play_button, resources.newTheme()))
                }
            }
        })

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
            clock_title.text = model.program.name
            bottom_margin.visibility = View.VISIBLE
        } else {
            clock_title.visibility = View.GONE
            bottom_margin.visibility = View.GONE
        }

        // Pause/Play button
        play_button.setOnClickListener {
            model.onPlayButtonClicked()
            skip_forward_button.visibility = View.VISIBLE
            skip_back_button.visibility = View.VISIBLE
        }

        // Skip Forward button
        skip_forward_button.setOnClickListener {
            model.position++
            model.startInterval()
        }

        // Skip Back button
        skip_back_button.setOnClickListener {
            model.startInterval()
        }

        skip_back_button.setOnLongClickListener {
            model.position--
            model.startInterval()
            true //Long click listener wants a boolean return
        }
    }

    /**
     * When at the end of the program, set the screen to restart
     */
    fun finishProgram() {
        skip_forward_button.visibility = View.GONE
        skip_back_button.visibility = View.GONE
        play_button.setImageDrawable(resources.getDrawable(R.drawable.big_restart_button, resources.newTheme()))
    }
}
