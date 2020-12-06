package com.hxbnproto.rountime

import javafx.scene.paint.Color
import tornadofx.c
import java.time.Duration


fun formatDuration(duration: Duration): String {
    return String.format(
        "%02d:%02d:%02d",
        duration.seconds / 3600,
        (duration.seconds % 3600) / 60,
        (duration.seconds % 60)
    )
}

fun parseDuration(text: String): Duration {
    var resultDuration = Duration.ZERO

    val splittedText: List<String> = text.split(":").filter { str -> !str.isBlank() }

    resultDuration = when (splittedText.size) {
        1 -> resultDuration.plusSeconds(splittedText[0].toLong())
        2 -> resultDuration.plusMinutes(splittedText[0].toLong()).plusSeconds(splittedText[1].toLong())
        3 -> resultDuration.plusHours(splittedText[0].toLong()).plusMinutes(splittedText[1].toLong()).plusSeconds(
            splittedText[2].toLong()
        )
        else -> resultDuration
    }

    return resultDuration
}

fun calculateContrastColor(backgroundColor: Color): Color {
    return if (backgroundColor.brightness > 0.6) c("#252525") else c("#e1e2e2")
}

