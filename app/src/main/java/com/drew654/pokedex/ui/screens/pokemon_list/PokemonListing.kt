package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    val pokemonJson = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
    val color = pokemonJson.jsonObject["color"]?.jsonPrimitive?.content
    val typesData =
        pokemonJson.jsonObject["types"]?.jsonArray?.map { it.jsonPrimitive.content }?.toList()
    val pokemon = Pokemon(id, name, color.toString())

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(pokemon.color)
            .clickable {
                navController.navigate("${Screen.PokemonDetails.route}/${id}")
            }
            .clip(RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Box {
            Column {
                Row {
                    Text(
                        text = "#${id}",
                        color = Color(0x88000000),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Text(
                        text = name.toString(),
                        color = Color(0x88000000),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                PokemonListingTypes(typesData = typesData)
            }
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = "file:///android_asset/sprites/${id}.png",
                contentDescription = name.toString(),
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}
