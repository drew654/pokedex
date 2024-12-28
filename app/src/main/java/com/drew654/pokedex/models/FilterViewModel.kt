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
            loadRegionsFromJson()
            loadTypesFromJson()
        }
    }

    private suspend fun loadRegionsFromJson() {
        val json = Json { ignoreUnknownKeys = true }
        val regionsJson = withContext(Dispatchers.IO) {
            val inputStream =
                getApplication<Application>().assets.open("region_names.json")
            json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() }).jsonArray
        }

        _regions.value = regionsJson.map { it.jsonPrimitive.content }
    }

    private val _regions = MutableStateFlow<List<String>>(emptyList())
    val regions: StateFlow<List<String>> = _regions.asStateFlow()

    private val _regionFilter = MutableStateFlow<String?>(null)
    val regionFilter: StateFlow<String?> = _regionFilter.asStateFlow()

    fun setRegionFilter(region: String) {
        _regionFilter.value = region
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
        _regionFilter.value = null
        _type1Filter.value = null
        _type2Filter.value = null
        _hasBranchedEvolutionFilter.value = null
        _isMonotypeFilter.value = null
    }
}
