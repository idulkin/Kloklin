package com.idulkin.kloklin.fragments

import android.arch.lifecycle.Observer
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.idulkin.kloklin.KloklinActivity
import com.idulkin.kloklin.R
import com.idulkin.kloklin.models.ClockViewModel
import com.idulkin.kloklin.objects.Interval
import com.idulkin.kloklin.objects.Program
import kotlinx.android.synthetic.main.fragment_clock.*

/**
 * The clockface of the interval timer
 * Observes a ClockViewModel and updates the UI
 */
class ClockFragment : Fragment() {

    val model: ClockViewModel by lazy {
        ClockViewModel.create(activity as KloklinActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Observe LiveData from the model
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
/*
        //Set the beep sound. Create a new shared pref if there isn't one
        val beep = sharedPrefs.getString("pref_beep", "R.raw.drip")
        model.mediaPlayer = MediaPlayer.create(context, Uri.parse(beep))
        */
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_clock, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: Do this at the resource level
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
            model.onSkipForwardClicked()
        }

        // Skip Back button
        skip_back_button.setOnClickListener {
            model.onSkipBackClicked()
        }
    }
    /*
     * When this fragment becomes visible, check if the activity is starting a new program
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint && activity != null) {
            val activity = activity as KloklinActivity
            if (activity.model.playingProgram != model.program) {
                model.newProgram(activity.model.playingProgram)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        //Save the current program as a shared preference
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val json = Gson().toJson(model.program)
        editor.putString("CurrentProgram", json)
        editor.apply()
    }

    /**
     * When at the end of the program, set the screen to restart
     */
    private fun finishProgram() {
        skip_forward_button.visibility = View.GONE
        skip_back_button.visibility = View.GONE
        play_button.setImageDrawable(resources.getDrawable(R.drawable.big_restart_button, resources.newTheme()))
    }
}
