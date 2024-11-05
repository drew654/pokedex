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


def main():
    pokemon_species = get_all_pokemon_species()
    pokemon = get_all_pokemon()

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
                type_data = get_data(type["type"]["url"])
                type_names = type_data["names"]
                for type_name in type_names:
                    if type_name["language"]["name"] == "en":
                        trimmed_data["types"] += [type_name["name"]]

            trimmed_data["color"] = species_data["color"]["name"]

            if not os.path.exists("pokemon"):
                os.makedirs("pokemon")
            file_path = os.path.join("pokemon", f"{id}.json")
            with open(file_path, "w") as f:
                f.write(json.dumps(trimmed_data))

            if not os.path.exists("sprites"):
                os.makedirs("sprites")
            file_path = os.path.join("sprites", f"{id}.png")
            image_url = f"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/{id}.png"
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
