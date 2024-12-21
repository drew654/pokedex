package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PokemonListingTypes(types: List<String>?, color: Color = Color.Unspecified) {
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .border(1.dp, color, RoundedCornerShape(12.dp))
        ) {
            if (types?.toList()?.get(0) != null) {
                Text(
                    text = types.toList()[0].toString(),
                    color = color,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(all = 2.dp)
                )
            }
        }
        if (types?.toList()?.size == 2) {
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, color, RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = types.toList()[1].toString(),
                    color = color,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(all = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(100.dp))
    }
}
