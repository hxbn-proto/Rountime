package com.hxbnproto.rountime.gui

import com.hxbnproto.rountime.RnRound
import com.hxbnproto.rountime.TimeTextField
import com.hxbnproto.rountime.formatDuration
// import com.hxbnproto.rountime.gui.Styles.Companion.roundScreen
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXRadioButton
import com.jfoenix.controls.JFXToggleNode
import com.pepperonas.fxiconics.FxIconicsLabel
import com.pepperonas.fxiconics.gmd.FxFontGoogleMaterial
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.*
import javafx.scene.layout.BorderWidths
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import kfoenix.jfxbutton
import kfoenix.jfxcolorpicker
import kfoenix.jfxradiobutton
import tornadofx.*
import java.time.Duration

class RoundScreen : View("RounTime") {

    private val roundScreenController: RoundScreenController by inject()
    private val roundButtonGroup = ToggleGroup()
    lateinit var timerDisplay: TimerDisplay

    override val root = vbox {

//        todo:
//        addClass(roundScreen)

        style {
            padding = box(15.px)
            vgap = 7.px
            hgap = 10.px
        }

        stackpane {
            background =
                Background(BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY))
            border = Border(
                BorderStroke(
                    Paint.valueOf("black"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
                )
            )

            pane {
                id = "coloredBackground"
                opacity = 0.2
            }

            // Display
            vbox {
                alignment = Pos.BASELINE_CENTER
                padding = Insets(0.0, 0.0, 20.0, 0.0)

                label {
                    id = "timeCounter"
                    font = Font.font("arial", FontWeight.BOLD, 50.0)
                    spacing = 25.0
                }

                label {
                    id = "roundTitle"
                    font = Font.font("arial", FontWeight.BOLD, 24.0)
                    spacing = 15.0
                }

                hbox {
                    spacing = 10.0
                    alignment = Pos.BASELINE_CENTER

                    jfxbutton("Restart") {
                        prefWidth = 80.0
                        style {
                            buttonType = JFXButton.ButtonType.RAISED
                            textFill = Color.WHITE
                            background =
                                Background(
                                    BackgroundFill(
                                        Paint.valueOf("#5164ae"),
                                        CornerRadii.EMPTY,
                                        Insets.EMPTY
                                    )
                                )
                        }

                        action {
                            roundScreenController.restartCurrentRound()
                        }
                    }
                }
            }
        }

        hbox {
            alignment = Pos.BASELINE_CENTER
            spacing = 4.0
            HBox.setHgrow(this, Priority.ALWAYS)

            label("Totall time: ")
            label("00:00:00") {
                id = "totalTime"
                font = Font.font("arial", FontWeight.BOLD, 18.0)
            }

            val loopButton = JFXToggleNode()
            loopButton.style = "-fx-padding: 10px; -fx-background-color: #e3e3e3; -fx-background-radius: 50%;-jfx-toggle-color: deepskyblue;" // TODO: remake to kotlin-style
            loopButton.maxHeight = width
            loopButton.add(
                FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_loop)
                    .size(24)
                    .build()
            )
            loopButton.setOnAction {
                roundScreenController.setInfiniteLoop(loopButton.isSelected)
            }

            add(loopButton)

        }
        separator {}
        hbox {
            spacing = 10.0
            alignment = Pos.CENTER

            jfxbutton {
                style {
                    buttonType = JFXButton.ButtonType.FLAT
                    textFill = Color.WHITE
                    background =
                        Background(
                            BackgroundFill(
                                Paint.valueOf("#5164ae"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                            )
                        )
                }
                add(
                    FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_add)
                        .size(24)
                        .color(Color.WHITE)
                        .build()
                )
                action {
                    roundScreenController.addRound()
                    selectFirstRoundElement()
                }
            }
            jfxbutton {
                style {
                    buttonType = JFXButton.ButtonType.FLAT
                    textFill = Color.WHITE
                    background =
                        Background(
                            BackgroundFill(
                                Paint.valueOf("#5164ae"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                            )
                        )
                }
                add(
                    FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_remove)
                        .size(24)
                        .color(Color.WHITE)
                        .build()
                )

                action {
                    if (roundButtonGroup.selectedToggle != null) {
                        val dataArray = roundButtonGroup.selectedToggle.userData as Array<*>

                        roundScreenController.deleteRound(dataArray[0] as Node, dataArray[1] as RnRound)

                        if (getRoundElements()!!.size > 0) {
                            // Toggle radio button of first element if present
                            selectFirstRoundElement()
                            timerDisplay.roundRemoved(dataArray[1] as RnRound)
                        }
                    }
                }
            }
            separator(Orientation.VERTICAL)
            jfxbutton {
                style {
                    buttonType = JFXButton.ButtonType.FLAT
                    textFill = Color.WHITE
                    background =
                        Background(
                            BackgroundFill(
                                Paint.valueOf("#5164ae"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                            )
                        )
                }
                add(
                    FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_play_arrow)
                        .size(24)
                        .color(Color.WHITE)
                        .build()
                )
                action {
                    val data = roundButtonGroup.selectedToggle?.userData
                    val round = (data as? Array<*>)?.get(1)

                    roundScreenController.startTimer(round as? RnRound?)
                }
            }
            // TODO: pause timer
            /*jfxbutton {
                style {
                    buttonType = JFXButton.ButtonType.FLAT
                    textFill = Color.WHITE
                    background =
                        Background(
                            BackgroundFill(
                                Paint.valueOf("#5164ae"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                            )
                        )
                }
                add(
                    FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_pause)
                        .size(24)
                        .color(Color.WHITE)
                        .build()
                )
                action {
                    //roundScreenController.pauseTimer()
                }
            }*/
            jfxbutton {
                style {
                    buttonType = JFXButton.ButtonType.FLAT
                    textFill = Color.WHITE
                    background =
                        Background(
                            BackgroundFill(
                                Paint.valueOf("#5164ae"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                            )
                        )
                }
                add(
                    FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_stop)
                        .size(24)
                        .color(Color.WHITE)
                        .build()
                )
                action {
                    roundScreenController.stopTimer()
                }
            }
            jfxbutton {
                style {
                    buttonType = JFXButton.ButtonType.FLAT
                    textFill = Color.WHITE
                    background =
                        Background(
                            BackgroundFill(
                                Paint.valueOf("#5164ae"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                            )
                        )
                }
                add(
                    FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_skip_next)
                        .size(24)
                        .color(Color.WHITE)
                        .build()
                )
                action {
                    roundScreenController.skipRound()
                }
            }
        }

