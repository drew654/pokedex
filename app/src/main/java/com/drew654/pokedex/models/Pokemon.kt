package com.drew654.pokedex.models

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class Pokemon() {
    @Composable
    fun GetBackgroundColor(color: String): Color {
        return when (color) {
            "green" -> Color(0xFF7AC74C)
            "red" -> Color(0xFFEE8130)
            "blue" -> Color(0xFF6390F0)
            "white" -> Color(0xFFFFFFFF)
            "brown" -> Color(0xFFA52A2A)
            "yellow" -> Color(0xFFFFD600)
            "purple" -> Color(0xFF9F5BBA)
            "pink" -> Color(0xFFF48FB1)
            "gray" -> Color(0xFF808080)
            "black" -> Color(0xFF000000)
            else -> MaterialTheme.colorScheme.background
        }
    }
}
