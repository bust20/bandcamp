package com.example.bandcamp

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Album2016Activity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var currentTrackResId: Int? = null
    private var lastButton: ImageView? = null

    private fun playTrack(trackResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, trackResId)
        mediaPlayer?.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.album_2016)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttons = listOf(
            R.id.playParis2023 to R.raw.kiddkeo_paris_2023,
            R.id.playSober to R.raw.kiddkeo_sober,
            R.id.playNinoDelHood to R.raw.kiddkeo_ninodelhood,
            R.id.playLosMasPegaos20 to R.raw.kiddkeo_losmaspegaos20
        )

        for ((buttonId, trackResId) in buttons) {
            val button = findViewById<ImageView>(buttonId)
            button.setOnClickListener { toggleTrack(button, trackResId) }
        }
    }

    private fun toggleTrack(button: ImageView, trackResId: Int) {

        if (isPlaying && currentTrackResId == trackResId) {

            MediaPlayerSingleton.mediaPlayer?.pause()
            button.setImageResource(R.drawable.play)
            isPlaying = false
        } else {
            lastButton?.setImageResource(R.drawable.play)

            MediaPlayerSingleton.stopMusic()

            MediaPlayerSingleton.playTrack(this, trackResId)
            button.setImageResource(R.drawable.pause)
            isPlaying = true
            currentTrackResId = trackResId
            lastButton = button
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerSingleton.stopMusic()
    }
}