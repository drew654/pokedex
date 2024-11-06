package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.drew654.pokedex.models.Pokemon
import com.drew654.pokedex.models.Screen
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PokemonListing(
    id: Int,
    name: String,
    navController: NavController
) {
    val context = LocalContext.current
    val inputStream = context.assets.open("pokemon/${id}.json")
    val json = Json { ignoreUnknownKeys = true }
    val pokemon = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
    val color = pokemon.jsonObject["color"]?.jsonPrimitive?.content
    val types = pokemon.jsonObject["types"]?.jsonArray?.map { it.jsonPrimitive.content }?.toList()
    val backgroundColor = Pokemon().GetBackgroundColor(color.toString())

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                navController.navigate("${Screen.PokemonDetails.route}/${id}")
            }
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column {
                Row {
                    Text(
                        text = "#${id}",
                        color = Color(0x88000000),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                    Text(
                        text = name.toString(),
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
}
