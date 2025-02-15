package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drew654.pokedex.models.PokedexViewModel
import com.drew654.pokedex.utils.getTypeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    filter: String?,
    label: String,
    options: List<Pair<String, Color>>,
    onValueChange: (String?) -> Unit,
    pokedexViewModel: PokedexViewModel,
    modifier: Modifier = Modifier,
) {
    val isExpanded = remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FilterTag(
            text = filter?.replace("Generation", "Gen.") ?: label,
            color = getTypeColor(type = filter ?: label, pokedexViewModel = pokedexViewModel),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    isExpanded.value = true
                }
        )
    }

    if (isExpanded.value) {
        ModalBottomSheet(
            onDismissRequest = { isExpanded.value = false },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            LazyColumn {
                items(1) {
                    FilterTag(
                        text = "All ${label}s",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isExpanded.value = false
                                onValueChange(null)
                            }
                    )
                }
                items(options) { option ->
                    FilterTag(
                        text = option.first,
                        color = option.second,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isExpanded.value = false
                                onValueChange(option.first)
                            }
                    )
                }
            }
        }
    }
}
