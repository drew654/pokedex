package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.drew654.pokedex.R
import com.drew654.pokedex.models.FilterViewModel
import com.drew654.pokedex.models.PokemonViewModel
import com.drew654.pokedex.models.Screen
import com.drew654.pokedex.ui.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    filterViewModel: FilterViewModel
) {
    val searchPokemonName = pokemonViewModel.searchPokemonName.collectAsState()
    val isSearching = pokemonViewModel.isSearching.collectAsState()

    val types = filterViewModel.types.collectAsState()
    val generations = filterViewModel.generations.collectAsState()
    val generationFilter = filterViewModel.generationFilter.collectAsState()
    val type1Filter = filterViewModel.type1Filter.collectAsState()
    val type2Filter = filterViewModel.type2Filter.collectAsState()
    val hasBranchedEvolutionFilter = filterViewModel.hasBranchedEvolutionFilter.collectAsState()
    val isMonotypeFilter = filterViewModel.isMonotypeFilter.collectAsState()

    val pokemonList = pokemonViewModel.pokemon.collectAsState()
    val pokemonListFiltered = pokemonList.value.filter { pokemon ->
        generationFilter.value == null || pokemon.generation == generationFilter.value
    }.filter { pokemon ->
        type1Filter.value == null || pokemon.types.contains(type1Filter.value)
    }.filter { pokemon ->
        type2Filter.value == null || pokemon.types.contains(type2Filter.value)
    }.filter { pokemon ->
        isMonotypeFilter.value == null || pokemon.types.size == 1
    }.filter { pokemon ->
        pokemon.name.contains(searchPokemonName.value, ignoreCase = true)
    }.filter { pokemon ->
        hasBranchedEvolutionFilter.value == null || pokemon.hasBranchedEvolution == hasBranchedEvolutionFilter.value
    }

    fun clearSearch() {
        pokemonViewModel.clearSearchPokemonName()
        filterViewModel.clearFilters()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            if (isSearching.value) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            pokemonViewModel.setIsSearching(false)
                            clearSearch()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                    SearchBar(
                        query = searchPokemonName.value,
                        onQueryChange = { pokemonViewModel.setSearchPokemonName(it) },
                        clearQuery = {
                            clearSearch()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Filters.route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_filter_list_24),
                            contentDescription = "Filter"
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    FilterDropdown(
                        filter = generationFilter.value,
                        label = "Generation",
                        options = generations.value.map { it to MaterialTheme.colorScheme.surfaceVariant },
                        onValueChange = { selectedName ->
                            filterViewModel.setGenerationFilter(selectedName)
                        },
                        filterViewModel = filterViewModel,
                        modifier = Modifier.weight(1f)
                    )
                    FilterDropdown(
                        filter = type1Filter.value,
                        label = "Type",
                        options = types.value.map { it -> it.name to if (isSystemInDarkTheme()) it.darkColor else it.lightColor },
                        onValueChange = { selectedName ->
                            filterViewModel.setType1Filter(selectedName)
                        },
                        filterViewModel = filterViewModel,
                        modifier = Modifier.weight(1f)
                    )
                    if (isMonotypeFilter.value == null) {
                        FilterDropdown(
                            filter = type2Filter.value,
                            label = "Type",
                            options = types.value.map { it -> it.name to if (isSystemInDarkTheme()) it.darkColor else it.lightColor },
                            onValueChange = { selectedName ->
                                filterViewModel.setType2Filter(selectedName)
                            },
                            filterViewModel = filterViewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (isMonotypeFilter.value == true) {
                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                    FilterTag(
                        text = "Monotype",
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            if (hasBranchedEvolutionFilter.value == true) {
                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                    FilterTag(
                        text = "Branched Evolution",
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            LazyColumn {
                items(pokemonListFiltered.size) { index ->
                    val pokemon = pokemonListFiltered[index]
                    PokemonListing(
                        pokemon = pokemon,
                        onClick = {
                            navController.navigate("${Screen.PokemonDetails.route}/${pokemon.id}")
                        }
                    )
                }
            }
        }
    }

    if (!isSearching.value) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
        ) {
            FloatingActionButton(
                onClick = {
                    pokemonViewModel.setIsSearching(true)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "Filter"
                )
            }
        }
    }
}
