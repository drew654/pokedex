package com.drew654.pokedex.ui.screens.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.drew654.pokedex.models.Pokemon
import com.drew654.pokedex.ui.screens.pokemon_list.PokemonListing
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
    val originalRegion = pokemonJson.jsonObject["original_region"]?.jsonPrimitive?.content!!
    val pokemon = Pokemon(id, name.toString(), color.toString(), types, originalRegion)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pokemon.color)
    ) {
        LazyColumn {
            items(1) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
                ) {
                    PokemonListing(pokemon = pokemon)
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
