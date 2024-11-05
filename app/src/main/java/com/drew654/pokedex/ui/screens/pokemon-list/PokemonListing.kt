package com.drew654.pokedex.ui.screens.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PokemonListing(
    id: Int,
    name: String
) {
    val context = LocalContext.current
    val inputStream = context.assets.open("pokemon/${id}.json")
    val json = Json { ignoreUnknownKeys = true }
    val pokemon = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
    val color = pokemon.jsonObject["color"]?.jsonPrimitive?.content
    val colorObject = when (color) {
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

    Row(
        modifier = Modifier
            .background(colorObject)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "#${id}",
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
        Text(
            text = name.toString(),
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        AsyncImage(
            model = "file:///android_asset/sprites/${id}.png",
            contentDescription = name.toString(),
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
