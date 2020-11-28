package com.hxbnproto.rountime

import com.jfoenix.controls.JFXTextField
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import tornadofx.ChangeListener
import java.time.Duration

class TimeTextField : JFXTextField("00:00:00") {
    private var defaultText: String
    var duration: Duration = Duration.ZERO

    init {
        alignment = Pos.CENTER
        defaultText = this.text

        textProperty().addListener(ChangeListener<String> { _: ObservableValue<out String>?, _: String, newValue: String ->
            val timePattern = "^(\\d{1,99}:\\d{1,99}){1,2}\$|^\\d+\$"
            val digitsAndDotsPattern = "[^\\d:]"

            if (!newValue.matches(Regex(timePattern))) {
                text = newValue.replace(Regex(digitsAndDotsPattern), "")
            }
            duration = parseDuration(text)
        })

        focusedProperty().addListener { _, _, isFocused ->
            if (!isFocused) {
                text = formatDuration(duration)
            }
        }
    }
}