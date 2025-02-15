package com.drew654.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.drew654.pokedex.R
import com.drew654.pokedex.models.PokedexViewModel
import com.drew654.pokedex.ui.components.CheckboxRow
import com.drew654.pokedex.ui.components.DismissibleDropdownMenu

@Composable
fun FiltersScreen(navController: NavController, pokedexViewModel: PokedexViewModel) {
    val focusManager = LocalFocusManager.current
    val generations = pokedexViewModel.generations.collectAsState()
    val selectedGenerationFilter = pokedexViewModel.generationFilter.collectAsState()
    val types = pokedexViewModel.types.collectAsState()
    val selectedType1Filter = pokedexViewModel.type1Filter.collectAsState()
    val selectedType2Filter = pokedexViewModel.type2Filter.collectAsState()
    val hasBranchedEvolutionFilter = pokedexViewModel.hasBranchedEvolutionFilter.collectAsState()
    val isMonotypeFilter = pokedexViewModel.isMonotypeFilter.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(1) {
                    DismissibleDropdownMenu(
                        selectedValue = selectedGenerationFilter.value,
                        label = "Generation",
                        options = generations.value,
                        onValueChange = { selectedName ->
                            pokedexViewModel.setGenerationFilter(selectedName)
                        },
                        onClear = {
                            pokedexViewModel.setGenerationFilter(null)
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    DismissibleDropdownMenu(
                        selectedValue = selectedType1Filter.value,
                        label = "Type",
                        options = types.value.map { it.name },
                        onValueChange = { selectedName ->
                            pokedexViewModel.setType1Filter(selectedName)
                        },
                        onClear = {
                            pokedexViewModel.setType1Filter(null)
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    DismissibleDropdownMenu(
                        selectedValue = selectedType2Filter.value,
                        label = "Type",
                        options = types.value.map { it.name },
                        onValueChange = { selectedName ->
                            pokedexViewModel.setType2Filter(selectedName)
                        },
                        onClear = {
                            pokedexViewModel.setType2Filter(null)
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    CheckboxRow(
                        label = "Monotype",
                        checked = isMonotypeFilter.value == true,
                        onClick = {
                            if (isMonotypeFilter.value == null) {
                                pokedexViewModel.setIsMonotypeFilter(true)
                                if (selectedType1Filter.value == null && selectedType2Filter.value != null) {
                                    pokedexViewModel.setType1Filter(selectedType2Filter.value!!)
                                    pokedexViewModel.setType2Filter(null)
                                } else {
                                    pokedexViewModel.setType2Filter(null)
                                }
                            } else {
                                pokedexViewModel.setIsMonotypeFilter(null)
                            }
                        }
                    )

                    CheckboxRow(
                        label = "Branched Evolution",
                        checked = hasBranchedEvolutionFilter.value == true,
                        onClick = {
                            if (hasBranchedEvolutionFilter.value == null) {
                                pokedexViewModel.setHasBranchedEvolutionFilter(true)
                            } else {
                                pokedexViewModel.setHasBranchedEvolutionFilter(null)
                            }
                        }
                    )
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedButton(
            onClick = {
                pokedexViewModel.clearFilters()
                focusManager.clearFocus()
            },
            content = {
                Text(text = "Clear Filters")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