        scrollpane {
            VBox.setVgrow(this, Priority.ALWAYS)
            hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
            id = "blocker"

            vbox {
                togglegroup {
                    id = "rounds"
                }
            }

            minHeight = 150.0
        }

        jfxbutton("Clear") {
            maxWidth = Double.MAX_VALUE
            spacing = 10.0
            style {
                buttonType = JFXButton.ButtonType.FLAT
                textFill = Color.WHITE
                background =
                    Background(
                        BackgroundFill(
                            Paint.valueOf("#5164ae"),
                            CornerRadii.EMPTY,
                            Insets.EMPTY
                        )
                    )
            }
            HBox.setHgrow(this, Priority.ALWAYS)

            action {
                roundScreenController.deleteAllRounds()
            }
        }
    }

    init {
        primaryStage.minWidth = 410.0
        primaryStage.maxWidth = 410.0
        primaryStage.minHeight = 500.0

        timerDisplay = TimerDisplay(
            root.lookup("#coloredBackground") as Pane,
            root.lookup("#timeCounter") as Label,
            root.lookup("#roundTitle") as Label
            /* todo: spinner
            root.lookup("#roundTitle") as JFXSpinner*/
        )
        runLater { root.requestFocus() }
    }

    private fun getRoundElements(): MutableList<Node>? {
        return root.lookup("#rounds").getChildList()
    }

    private fun selectFirstRoundElement() {
        val radio = getRoundElements()!![0].getChildList()!!.filterIsInstance<JFXRadioButton>()[0]
        radio.isSelected = true
        radio.onAction.handle(ActionEvent())
    }

    fun deleteAllRoundElements() {
        getRoundElements()!!.clear()
    }

    fun addRoundElement(roundElement: Node) {
        getRoundElements()!!.add(roundElement)
    }

    fun insertRoundElement(index: Int, roundElement: Node) {
        getRoundElements()!!.add(index, roundElement)
    }

    fun deleteRoundElement(roundElement: Node) {
        getRoundElements()!!.remove(roundElement)
    }

    fun constructElementForRound(round: RnRound): Node {

        return hbox {
            prefWidth = 362.0
            spacing = 5.0
            padding = Insets(3.0, 5.0, 3.0, 5.0)

            style {
                font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14.0)
                border = Border(
                    BorderStroke(
                        Color.RED, Color.RED, c("bfbfbf"), Color.RED,
                        BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                        CornerRadii.EMPTY, BorderWidths(1.0), Insets.EMPTY
                    )
                )
                alignment = Pos.BASELINE_CENTER
                background = Background(
                    BackgroundFill(
                        Paint.valueOf(Color.WHITE.toString()),
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                    )
                )
            }

            jfxradiobutton {
                toggleGroup = roundButtonGroup
                minWidth = 120.0
                text = round.description
                userData = arrayOf(parent, round)
                onAction = EventHandler {
                    val dataRound = (userData as Array<*>)[1] as RnRound

                    roundScreenController.setActiveRound(dataRound)
                    roundScreenController.print(dataRound)
                }
            }

            val timeField = TimeTextField()
            timeField.maxWidth = 100.0
            timeField.userData = round
            timeField.textProperty()
                .addListener { _, _, _ ->
                    round.duration = timeField.duration
                }

            add(timeField)

            jfxcolorpicker {
                value = round.color
                maxWidth = 35.0
                maxHeight = 35.0
                style {
                    colorLabelVisible = false
                    border = Border(
                        BorderStroke(
                            Paint.valueOf("#bfbfbf"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            BorderWidths.FULL
                        )
                    )
                }
                onAction = EventHandler {
                    round.color = value
                }
            }

            val alarmButton = JFXToggleNode()
            alarmButton.paddingHorizontal = 8
            alarmButton.paddingVertical = 8
            alarmButton.add(
                FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_access_alarms)
                    .size(24)
                    .build()
            )
            alarmButton.isSelected = true
            alarmButton.setOnAction {
                round.alarmIsOn = alarmButton.isSelected
            }

            add(alarmButton)

            jfxbutton {
                style {
                    paddingHorizontal = 8
                    paddingVertical = 8
                }
                add(
                    FxIconicsLabel.Builder(FxFontGoogleMaterial.Icons.gmd_control_point_duplicate)
                        .size(24)
                        .build()
                )
                action { roundScreenController.duplicateRound(round) }
            }
        }
    }

    fun updateGlobalDuration(duration: Duration) {
        runLater { (root.lookup("#totalTime") as Label).text = formatDuration(duration) }
    }
}