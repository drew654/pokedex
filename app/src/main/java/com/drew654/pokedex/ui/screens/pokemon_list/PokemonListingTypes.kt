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
fun PokemonListingTypes(typesData: List<String>?) {
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
        ) {
            if (typesData?.toList()?.get(0) != null) {
                Text(
                    text = typesData.toList()[0].toString(),
                    color = Color(0x88000000),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(all = 2.dp)
                )
            }
        }
        if (typesData?.toList()?.size == 2) {
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0x88000000), RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = typesData.toList()[1].toString(),
                    color = Color(0x88000000),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(all = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(100.dp))
    }
}
