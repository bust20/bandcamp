package com.example.bandcamp

import android.media.MediaPlayer

object MediaPlayerSingleton {

    var mediaPlayer: MediaPlayer? = null

    fun playTrack(activity: Album2016Activity, trackResId: Int) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(activity, trackResId)
        mediaPlayer?.start()
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}