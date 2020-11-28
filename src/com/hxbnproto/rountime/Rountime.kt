package com.hxbnproto.rountime

import com.hxbnproto.rountime.gui.RoundScreen
import com.hxbnproto.rountime.gui.RoundScreenController
import com.hxbnproto.rountime.gui.Styles
import com.sun.jndi.toolkit.url.Uri
import javafx.application.Platform
import javafx.stage.Stage
import tornadofx.*
import java.io.File

class Rountime : App(RoundScreen::class, Styles::class) {
    private val roundScreenController: RoundScreenController by inject()

    override fun start(stage: Stage) {
        Platform.setImplicitExit(true)

        super.start(stage)
        roundScreenController.init()
    }
}

fun main(args: Array<String>) {
    launch<Rountime>(args)
}