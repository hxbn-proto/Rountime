package com.hxbnproto.rountime.gui

import com.hxbnproto.rountime.RnRound
import com.hxbnproto.rountime.calculateContrastColor
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Text
import tornadofx.c
import tornadofx.multi
import tornadofx.runLater
import tornadofx.style

class TimerDisplay(
    private val backgroundColorElement: Pane,
    private val timeValueElement: Text,
    private val screenTextElement: Text
    /*TODO(soon): spinner
    val percentageSpinner: Spinner<>*/
) {

    init {
        reset(true)
    }

    fun reset(timeTextDeepReset: Boolean = false) {

        updateDisplayColor(c(TIMER_DEFAULT_BG_COLOR))
        updateScreenText(TIMER_DEFAULT_ROUND_TITLE)

        if (timeTextDeepReset) {
            wipeTime()
        } else {
            resetTime()
        }
    }

    private fun updateDisplayColor(newColor: Color) {
        backgroundColorElement.style {
            backgroundColor = multi(Paint.valueOf(newColor.toString()))

        }
        val contrastColor = calculateContrastColor(newColor)

        timeValueElement.style {
            fill = contrastColor
            // TODO(later): research colors and make stroke
            /*strokeWidth = 1.px
            stroke = contrastColor.invert()*/
        }
        screenTextElement.style {
            fill = contrastColor
        }
    }

    private fun updateTime(time: String) {
        runLater {
            timeValueElement.text = time
        }
    }

    private fun resetTime() {
        runLater {
            timeValueElement.text = TIMER_DEFAULT_COUNTER_ZERO_TIME
        }
    }

    private fun wipeTime() {
        runLater { timeValueElement.text = TIMER_DEFAULT_COUNTER_TEXT }
    }

    private fun updateScreenText(text: String) {
        runLater { screenTextElement.text = text }
    }

    fun show(round: RnRound) {
        updateDisplayColor(round.color)
        updateScreenText(round.description)
        updateTime(round.getFormattedTickingDuration())
    }

    companion object {
        private const val TIMER_DEFAULT_COUNTER_ZERO_TIME = "00:00:00"
        private const val TIMER_DEFAULT_COUNTER_TEXT = "--:--:--"
        private const val TIMER_DEFAULT_ROUND_TITLE = "No tasks"
        private const val TIMER_DEFAULT_BG_COLOR = "#fff"
    }
}
