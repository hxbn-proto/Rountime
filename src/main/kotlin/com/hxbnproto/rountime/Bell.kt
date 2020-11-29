package com.hxbnproto.rountime

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Bell : Thread() {
    private val player: MediaPlayer =
        MediaPlayer(Media(javaClass.classLoader.getResource("ring.mp3").toURI().toURL().toString()))

    fun ring() {
        Bell().start()
    }

    override fun run() {
        super.run()
        player.play()
    }
}
