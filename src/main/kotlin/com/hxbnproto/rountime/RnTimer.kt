package com.hxbnproto.rountime

import java.util.*

class RnTimer {

    var rounds: ArrayList<RnRound> = ArrayList()
    var infiniteLoops: Boolean = false
    var loopCount: Int = 0
    var active = false
        private set

    private var running = false
    private var roundTimerThread: Thread? = null
    private var activeRoundIndex = 0
    private var loopIndex: Int = 0

    fun startTimer() {

        if (!active && !running) {
            active = true

            roundTimerThread = Thread {

                running = true

                loop@ while (active && rounds.size > 0) {

                    do {
                        val activeRound: RnRound = rounds[maxOf(activeRoundIndex, 0)]

                        try {
                            activeRound.go()
                        } catch (stoppingException: InterruptedException) {
                            break@loop
                        }

                    } while (++activeRoundIndex < rounds.size)

                    if (!infiniteLoops && (loopIndex++ >= loopCount)) {
                        break
                    }

                    activeRoundIndex = 0
                }

                active = false
                running = false
                loopIndex = 0
            }

            // Start countdown
            roundTimerThread?.isDaemon = true
            roundTimerThread?.start()
        }
    }

    fun stopTimer() {
        stopActiveRound()
        activeRoundIndex = 0
    }

    fun stopActiveRound() {
        active = false
        roundTimerThread?.interrupt()

        while (roundTimerThread?.state != Thread.State.TERMINATED) {
            Thread.sleep(200)
        }
    }

    fun runFrom(selectedRound: RnRound) {
        activeRoundIndex = rounds.indexOf(selectedRound)
    }

    fun restartCurrentRound() {
        val newIndex = activeRoundIndex

        stopActiveRound()
        if (newIndex < rounds.size) {
            activeRoundIndex = newIndex
            startTimer()
        }
    }

    fun skip() {
        var newIndex = activeRoundIndex + 1
        if (newIndex >= rounds.size) {
            newIndex = 0
        }

        stopActiveRound()
        if (newIndex < rounds.size) {
            activeRoundIndex = newIndex
            startTimer()
        }
    }

    fun skipIfRunning(round: RnRound) {
        if (running && rounds.indexOf(round) == activeRoundIndex) {
            skip()
        }
    }
}
