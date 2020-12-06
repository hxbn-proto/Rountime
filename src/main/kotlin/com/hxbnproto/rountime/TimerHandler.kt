package com.hxbnproto.rountime

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class TimerHandler {

    var infiniteLoops: Boolean = false
    var loopCount: Int = 0
    var paused = false
        private set
    var isActive = false
        private set
        get() = timerHandlerJob?.isActive ?: false || paused

    var activeRound: RnRound? = null
        private set

    private var rounds: ArrayList<RnRound> = ArrayList()
    private var timerHandlerJob: Job? = null
    private var activeRoundIndex = 0
    private var loopIndex: Int = 0

    fun startTimer() {

        if (timerHandlerJob?.isActive != true) {

            timerHandlerJob = GlobalScope.launch {
                paused = false
                mainLoop@ while (rounds.size > 0) {

                    while (activeRoundIndex < rounds.size) {
                        activeRound = rounds[Integer.max(activeRoundIndex++, 0)]

                        val successfulRun: Boolean = activeRound?.go() ?: false
                        if (!successfulRun) {
                            break@mainLoop
                        }
                    }

                    activeRoundIndex = 0

                    if (!infiniteLoops && (loopIndex++ >= loopCount)) {
                        break
                    }
                }

                loopIndex = 0
            }
        }
    }

    fun addRound(round: RnRound) {
        rounds.add(round)
    }

    fun addRoundOnIndex(index: Int, round: RnRound) {
        rounds.add(index, round)
    }

    fun removeRound(round: RnRound) {
        rounds.remove(round)
        if (rounds.size == 0) {
            activeRound = null
        }
    }

    fun removeAll() {
        rounds.clear()
        activeRound = null
    }

    fun roundsCount(): Int {
        return rounds.size
    }

    fun indexOfRound(round: RnRound): Int {
        return rounds.indexOf(round)
    }

    fun stopTimer() = runBlocking {
        paused = false

        stopRound()

        activeRound = null
        activeRoundIndex = 0
    }

    fun stopRound() = runBlocking {
        activeRound?.stop()

        timerHandlerJob?.cancel()
        timerHandlerJob?.join()

        activeRound?.stop()
    }

    fun nextRun(selectedRound: RnRound) {
        activeRoundIndex = rounds.indexOf(selectedRound)
    }

    fun restartCurrentRound() {
        if (activeRound != null) {
            activeRoundIndex--

            stopRound()
            startTimer()
        }
    }

    fun skip() {
        stopRound()

        if (activeRoundIndex >= rounds.size) {
            activeRoundIndex = 0
        }
        startTimer()
    }

    fun skipIfRunning(round: RnRound) {
        if (activeRound == round) {
            skip()
        }
    }

    fun pause() {
        paused = true

        activeRoundIndex--
        activeRound?.pause()
    }
}
