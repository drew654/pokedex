package com.drew654.pokedex.ui.screens.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drew654.pokedex.utils.getScreenWidthDp

@Composable
fun StatRow(statName: String, statValue: Int, maxStatValue: Int) {
    val screenWidthDp = getScreenWidthDp()
    val maxBarWidth = screenWidthDp - 176.dp
    val barWidth = maxBarWidth * (statValue / maxStatValue.toFloat())

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .width(120.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    AbsoluteRoundedCornerShape(topLeft = 12.dp, bottomLeft = 12.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = statName.replace("Special", "Sp."),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier

            )
        }
        Box(
            modifier = Modifier
                .padding(start = 132.dp, end = 12.dp)
                .height(36.dp)
                .width(barWidth)
                .background(
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    AbsoluteRoundedCornerShape(topRight = 12.dp, bottomRight = 12.dp)
                )
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(120.dp + barWidth - 32.dp))
            Text(
                text = statValue.toString(),
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}
