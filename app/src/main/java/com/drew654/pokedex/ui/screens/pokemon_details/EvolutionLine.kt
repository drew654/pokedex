package com.drew654.pokedex.ui.screens.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.drew654.pokedex.models.Screen
import com.drew654.pokedex.utils.conditional
import com.drew654.pokedex.utils.getEvolutionsWithIndention
import kotlinx.serialization.json.JsonElement

@Composable
fun EvolutionLine(id: Int, evolutionData: JsonElement?, navController: NavController) {
    val context = LocalContext.current
    val namesWithIndent = getEvolutionsWithIndention(evolutionData, context)
    val pokemonBackgroundColor = if (isSystemInDarkTheme()) Color(0x22FFFFFF) else Color(0x88FFFFFF)

    Column {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Evolution Line",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        namesWithIndent.forEach { (pokemon, indentLevel) ->
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Spacer(modifier = Modifier.padding(horizontal = (indentLevel * 32).dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .conditional(id != pokemon.id) {
                            clickable {
                                navController.navigate("${Screen.PokemonDetails.route}/${pokemon.id}")
                            }
                        }
                ) {
                    AsyncImage(
                        model = "file:///android_asset/sprites/${pokemon.id}.png",
                        contentDescription = pokemon.toString(),
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(pokemonBackgroundColor)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Column {
                        Text(
                            text = "#${pokemon.id}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 24.sp
                        )
                        Text(
                            text = pokemon.name,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}
