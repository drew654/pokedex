package com.drew654.pokedex.ui.screens.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.drew654.pokedex.models.Pokemon
import com.drew654.pokedex.models.Screen

@Composable
fun PokemonListing(
    pokemon: Pokemon,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(pokemon.color)
            .clickable {
                navController.navigate("${Screen.PokemonDetails.route}/${pokemon.id}")
            }
            .clip(RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Box {
            Column {
                Row {
                    Text(
                        text = "#${pokemon.id}",
                        color = Color(0x88000000),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Text(
                        text = pokemon.name,
                        color = Color(0x88000000),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                PokemonListingTypes(types = pokemon.types)
            }
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}
