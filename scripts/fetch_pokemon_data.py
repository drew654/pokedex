import requests
import os
import json

total_pokemon = 1025

def get_all_pokemon_species():
    url = "https://pokeapi.co/api/v2/pokemon-species?limit={}".format(total_pokemon)
    try:
        response = requests.get(url)

        if response.status_code == 200:
            return response.json()
        else:
            print("Error: ", response.status_code)
            return None
    except requests.exceptions.RequestException as e:
        print("Error: ", e)
        return None

def get_all_pokemon():
    url = "https://pokeapi.co/api/v2/pokemon?limit={}".format(total_pokemon)
    try:
        response = requests.get(url)

        if response.status_code == 200:
            return response.json()
        else:
            print("Error: ", response.status_code)
            return None
    except requests.exceptions.RequestException as e:
        print("Error: ", e)
        return None

def get_data(url):
    try:
        response = requests.get(url)

        if response.status_code == 200:
            return response.json()
        else:
            print("Error: ", response.status_code)
            return None
    except requests.exceptions.RequestException as e:
        print("Error: ", e)
        return None

def get_type_names_en():
    data = get_data("https://pokeapi.co/api/v2/type")
    types = data["results"]
    type_names = {}
    for type in types:
        url = type["url"]
        names = get_data(url)["names"]
        for name in names:
            if name["language"]["name"] == "en":
                type_names[type["name"]] = name["name"]
                break
    return type_names

def get_ability_names_en():
    data = get_data("https://pokeapi.co/api/v2/ability?limit=1000")
    abilities = data["results"]
    ability_names = {}
    for ability in abilities:
        url = ability["url"]
        names = get_data(url)["names"]
        for name in names:
            if name["language"]["name"] == "en":
                ability_names[ability["name"]] = name["name"]
                break
    return ability_names

def get_evolution_line_id(id):
    data = get_data(f"https://pokeapi.co/api/v2/pokemon-species/{id}")
    return data["evolution_chain"]["url"].split("/")[-2]

def get_evolution_line(pokemon_id):
    data = get_data(f"https://pokeapi.co/api/v2/evolution-chain/{get_evolution_line_id(pokemon_id)}")

    def build_evolution_tree(chain):
        species_id = int(chain["species"]["url"].split("/")[-2])
        evolution_tree = {"id": species_id}
        evolves_to = [build_evolution_tree(evolution) for evolution in chain["evolves_to"]]
        if evolves_to:
            evolution_tree["evolves_to"] = evolves_to
        return evolution_tree

    return build_evolution_tree(data["chain"])

def main():
    pokemon_species = get_all_pokemon_species()
    pokemon = get_all_pokemon()
    type_names_en = get_type_names_en()
    ability_names_en = get_ability_names_en()

    if pokemon_species and pokemon:
        pokemon_names = []
        for i in range(total_pokemon):
            species_data = get_data(pokemon_species["results"][i]["url"])
            pokemon_data = get_data(pokemon["results"][i]["url"])

            id = species_data["id"]
            print(f"\rPokemon data fetched: {int(id / total_pokemon * 100)}%", end="", flush=True)

            trimmed_data = {}
            trimmed_data["id"] = id

            names = species_data["names"]
            for name in names:
                if name["language"]["name"] == "en":
                    trimmed_data["name"] = name["name"]
                    pokemon_names.append(name["name"])
                    break

            trimmed_data["types"] = []
            types = pokemon_data["types"]
            for type in types:
                trimmed_data["types"] += [type_names_en[type["type"]["name"]]]

            trimmed_data["abilities"] = []
            abilities = pokemon_data["abilities"]
            for ability in abilities:
                trimmed_data["abilities"] += [{"name": ability_names_en[ability["ability"]["name"]], "is_hidden": ability["is_hidden"]}]

            trimmed_data["color"] = species_data["color"]["name"]

            trimmed_data["evolution_line"] = get_evolution_line(id)

            if not os.path.exists("pokemon"):
                os.makedirs("pokemon")
            file_path = os.path.join("pokemon", f"{id}.json")
            with open(file_path, "w") as f:
                f.write(json.dumps(trimmed_data))

            if not os.path.exists("sprites"):
                os.makedirs("sprites")
            file_path = os.path.join("sprites", f"{id}.png")
            image_url = f"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/{id}.png"
            image = requests.get(image_url)
            with open(file_path, "wb") as f:
                f.write(image.content)

        file_path = os.path.join("pokemon", "names.json")
        with open(file_path, "w") as f:
            f.write(json.dumps(pokemon_names))

    else:
        print("Failed to get pokemon species")


if __name__ == "__main__":
    main()
