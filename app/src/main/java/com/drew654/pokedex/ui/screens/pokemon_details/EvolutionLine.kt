package com.drew654.pokedex.ui.screens.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Spacer(modifier = Modifier.padding(horizontal = (indentLevel * 32).dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .conditional(id != pokemon.id) {
                            clickable {
                                navController.navigate("${Screen.PokemonDetails.route}/${pokemon.id}")
                            }
                        }
                        .padding(all = 8.dp)
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
