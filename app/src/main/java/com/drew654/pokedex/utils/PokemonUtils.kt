package com.drew654.pokedex.utils

import android.content.Context
import com.drew654.pokedex.models.Pokemon
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun getEvolutionsWithIndention(
    evolutionData: JsonElement?,
    context: Context,
    indentLevel: Int = 0
): List<Pair<Pokemon, Int>> {
    val json = Json { ignoreUnknownKeys = true }
    val pokemonList = mutableListOf<Pair<Pokemon, Int>>()
    evolutionData?.jsonObject?.let { jsonObject ->
        val id = jsonObject["id"]?.jsonPrimitive?.intOrNull
        if (id != null) {
            val inputStream = context.assets.open("pokemon/${id}.json")
            val pokemonJson =
                json.parseToJsonElement(inputStream.bufferedReader().use { it.readText() })
            val name = pokemonJson.jsonObject["name"]?.jsonPrimitive?.content
            val color = pokemonJson.jsonObject["color"]?.jsonPrimitive?.content
            val types =
                pokemonJson.jsonObject["types"]?.jsonArray?.map { it.jsonPrimitive.content }
                    ?.toList()!!
            val pokemonObject = Pokemon(id, name.toString(), color.toString(), types)
            pokemonList.add(pokemonObject to indentLevel)

            val evolvesTo = jsonObject["evolves_to"]?.jsonArray
            evolvesTo?.forEach { evolution ->
                pokemonList.addAll(getEvolutionsWithIndention(evolution, context, indentLevel + 1))
            }
        }
    }
    return pokemonList
}
