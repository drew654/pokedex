package com.drew654.pokedex.models

sealed class Screen(val route: String = "") {
    data object PokemonList : Screen(route = "pokemon-list")

    data object PokemonDetails : Screen(route = "pokemon-details")
}
