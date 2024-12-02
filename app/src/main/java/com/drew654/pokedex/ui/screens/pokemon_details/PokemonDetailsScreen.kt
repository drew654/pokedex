package com.drew654.pokedex.ui.screens.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.drew654.pokedex.models.Pokemon
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PokemonDetailsScreen(id: Int, navController: NavController) {
    val context = LocalContext.current
    val inputStream = context.assets.open("pokemon/${id}.json")
    val json = Json { ignoreUnknownKeys = true }
    val pokemonJson = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
    val name = pokemonJson.jsonObject["name"]?.jsonPrimitive?.content
    val color = pokemonJson.jsonObject["color"]?.jsonPrimitive?.content
    val types =
        pokemonJson.jsonObject["types"]?.jsonArray?.map { it.jsonPrimitive.content }?.toList()!!
    val abilities = pokemonJson.jsonObject["abilities"]?.jsonArray
    val (hiddenAbilityNames, abilityNames) = abilities
        ?.partition { ability ->
            ability.jsonObject["is_hidden"]?.jsonPrimitive?.booleanOrNull == true
        }
        ?.let { (hidden, nonHidden) ->
            hidden.mapNotNull { it.jsonObject["name"]?.jsonPrimitive?.content } to
                    nonHidden.mapNotNull { it.jsonObject["name"]?.jsonPrimitive?.content }
        }
        ?: (emptyList<String>() to emptyList<String>())
    val pokemon = Pokemon(id, name.toString(), color.toString(), types)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pokemon.color)
    ) {
        LazyColumn {
            items(1) {
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
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                AbilitiesSection(abilityNames, hiddenAbilityNames)
                Spacer(modifier = Modifier.size(16.dp))
                EvolutionLine(
                    id = id,
                    evolutionData = pokemonJson.jsonObject["evolution_line"],
                    navController = navController
                )
            }
        }
    }
}
