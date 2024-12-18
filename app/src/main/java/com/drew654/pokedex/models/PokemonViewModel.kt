package com.drew654.pokedex.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    init {
        viewModelScope.launch {
            val pokemonCount = loadPokemonNamesFromJson().size
            for (i in 1..pokemonCount) {
                addPokemon(loadOnePokemonFromJson("$i.json"))
            }
        }
    }

    private suspend fun loadOnePokemonFromJson(fileName: String): Pokemon {
        val json = Json { ignoreUnknownKeys = true }
        return withContext(Dispatchers.IO) {
            val inputStream = getApplication<Application>().assets.open("pokemon/$fileName")
            val pokemonJson = json.parseToJsonElement(
                inputStream.bufferedReader().use { it.readText() }).jsonObject
            val id = pokemonJson["id"]?.jsonPrimitive?.intOrNull
            val name = pokemonJson["name"]?.jsonPrimitive?.content
            val color = pokemonJson["color"]?.jsonPrimitive?.content
            val types = pokemonJson["types"]?.jsonArray?.map { it.jsonPrimitive.content }
            val originalRegion = pokemonJson["original_region"]?.jsonPrimitive?.content

            if (id != null && name != null && color != null && types != null && originalRegion != null) {
                Pokemon(id, name, color, types, originalRegion)
            } else {
                throw IllegalStateException("Invalid Pokemon data in $fileName")
            }
        }
    }

    private suspend fun loadPokemonNamesFromJson(): List<String> {
        val json = Json { ignoreUnknownKeys = true }
        return withContext(Dispatchers.IO) {
            val inputStream = getApplication<Application>().assets.open("pokemon/names.json")
            val namesJson = json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() }).jsonArray
            namesJson.map { it.jsonPrimitive.content }
        }
    }

    private val _searchPokemonName = MutableStateFlow("")
    val searchPokemonName: StateFlow<String> = _searchPokemonName.asStateFlow()

    fun addPokemon(pokemon: Pokemon) {
        _pokemon.value = _pokemon.value + pokemon
    }

    private val _pokemon = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemon: StateFlow<List<Pokemon>> = _pokemon.asStateFlow()

    fun setSearchPokemonName(name: String) {
        _searchPokemonName.value = name
    }

    fun clearSearchPokemonName() {
        _searchPokemonName.value = ""
    }
}
