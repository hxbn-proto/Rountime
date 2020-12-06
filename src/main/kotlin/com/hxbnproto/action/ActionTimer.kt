package com.hxbnproto.action

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration

class ActionTimer(duration: Duration = Duration.ZERO) {

    var duration: Duration = duration
        set(newVal) {
            field = newVal

            if (runningDuration ?: Duration.ZERO > duration) {
                runningDuration = duration
            }
        }
    var onStart: (() -> Unit)? = null
    var onTick: (() -> Unit)? = null
    var onPause: (() -> Unit)? = null
    var onDeactivated: (() -> Unit)? = null
    var onStop: (() -> Unit)? = null
    var onComplete: (() -> Unit)? = null
    var onEnd: (() -> Unit)? = null

    private var timerJob: Job? = null
    var runningDuration: Duration? = null
        private set

    fun start() = runBlocking {

        if (timerJob == null || !timerJob!!.isActive) {
            if (runningDuration == null) {
                reset()
            }

            timerJob = launch {
                onStart?.invoke()

                while (runningDuration?.isNegative == false) {
                    onTick?.invoke()

                    delay(1000)
                    runningDuration = runningDuration?.minusSeconds(1)
                }

                // Reset duration
                runningDuration = duration

                onComplete?.invoke()
                onDeactivated?.invoke()
                onEnd?.invoke()
            }

            timerJob?.join()
        }
    }

    fun pause() = runBlocking {
        timerJob?.cancel()
        timerJob?.join()

        onPause?.invoke()
        onDeactivated?.invoke()
    }

    fun stop() = runBlocking {
        timerJob?.cancel()
        timerJob?.join()

        // Reset duration
        runningDuration = duration

        onStop?.invoke()
        onDeactivated?.invoke()
        onEnd?.invoke()
    }

    fun reset() {
        runningDuration = duration
    }
}