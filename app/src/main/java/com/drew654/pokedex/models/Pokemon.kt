package com.drew654.pokedex.models

import androidx.compose.ui.graphics.Color

class Pokemon(val id: Int, val name: String, colorString: String) {
    val color: Color = when (colorString) {
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
        else -> Color.Unspecified
    }
}
