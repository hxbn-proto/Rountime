package com.hxbnproto.rountime.gui

import com.hxbnproto.rountime.GlobalTimer
import com.hxbnproto.rountime.RnRound
import com.hxbnproto.rountime.RnTimer
import javafx.scene.Node
import tornadofx.*
import java.time.Duration

class RoundScreenController : Controller() {
    private val roundScreen: RoundScreen by inject()
    private val roundTimer: RnTimer = RnTimer()
    private val globalTimer: GlobalTimer = GlobalTimer()

    fun init() {
        globalTimer.onTick = roundScreen::updateGlobalDuration
        globalTimer.onFinish = {roundScreen.updateGlobalDuration(Duration.ZERO)}
    }

    fun startTimer(selectedRound: RnRound?) {
        if (selectedRound != null) {
            roundTimer.runFrom(selectedRound)
        }
        roundTimer.startTimer()
        globalTimer.startTimer()
    }

    fun print(round: RnRound) {
        if (!roundTimer.active) {
            roundScreen.timerDisplay.print(round)
        }
    }

    fun stopTimer() {
        roundScreen.timerDisplay.reset()
        roundTimer.stopTimer()
        globalTimer.stopTimer()
    }

    fun addRound() {

        val round = RnRound()
        round.description = "Round #${roundTimer.rounds.size}"
        round.onChanged = roundScreen.timerDisplay::roundUpdated
        round.onStart = { (roundScreen.timerDisplay::setLocked)(true) }
        round.onTick = roundScreen.timerDisplay::roundTicked
        round.onFinish = { (roundScreen.timerDisplay::setLocked)(false) }

        val roundElement = roundScreen.constructElementForRound(round)

        roundTimer.rounds.add(round)
        roundScreen.addRoundElement(roundElement)

    }

    fun duplicateRound(round: RnRound) {

        val duplicated = round.clone()
        val insertingIndex: Int = roundTimer.rounds.indexOf(round) + 1

        duplicated.description = "Round #${insertingIndex}"

        val roundElement = roundScreen.constructElementForRound(duplicated)

        roundScreen.insertRoundElement(insertingIndex, roundElement)


        roundTimer.rounds.add(insertingIndex, duplicated)
    }

    fun deleteRound(roundElement: Node, round: RnRound) {

        roundScreen.deleteRoundElement(roundElement)

        roundTimer.skipIfRunning(round)
        roundTimer.rounds.remove(round)

        if (roundTimer.rounds.size == 0) {
            roundScreen.timerDisplay.reset()
        }
    }

    fun deleteAllRounds() {
        stopTimer()
        roundScreen.timerDisplay.reset()
        roundScreen.deleteAllRoundElements()

        roundTimer.rounds.clear()
    }

    fun restartCurrentRound() {
        roundTimer.restartCurrentRound()
    }

    fun setInfiniteLoop(isInfiniteLoop: Boolean) {
        roundTimer.infiniteLoops = isInfiniteLoop
    }

    fun setActiveRound(dataRound: RnRound) {
        roundScreen.timerDisplay.activeRound = dataRound
    }

    fun skipRound() {
        roundTimer.skip()
    }
}