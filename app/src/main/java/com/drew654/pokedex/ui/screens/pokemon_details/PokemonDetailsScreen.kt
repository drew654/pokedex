package com.drew654.pokedex.ui.screens.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.drew654.pokedex.models.Pokemon
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PokemonDetailsScreen(id: Int) {
    val context = LocalContext.current
    val inputStream = context.assets.open("pokemon/${id}.json")
    val json = Json { ignoreUnknownKeys = true }
    val pokemon = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
    val name = pokemon.jsonObject["name"]?.jsonPrimitive?.content
    val color = pokemon.jsonObject["color"]?.jsonPrimitive?.content
    val types = pokemon.jsonObject["types"]?.jsonArray?.map { it.jsonPrimitive.content }?.toList()
    val abilities = pokemon.jsonObject["abilities"]?.jsonArray
    val abilityNames = abilities?.map { ability ->
        ability.jsonObject["name"]?.jsonPrimitive?.content
    }
    val backgroundColor = Pokemon().GetBackgroundColor(color.toString())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = name.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0x88000000),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "#${id}",
                            color = Color(0x88000000),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }
                    Row {
                        for (type in types!!) {
                            Text(
                                text = type.toString(),
                                color = Color(0x88000000),
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
                                    .padding(all = 4.dp)
                            )
                        }
                    }
                }
                AsyncImage(
                    model = "file:///android_asset/sprites/${id}.png",
                    contentDescription = name.toString(),
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Abilities",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0x88000000)
                )
            }
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
            ) {
                Column {
                    for (ability in abilityNames!!) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                text = ability.toString(),
                                fontSize = 24.sp,
                                color = Color(0x88000000),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
