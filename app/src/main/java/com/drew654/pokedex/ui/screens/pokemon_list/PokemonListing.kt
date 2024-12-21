package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.drew654.pokedex.models.Pokemon

@Composable
fun PokemonListing(
    pokemon: Pokemon,
    onClick: () -> Unit = {}
) {
    val pokemonBackgroundColor = if (isSystemInDarkTheme()) Color(0x22FFFFFF) else Color(0x88FFFFFF)

    Box {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    onClick()
                }
                .clip(RoundedCornerShape(12.dp))
                .padding(8.dp)
                .height(92.dp)
                .fillMaxWidth()
        ) {
            Box {
                Column {
                    Row {
                        Text(
                            text = "#${pokemon.id}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(all = 8.dp)
                        )
                        Text(
                            text = pokemon.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(all = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    PokemonListingTypes(
                        types = pokemon.types,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .background(
                        pokemonBackgroundColor,
                        RoundedCornerShape(
                            topStart = 58.dp,
                            bottomStart = 58.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(92.dp)
            )
        }
    }
}
