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

    fun clearFilters() {
        _regionFilter.value = null
    }
}
