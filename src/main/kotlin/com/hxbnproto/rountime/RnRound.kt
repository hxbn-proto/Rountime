package com.hxbnproto.rountime

import javafx.scene.paint.Color
import java.time.Duration

class RnRound {

    var onChanged: ((round: RnRound) -> Unit)? = null
    var onTick: ((round: RnRound) -> Unit)? = null
    var onStart: (() -> Unit)? = null
    var onFinish: (() -> Unit)? = null

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

    var duration: Duration = Duration.ZERO
        set(newVal) {
            field = newVal
            onChanged?.invoke(this)
        }
    private var bell: Bell = Bell()
    private var tickingDuration: Duration? = null

    fun go(): Boolean {

        tickingDuration = duration.abs()
        onStart?.invoke()
        while (tickingDuration!!.seconds >= 0) {

            onTick?.invoke(this)
            tickingDuration = tickingDuration!!.minusSeconds(1)

            try {
                Thread.sleep(1000)
            } catch (stoppingException: InterruptedException) {
                throw stoppingException
            }
        }
        if (alarmIsOn) {
            bell.ring()
        }
        onFinish?.invoke()
        return true
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
        return formatDuration(tickingDuration!!)
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
