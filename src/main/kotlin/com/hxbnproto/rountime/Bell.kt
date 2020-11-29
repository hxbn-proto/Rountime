package com.hxbnproto.rountime

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

class Bell : Thread() {
    private val player: MediaPlayer = MediaPlayer(Media(File("ring.mp3").toURI().toURL().toString()))

    fun ring() {
        Bell().start()
    }

    override fun run() {
        super.run()
        player.play()
    }
}
