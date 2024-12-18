package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.drew654.pokedex.R
import com.drew654.pokedex.models.FilterViewModel
import com.drew654.pokedex.models.PokemonViewModel
import com.drew654.pokedex.models.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    filterViewModel: FilterViewModel
) {
    val searchPokemonName = pokemonViewModel.searchPokemonName.collectAsState()
    val isSearching = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val regionFilter = filterViewModel.regionFilter.collectAsState()
    val type1Filter = filterViewModel.type1Filter.collectAsState()
    val type2Filter = filterViewModel.type2Filter.collectAsState()

    val pokemonList = pokemonViewModel.pokemon.collectAsState()
    val pokemonListFiltered = pokemonList.value.filter { pokemon ->
        regionFilter.value == null || pokemon.originalRegion == regionFilter.value
    }.filter { pokemon ->
        type1Filter.value == null || pokemon.types.contains(type1Filter.value)
    }.filter { pokemon ->
        type2Filter.value == null || pokemon.types.contains(type2Filter.value)
    }.filter { pokemon ->
        pokemon.name.contains(searchPokemonName.value, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            TextField(
                value = searchPokemonName.value,
                onValueChange = { pokemonViewModel.setSearchPokemonName(it) },
                placeholder = { Text("Search Pokemon") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            pokemonViewModel.clearSearchPokemonName()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_clear_24),
                            contentDescription = "Clear"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (regionFilter.value != null) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(0.5f)
                    ) {
                        FilterTag(type = regionFilter.value!!)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.weight(1f)
                ) {
                    if (type1Filter.value != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(1f)
                        ) {
                            FilterTag(type = type1Filter.value!!)
                        }
                    }
                    if (type2Filter.value != null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(1f)
                        ) {
                            FilterTag(type = type2Filter.value!!)
                        }
                    }
                }
            }

            LazyColumn {
                items(pokemonListFiltered.size) { index ->
                    val pokemon = pokemonListFiltered[index]
                    PokemonListing(
                        pokemon = pokemon,
                        navController = navController
                    )
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Screen.Filters.route) },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = com.drew654.pokedex.R.drawable.baseline_filter_list_24),
                contentDescription = "Filter"
            )
        }
    }
}
