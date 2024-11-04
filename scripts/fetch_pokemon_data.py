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


def get_pokemon_species(url):
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

    if pokemon_species:
        pokemon_names = []
        for species in pokemon_species["results"]:
            species_data = get_pokemon_species(species["url"])
            id = species_data["id"]
            print(f"\rPokemon data fetched: {int(id / total_pokemon * 100)}%", end="", flush=True)

            trimmed_data = {}

            names = species_data["names"]
            for name in names:
                if name["language"]["name"] == "en":
                    trimmed_data["name"] = name["name"]
                    pokemon_names.append(name["name"])
                    break

            if not os.path.exists("pokemon"):
                os.makedirs("pokemon")
            file_path = os.path.join("pokemon", f"{id}.json")
            with open(file_path, "w") as f:
                f.write(json.dumps(trimmed_data))
        file_path = os.path.join("pokemon", "names.json")
        with open(file_path, "w") as f:
            f.write(json.dumps(pokemon_names))

    else:
        print("Failed to get pokemon species")


if __name__ == "__main__":
    main()
