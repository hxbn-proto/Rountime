package com.hxbnproto.rountime

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object Bell {
    private val ringSound: Media =
        Media(javaClass.classLoader.getResource(Constants.RING_SOUND_FILENAME).toURI().toURL().toString())

    // TODO(later) check: probably lags on first run
    fun ring() {
        GlobalScope.launch {
            val player = MediaPlayer(ringSound)
            player.play()
            delay(player.media.duration.toMillis().toLong())
            player.stop()
        }
    }

    private object Constants {
        const val RING_SOUND_FILENAME: String = "ring.mp3"
    }
}
