package com.drew654.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.drew654.pokedex.models.FilterViewModel
import com.drew654.pokedex.models.Screen
import com.drew654.pokedex.ui.screens.FiltersScreen
import com.drew654.pokedex.ui.screens.pokemon_details.PokemonDetailsScreen
import com.drew654.pokedex.ui.screens.pokemon_list.PokemonListScreen
import com.drew654.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private val filterViewModel: FilterViewModel by viewModels<FilterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.PokemonList.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = Screen.PokemonList.route
                        ) {
                            PokemonListScreen(
                                navController = navController,
                                filterViewModel = filterViewModel
                            )
                        }
                        composable(
                            route = "${Screen.PokemonDetails.route}/{id}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            PokemonDetailsScreen(
                                id = it.arguments?.getInt("id")!!,
                                navController = navController
                            )
                        }
                        composable(
                            route = Screen.Filters.route
                        ) {
                            FiltersScreen(filterViewModel = filterViewModel)
                        }
                    }
                }
            }
        }
    }
}
