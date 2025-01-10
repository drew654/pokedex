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
import androidx.compose.material3.Checkbox
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
import com.drew654.pokedex.models.FilterViewModel
import com.drew654.pokedex.ui.components.DropdownMenu

@Composable
fun FiltersScreen(navController: NavController, filterViewModel: FilterViewModel) {
    val focusManager = LocalFocusManager.current
    val generations = filterViewModel.generations.collectAsState()
    val selectedGenerationFilter = filterViewModel.generationFilter.collectAsState()
    val types = filterViewModel.types.collectAsState()
    val selectedType1Filter = filterViewModel.type1Filter.collectAsState()
    val selectedType2Filter = filterViewModel.type2Filter.collectAsState()
    val hasBranchedEvolutionFilter = filterViewModel.hasBranchedEvolutionFilter.collectAsState()
    val isMonotypeFilter = filterViewModel.isMonotypeFilter.collectAsState()

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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenu(
                            selectedValue = selectedGenerationFilter.value ?: "",
                            label = "Generation",
                            options = generations.value,
                            onValueChange = { selectedName ->
                                filterViewModel.setGenerationFilter(selectedName)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        if (selectedGenerationFilter.value != null) {
                            IconButton(
                                onClick = {
                                    filterViewModel.setGenerationFilter(null)
                                    focusManager.clearFocus()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_clear_24),
                                    contentDescription = "Clear filter"
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenu(
                            selectedValue = selectedType1Filter.value ?: "",
                            label = "Type",
                            options = types.value,
                            onValueChange = { selectedName ->
                                filterViewModel.setType1Filter(selectedName)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        if (selectedType1Filter.value != null) {
                            IconButton(
                                onClick = {
                                    filterViewModel.setType1Filter(null)
                                    focusManager.clearFocus()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_clear_24),
                                    contentDescription = "Clear filter"
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenu(
                            selectedValue = selectedType2Filter.value ?: "",
                            label = "Type",
                            options = types.value,
                            onValueChange = { selectedName ->
                                filterViewModel.setType2Filter(selectedName)
                            },
                            disabled = isMonotypeFilter.value == true,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        if (selectedType2Filter.value != null) {
                            IconButton(
                                onClick = {
                                    filterViewModel.setType2Filter(null)
                                    focusManager.clearFocus()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_clear_24),
                                    contentDescription = "Clear filter"
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isMonotypeFilter.value == null) {
                                    filterViewModel.setIsMonotypeFilter(true)
                                    if (selectedType1Filter.value == null && selectedType2Filter.value != null) {
                                        filterViewModel.setType1Filter(selectedType2Filter.value!!)
                                        filterViewModel.setType2Filter(null)
                                    } else {
                                        filterViewModel.setType2Filter(null)
                                    }
                                } else {
                                    filterViewModel.setIsMonotypeFilter(null)
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = isMonotypeFilter.value == true,
                            onCheckedChange = {
                                if (isMonotypeFilter.value == null) {
                                    filterViewModel.setIsMonotypeFilter(true)
                                    if (selectedType1Filter.value == null && selectedType2Filter.value != null) {
                                        filterViewModel.setType1Filter(selectedType2Filter.value!!)
                                        filterViewModel.setType2Filter(null)
                                    } else {
                                        filterViewModel.setType2Filter(null)
                                    }
                                } else {
                                    filterViewModel.setIsMonotypeFilter(null)
                                }
                            }
                        )
                        Text(text = "Monotype")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (hasBranchedEvolutionFilter.value == null) {
                                    filterViewModel.setHasBranchedEvolutionFilter(true)
                                } else {
                                    filterViewModel.setHasBranchedEvolutionFilter(null)
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = hasBranchedEvolutionFilter.value == true,
                            onCheckedChange = {
                                if (hasBranchedEvolutionFilter.value == null) {
                                    filterViewModel.setHasBranchedEvolutionFilter(true)
                                } else {
                                    filterViewModel.setHasBranchedEvolutionFilter(null)
                                }
                            }
                        )
                        Text(text = "Branched Evolution")
                    }
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
                filterViewModel.clearFilters()
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
