package com.drew654.pokedex.models

class Pokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val baseStats: Map<String, Int>,
    val generation: String,
    val hasBranchedEvolution: Boolean
) {
    val imageUrl = "file:///android_asset/sprites/${id}.png"
}
