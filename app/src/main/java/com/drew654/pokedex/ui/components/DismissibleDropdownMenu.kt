package com.drew654.pokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.drew654.pokedex.R

@Composable
fun DismissibleDropdownMenu(
    selectedValue: String?,
    label: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        DropdownMenu(
            selectedValue = selectedValue ?: "",
            label = label,
            options = options,
            onValueChange = onValueChange,
            modifier = modifier
        )

        if (selectedValue != null) {
            IconButton(
                onClick = {
                    onClear()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_clear_24),
                    contentDescription = "Clear dropdown"
                )
            }
        }
    }
}
