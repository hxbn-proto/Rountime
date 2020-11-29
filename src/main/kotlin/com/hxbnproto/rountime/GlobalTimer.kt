package com.hxbnproto.rountime

import java.time.Duration

class GlobalTimer {
    private var duration: Duration = Duration.ZERO
    private var active: Boolean = false

    var onTick: ((duration: Duration) -> Unit)? = null
    var onStart: ((timer: GlobalTimer) -> Unit)? = null
    var onFinish: ((timer: GlobalTimer) -> Unit)? = null

    fun startTimer() {
        if (!active) {
            val timerThread = Thread {
                active = true
                onStart?.invoke(this)

                while (active) {
                    onTick?.invoke(duration)
                    Thread.sleep(1000)
                    duration = duration.plusSeconds(1)
                }

                onFinish?.invoke(this)
            }
            timerThread.isDaemon = true
            timerThread.start()
        }
    }

    fun stopTimer() {
        active = false
        duration = Duration.ZERO
    }
}