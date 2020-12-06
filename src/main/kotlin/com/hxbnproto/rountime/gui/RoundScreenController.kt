package com.hxbnproto.rountime.gui

import com.hxbnproto.rountime.GlobalTimer
import com.hxbnproto.rountime.RnRound
import com.hxbnproto.rountime.TimerHandler
import tornadofx.Controller
import java.time.Duration

class RoundScreenController : Controller() {
    private val roundScreen: RoundScreen by inject()
    private val timerHandler = TimerHandler()
    private val globalTimer = GlobalTimer()

    fun init() {
        globalTimer.onTick = roundScreen::updateGlobalDuration
        globalTimer.onFinish = { roundScreen.updateGlobalDuration(Duration.ZERO) }
    }

    fun startTimer() {

        if (!timerHandler.isActive && timerHandler.activeRound == null) {
            roundScreen.getSelectedRound()?.let { timerHandler.nextRun(it) }
        }
        timerHandler.startTimer()

        globalTimer.startTimer()
    }

    fun show(round: RnRound?) {
        if (round != null) {
            if (!timerHandler.isActive && round == roundScreen.getSelectedRound() || round == timerHandler.activeRound) {
                roundScreen.timerDisplay.show(round)
            }
        } else {
            roundScreen.timerDisplay.reset()
        }
    }

    fun stopTimer() {
        roundScreen.timerDisplay.reset()
        timerHandler.stopTimer()
        globalTimer.stopTimer()
    }

    fun addRound() {

        val round = RnRound()
        round.description = "Round #${timerHandler.roundsCount()}"
        round.onChanged = this::show
        round.onTick = this::show

        val roundElement = roundScreen.constructElementForRound(round)

        timerHandler.addRound(round)
        roundScreen.addRoundElement(roundElement)
    }

    fun duplicateRound(round: RnRound) {

        val duplicated = round.clone()
        val insertingIndex: Int = timerHandler.indexOfRound(round) + 1

        duplicated.description = "Round #${insertingIndex}"

        val roundElement = roundScreen.constructElementForRound(duplicated)

        roundScreen.insertRoundElement(insertingIndex, roundElement)

        timerHandler.addRoundOnIndex(insertingIndex, duplicated)
    }

    fun deleteRound(round: RnRound) {

        roundScreen.deleteRoundElement(round.element!!)

        timerHandler.skipIfRunning(round)
        timerHandler.removeRound(round)

        show(timerHandler.activeRound)

    }

    fun deleteAllRounds() {
        stopTimer()
        roundScreen.deleteAllRoundElements()

        timerHandler.removeAll()
        roundScreen.timerDisplay.reset()
    }

    fun restartCurrentRound() {
        timerHandler.restartCurrentRound()
    }

    fun setInfiniteLoop(isInfiniteLoop: Boolean) {
        timerHandler.infiniteLoops = isInfiniteLoop
    }

    fun skipRound() {
        timerHandler.skip()
    }

    fun pauseTimer() {
        timerHandler.pause()
    }
}