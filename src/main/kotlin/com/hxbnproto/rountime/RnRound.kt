package com.hxbnproto.rountime

import com.hxbnproto.action.ActionTimer
import javafx.scene.Node
import javafx.scene.paint.Color
import java.time.Duration

class RnRound {

    private val actionTimer = ActionTimer()
    private val bell = Bell
    private var executionResult: Boolean = false

    var element: Node? = null

    var alarmIsOn: Boolean = true
        set(newVal) {
            field = newVal
            onChanged?.invoke(this)
        }
    var description: String = ""
        set(newVal) {
            field = newVal
            onChanged?.invoke(this)
        }
    var color: Color = Color.color(Math.random(), Math.random(), Math.random())
        set(newVal) {
            field = newVal
            onChanged?.invoke(this)
        }

    var onChanged: ((round: RnRound) -> Unit)? = null

    var onTick: ((round: RnRound) -> Unit)? = null
        set(newVal) {
            field = newVal
            actionTimer.onTick = { onTick?.invoke(this) }
        }
    var onStart: ((round: RnRound) -> Unit)? = null
        set(newVal) {
            field = newVal

            actionTimer.onStart = {
                executionResult = false
                onStart?.invoke(this)
            }
        }
    var onFinish: ((round: RnRound) -> Unit)? = null
        set(newVal) {
            field = newVal

            actionTimer.onComplete = {
                executionResult = true
                if (alarmIsOn) {
                    bell.ring()
                }
                onFinish?.invoke(this)
            }
        }

    var duration: Duration
        set(newVal) {
            actionTimer.duration = newVal
            onChanged?.invoke(this)
        }
        get() = actionTimer.duration

    //private var tickingDuration: Duration? = null

    fun go(): Boolean {
        actionTimer.start()
        return executionResult
    }

    fun pause() {
        actionTimer.pause()
    }

    fun stop() {
        actionTimer.stop()
    }

    private fun addSeconds(seconds: Int) {
        duration.plusSeconds(seconds.toLong())
    }

    private fun addMinutes(minutes: Int) {
        duration.plusMinutes(minutes.toLong())
    }

    private fun addHours(hours: Int) {
        duration.plusHours(hours.toLong())
    }

    fun getFormattedDuration(): String {
        return formatDuration(duration)
    }

    fun getFormattedTickingDuration(): String {
        return formatDuration(actionTimer.runningDuration ?: duration)
    }

    fun resetDuration() {
        duration = Duration.ZERO
    }

    fun setDuration(seconds: Int, minutes: Int, hours: Int) {
        resetDuration()

        addHours(hours)
        addMinutes(minutes)
        addSeconds(seconds)
    }

    fun clone(): RnRound {
        val copied = RnRound()

        copied.color = color
        copied.alarmIsOn = alarmIsOn
        copied.duration = duration
        copied.onChanged = onChanged
        copied.onTick = onTick

        return copied
    }
}
