package com.drew654.pokedex.ui.screens.pokemon_details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbilitiesRow(abilities: List<String>) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (abilities.size) {
            1 -> {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
                        .weight(1f)
                ) {
                    Text(
                        text = abilities[0].toString(),
                        fontSize = 14.sp,
                        color = Color(0x88000000),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            2 -> {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 8.dp)
                        .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
                        .weight(1f)
                ) {
                    Text(
                        text = abilities[0].toString(),
                        fontSize = 14.sp,
                        color = Color(0x88000000),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Text(text = "or", color = Color(0x88000000), fontSize = 14.sp)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 12.dp)
                        .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
                        .weight(1f)
                ) {
                    Text(
                        text = abilities[1].toString(),
                        fontSize = 14.sp,
                        color = Color(0x88000000),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            else -> null
        }
    }
}
