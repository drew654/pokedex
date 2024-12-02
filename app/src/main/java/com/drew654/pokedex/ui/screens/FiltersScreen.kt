package com.drew654.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drew654.pokedex.models.FilterViewModel
import com.drew654.pokedex.ui.components.DropdownMenu

@Composable
fun FiltersScreen(filterViewModel: FilterViewModel) {
    val selectedPokedexFilter = filterViewModel.pokedexFilter.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(1) {
                DropdownMenu(
                    selectedValue = selectedPokedexFilter.value?.name ?: "",
                    label = "Pokédex",
                    options = filterViewModel.pokedexFilters.value.map { it.name },
                    onValueChange = { selectedName ->
                        println(selectedName)
                        filterViewModel.setPokedexFilter(
                            filterViewModel.pokedexFilters.value.first { filter -> filter.name == selectedName }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
