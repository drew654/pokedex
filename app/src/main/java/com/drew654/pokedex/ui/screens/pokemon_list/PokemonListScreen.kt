package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
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
    val regions = filterViewModel.regions.collectAsState()
    val regionFilter = filterViewModel.regionFilter.collectAsState()
    val type1Filter = filterViewModel.type1Filter.collectAsState()
    val type2Filter = filterViewModel.type2Filter.collectAsState()

    val pokemonList = pokemonViewModel.pokemon.collectAsState()
    val pokemonListFiltered = pokemonList.value.filter { pokemon ->
        regionFilter.value == null || pokemon.originalRegion == regionFilter.value
    }.filter { pokemon ->
        val isMonotype = type1Filter.value != null && type1Filter.value == type2Filter.value
        if (isMonotype) {
            pokemon.types.size == 1 && pokemon.types.contains(type1Filter.value)
        } else {
            (type1Filter.value == null || pokemon.types.contains(type1Filter.value)) &&
                    (type2Filter.value == null || pokemon.types.contains(type2Filter.value))
        }
    }.filter { pokemon ->
        pokemon.name.contains(searchPokemonName.value, ignoreCase = true)
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterDropdown(
                        filter = regionFilter.value,
                        label = "Region",
                        options = regions.value,
                        onValueChange = { selectedName ->
                            filterViewModel.setRegionFilter(selectedName)
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    FilterDropdown(
                        filter = type1Filter.value,
                        label = "Type",
                        options = types.value,
                        onValueChange = { selectedName ->
                            filterViewModel.setType1Filter(selectedName)
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    FilterDropdown(
                        filter = type2Filter.value,
                        label = "Type",
                        options = types.value,
                        onValueChange = { selectedName ->
                            filterViewModel.setType2Filter(selectedName)
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
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
