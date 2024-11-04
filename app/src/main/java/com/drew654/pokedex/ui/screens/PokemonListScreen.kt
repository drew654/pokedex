package com.drew654.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PokemonListScreen() {
    val context = LocalContext.current
    val inputStream = context.assets.open("pokemon/names.json")
    val json = Json { ignoreUnknownKeys = true }
    val names = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
        .jsonArray
        .map { it.jsonPrimitive.content }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            LazyColumn {
                items(names) { name ->
                    Text(text = name.toString())
                }
            }
        }
    }
}
