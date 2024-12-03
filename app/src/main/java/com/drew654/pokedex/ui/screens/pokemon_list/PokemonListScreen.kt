package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.drew654.pokedex.models.FilterViewModel
import com.drew654.pokedex.models.Pokemon
import com.drew654.pokedex.models.Screen
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PokemonListScreen(navController: NavController, filterViewModel: FilterViewModel) {
    val context = LocalContext.current
    val files = context.assets.list("pokemon")
    val regionFilter = filterViewModel.regionFilter.collectAsState()
    val type1Filter = filterViewModel.type1Filter.collectAsState()
    val type2Filter = filterViewModel.type2Filter.collectAsState()

    val pokemonList = files?.map { file ->
        if (file == "names.json") {
            return@map null
        }
        val inputStream = context.assets.open("pokemon/$file")
        val json = Json { ignoreUnknownKeys = true }
        val pokemonJson =
            json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
        val id = pokemonJson.jsonObject["id"]?.jsonPrimitive?.intOrNull
        val name = pokemonJson.jsonObject["name"]?.jsonPrimitive?.content
        val color = pokemonJson.jsonObject["color"]?.jsonPrimitive?.content
        val types =
            pokemonJson.jsonObject["types"]?.jsonArray?.map { it.jsonPrimitive.content }?.toList()!!
        val originalRegion = pokemonJson.jsonObject["original_region"]?.jsonPrimitive?.content!!

        if (id != null && name != null && color != null) {
            Pokemon(id, name, color, types, originalRegion)
        } else {
            null
        }
    }?.filterNotNull()?.sortedBy { it.id }
        ?.filter { pokemon ->
            regionFilter.value == null || pokemon.originalRegion == regionFilter.value
        }
        ?.filter { pokemon ->
            type1Filter.value == null || pokemon.types.contains(type1Filter.value)
        }
        ?.filter { pokemon ->
            type2Filter.value == null || pokemon.types.contains(type2Filter.value)
        }
        ?: emptyList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (regionFilter.value != null) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(0.5f)
                    ) {
                        FilterTag(type = regionFilter.value!!)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.weight(1f)
                ) {
                    if (type1Filter.value != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(1f)
                        ) {
                            FilterTag(type = type1Filter.value!!)
                        }
                    }
                    if (type2Filter.value != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(1f)
                        ) {
                            FilterTag(type = type2Filter.value!!)
                        }
                    }
                }
            }

            LazyColumn {
                items(pokemonList) { pokemon ->
                    PokemonListing(
                        pokemon = pokemon,
                        navController = navController
                    )
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Screen.Filters.route) },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = com.drew654.pokedex.R.drawable.baseline_filter_list_24),
                contentDescription = "Filter"
            )
        }
    }
}
