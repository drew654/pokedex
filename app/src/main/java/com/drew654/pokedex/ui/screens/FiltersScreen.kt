package com.drew654.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.drew654.pokedex.models.FilterViewModel
import com.drew654.pokedex.ui.components.DropdownMenu

@Composable
fun FiltersScreen(filterViewModel: FilterViewModel) {
    val focusManager = LocalFocusManager.current
    val regions = filterViewModel.regions.collectAsState()
    val selectedRegionFilter = filterViewModel.regionFilter.collectAsState()

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(1) {
                DropdownMenu(
                    selectedValue = selectedRegionFilter.value ?: "",
                    label = "Region",
                    options = regions.value,
                    onValueChange = { selectedName ->
                        filterViewModel.setRegionFilter(selectedName)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
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
