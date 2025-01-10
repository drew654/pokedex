package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FilterTag(
    text: String,
    color: Color? = MaterialTheme.colorScheme.surfaceVariant,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .padding(all = 4.dp)
            .background(
                color = color ?: MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .fillMaxWidth()
        )
    }
}
