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
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    init {
        viewModelScope.launch {
            loadFiltersFromJson()
        }
    }

    private suspend fun loadFiltersFromJson() {
        val json = Json { ignoreUnknownKeys = true }
        val filtersJson = withContext(Dispatchers.IO) {
            val inputStream =
                getApplication<Application>().assets.open("filters.json")
            json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() }).jsonObject
        }

        val pokedexesJson = filtersJson["pokedexes"]?.jsonObject

        val pokedexFilters: List<Filter> = pokedexesJson?.keys?.map {
            val pokedex = pokedexesJson[it]?.jsonObject
            val name = pokedex?.get("name")?.jsonPrimitive?.content
            val ids: List<Int> =
                pokedex?.get("pokemon")?.jsonArray?.map { it.jsonPrimitive.int } as List<Int>
            Filter(name!!, ids.toSet())
        } ?: emptyList()

        _pokedexFilter.value = pokedexFilters[0]
        _pokedexFilters.value = pokedexFilters
        _isLoading.value = false
    }

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _pokedexFilter = MutableStateFlow<Filter?>(null)
    val pokedexFilter: StateFlow<Filter?> = _pokedexFilter.asStateFlow()

    private val _pokedexFilters = MutableStateFlow<List<Filter>>(emptyList())
    val pokedexFilters: StateFlow<List<Filter>> = _pokedexFilters.asStateFlow()

    fun setPokedexFilter(filter: Filter) {
        _pokedexFilter.value = filter
    }
}
