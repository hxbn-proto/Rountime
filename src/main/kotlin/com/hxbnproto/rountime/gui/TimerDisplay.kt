package com.hxbnproto.rountime.gui

import com.hxbnproto.rountime.RnRound
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.c
import tornadofx.multi
import tornadofx.runLater
import tornadofx.style

class TimerDisplay(
    private val backgroundColorElement: Pane,
    private val timeValueElement: Label,
    private val screenTextElement: Label
    /*todo: spinner
    val percentageSpinner: Spinner<>*/
) {

    private var isLocked: Boolean = false
    var activeRound: RnRound? = null
        set(newRound) {
            if (!isLocked) {
                field = newRound

                print(newRound)
            }
        }

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

    private fun updateDisplayColor(color: Color) {
        backgroundColorElement.style {
            backgroundColor = multi(Paint.valueOf(color.toString()))
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

    fun print(round: RnRound) {
        updateDisplayColor(round.color)
        updateScreenText(round.description)
        updateTime(round.getFormattedDuration())
    }

    private fun printTick(round: RnRound) {
        updateDisplayColor(round.color)
        updateScreenText(round.description)
        updateTime(round.getFormattedTickingDuration())
    }

    fun roundUpdated(round: RnRound) {
        if (round == activeRound) {
            print(round)
        }
    }

    fun roundTicked(round: RnRound) {
        printTick(round)
    }

    fun roundRemoved(round: RnRound) {
        if (round == activeRound) {
            reset()
        }
    }

    fun setLocked(isLocked: Boolean) {
        this.isLocked = isLocked
    }

    companion object {
        private const val TIMER_DEFAULT_COUNTER_ZERO_TIME = "00:00:00"
        private const val TIMER_DEFAULT_COUNTER_TEXT = "--:--:--"
        private const val TIMER_DEFAULT_ROUND_TITLE = "No tasks"
        private const val TIMER_DEFAULT_BG_COLOR = "#fff"
    }
}
