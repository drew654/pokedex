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
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    init {
        viewModelScope.launch {
            loadGenerationsFromJson()
            loadTypesFromJson()
        }
    }

    private suspend fun loadGenerationsFromJson() {
        val json = Json { ignoreUnknownKeys = true }
        val generationsJson = withContext(Dispatchers.IO) {
            val inputStream = getApplication<Application>().assets.open("generation_names.json")
            json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() }).jsonArray
        }

        _generations.value = generationsJson.map { it.jsonPrimitive.content }
    }

    private val _generations = MutableStateFlow<List<String>>(emptyList())
    val generations: StateFlow<List<String>> = _generations.asStateFlow()

    private val _generationFilter = MutableStateFlow<String?>(null)
    val generationFilter: StateFlow<String?> = _generationFilter.asStateFlow()

    fun setGenerationFilter(generation: String) {
        _generationFilter.value = generation
    }

    private suspend fun loadTypesFromJson() {
        val json = Json { ignoreUnknownKeys = true }
        val typesJson = withContext(Dispatchers.IO) {
            val inputStream =
                getApplication<Application>().assets.open("type_names.json")
            json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() }).jsonArray
        }

        _types.value = typesJson.map { it.jsonPrimitive.content }
    }

    private val _types = MutableStateFlow<List<String>>(emptyList())
    val types: StateFlow<List<String>> = _types.asStateFlow()

    private val _type1Filter = MutableStateFlow<String?>(null)
    val type1Filter: StateFlow<String?> = _type1Filter.asStateFlow()

    fun setType1Filter(type: String) {
        _type1Filter.value = type
    }

    fun clearType1Filter() {
        _type1Filter.value = null
    }

    private val _type2Filter = MutableStateFlow<String?>(null)
    val type2Filter: StateFlow<String?> = _type2Filter.asStateFlow()

    fun setType2Filter(type: String) {
        _type2Filter.value = type
    }

    fun clearType2Filter() {
        _type2Filter.value = null
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
