package com.drew654.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.drew654.pokedex.models.Filter
import com.drew654.pokedex.ui.components.DropdownMenu
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun FiltersScreen() {
    val context = LocalContext.current
    val json = Json { ignoreUnknownKeys = true }
    val inputStream = context.assets.open("filters.json")
    val filtersJson = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
    val pokedexesJson = filtersJson.jsonObject["pokedexes"]?.jsonObject

    val pokedexFilters: List<Filter> = pokedexesJson?.keys?.map {
        val pokedex = pokedexesJson[it]?.jsonObject
        val name = pokedex?.get("name")?.jsonPrimitive?.content
        val ids: List<Int> =
            pokedex?.get("pokemon")?.jsonArray?.map { it.jsonPrimitive.int } as List<Int>
        Filter(name!!, ids.toSet())
    } ?: emptyList()

    val selectedPokedex = remember { mutableStateOf(pokedexFilters[0].name) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(1) {
                DropdownMenu(
                    selectedValue = selectedPokedex,
                    label = "Pokédex",
                    options = pokedexFilters.map { it.name },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
