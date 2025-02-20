package com.drew654.pokedex.models

import android.app.Application
import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PokedexViewModel(application: Application) : AndroidViewModel(application) {
    init {
        viewModelScope.launch {
            val pokemonOrder = getApplication<Application>().assets.open("pokemon_order.json")
            val pokemonOrderJson = Json.parseToJsonElement(pokemonOrder.bufferedReader().use { it.readText() }).jsonArray
            val fileNames =
                getApplication<Application>().assets.list("pokemon")?.toList() ?: emptyList()

            fileNames
                .sortedBy { fileName ->
                    val pokemonId = fileName.substringBefore(".").toIntOrNull()

                    if (pokemonId != null) {
                        pokemonOrderJson.indexOfFirst { it.jsonPrimitive.content.toIntOrNull() == pokemonId }
                    } else {
                        -1
                    }
                }
                .forEach { fileName ->
                    val pokemon = loadOnePokemonFromJson(fileName)
                    addPokemon(pokemon)
                }
            loadGenerationsFromJson()
            loadTypesFromJson()
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
            val types = pokemonJson["types"]?.jsonArray?.map { it.jsonPrimitive.content }
            val baseStats = pokemonJson["base_stats"]?.jsonObject?.map { (key, value) ->
                key to value.jsonPrimitive.intOrNull!!
            }?.toMap()
            val generation = pokemonJson["generation"]?.jsonPrimitive?.content
            val hasBranchedEvolution = pokemonJson["has_branched_evolution"]?.jsonPrimitive?.boolean

            if (id != null && name != null && types != null && baseStats != null && generation != null && hasBranchedEvolution != null) {
                Pokemon(id, name, types, baseStats, generation, hasBranchedEvolution)
            } else {
                throw IllegalStateException("Invalid Pokemon data in $fileName")
            }
        }
    }

    private suspend fun loadTypesFromJson() {
        val json = Json { ignoreUnknownKeys = true }
        val typesJson = withContext(Dispatchers.IO) {
            val inputStream =
                getApplication<Application>().assets.open("types.json")
            json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() }).jsonArray
        }
        val types = typesJson.map { it ->
            val name = it.jsonObject["name"]?.jsonPrimitive?.content
            val lightColor = it.jsonObject["light_color"]?.jsonPrimitive?.content
            val color = it.jsonObject["color"]?.jsonPrimitive?.content
            val darkColor = it.jsonObject["dark_color"]?.jsonPrimitive?.content

            if (name != null && lightColor != null && color != null && darkColor != null) {
                Type(
                    name,
                    Color(parseColor(lightColor)),
                    Color(parseColor(color)),
                    Color(parseColor(darkColor))
                )
            } else {
                throw IllegalStateException("Invalid Type data")
            }
        }

        _types.value = types
    }

    private suspend fun loadGenerationsFromJson() {
        val json = Json { ignoreUnknownKeys = true }
        val generationsJson = withContext(Dispatchers.IO) {
            val inputStream = getApplication<Application>().assets.open("generation_names.json")
            json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() }).jsonArray
        }

        _generations.value = generationsJson.map { it.jsonPrimitive.content }
    }

    private val _searchPokemonName = MutableStateFlow("")
    val searchPokemonName: StateFlow<String> = _searchPokemonName.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    fun setIsSearching(isSearching: Boolean) {
        _isSearching.value = isSearching
    }

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

    private val _generations = MutableStateFlow<List<String>>(emptyList())
    val generations: StateFlow<List<String>> = _generations.asStateFlow()

    private val _generationFilter = MutableStateFlow<String?>(null)
    val generationFilter: StateFlow<String?> = _generationFilter.asStateFlow()

    fun setGenerationFilter(generation: String?) {
        _generationFilter.value = generation
    }

    private val _types = MutableStateFlow<List<Type>>(emptyList())
    val types: StateFlow<List<Type>> = _types.asStateFlow()

    private val _type1Filter = MutableStateFlow<String?>(null)
    val type1Filter: StateFlow<String?> = _type1Filter.asStateFlow()

    fun setType1Filter(type: String?) {
        _type1Filter.value = type
    }

    private val _type2Filter = MutableStateFlow<String?>(null)
    val type2Filter: StateFlow<String?> = _type2Filter.asStateFlow()

    fun setType2Filter(type: String?) {
        _type2Filter.value = type
    }

    private val _hasBranchedEvolutionFilter = MutableStateFlow<Boolean?>(null)
    val hasBranchedEvolutionFilter: StateFlow<Boolean?> = _hasBranchedEvolutionFilter.asStateFlow()

    fun setHasBranchedEvolutionFilter(hasBranchedEvolution: Boolean?) {
        _hasBranchedEvolutionFilter.value = hasBranchedEvolution
    }

    private val _isMonotypeFilter = MutableStateFlow<Boolean?>(null)
    val isMonotypeFilter: StateFlow<Boolean?> = _isMonotypeFilter.asStateFlow()

    fun setIsMonotypeFilter(isMonotype: Boolean?) {
        _isMonotypeFilter.value = isMonotype
    }

    fun clearFilters() {
        _generationFilter.value = null
        _type1Filter.value = null
        _type2Filter.value = null
        _hasBranchedEvolutionFilter.value = null
        _isMonotypeFilter.value = null
    }
}
