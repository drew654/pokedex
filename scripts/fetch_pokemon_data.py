import requests
import os
import json


def add_to_list(id, filename):
    with open(filename, 'r') as f:
        ids = json.loads(f.read())
    ids.append(id)
    with open(filename, 'w') as f:
        f.write(json.dumps(ids))

def fetch_data(url):
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
    data = fetch_data("https://pokeapi.co/api/v2/type")
    types = data["results"]
    type_names = {}
    for type in types:
        url = type["url"]
        names = fetch_data(url)["names"]
        for name in names:
            if name["language"]["name"] == "en":
                type_names[type["name"]] = name["name"]
                break
    return type_names

def get_ability_names_en():
    data = fetch_data("https://pokeapi.co/api/v2/ability?limit=1000")
    abilities = data["results"]
    ability_names = {}
    for ability in abilities:
        url = ability["url"]
        names = fetch_data(url)["names"]
        for name in names:
            if name["language"]["name"] == "en":
                ability_names[ability["name"]] = name["name"]
                break
    return ability_names

def get_stat_names_en():
    data = fetch_data("https://pokeapi.co/api/v2/stat")
    stats = data["results"]
    stat_names = {}
    for stat in stats:
        url = stat["url"]
        names = fetch_data(url)["names"]
        for name in names:
            if name["language"]["name"] == "en":
                stat_names[stat["name"]] = name["name"]
                break
    return stat_names

def get_evolution_line_id(species_data):
    return int(species_data["evolution_chain"]["url"].split("/")[-2])

def get_evolution_line(evolution_chain_data, species_data):
    data = evolution_chain_data[get_evolution_line_id(species_data)]

    def build_evolution_tree(chain):
        species_id = int(chain["species"]["url"].split("/")[-2])
        evolution_tree = {"id": species_id}
        evolves_to = [build_evolution_tree(evolution) for evolution in chain["evolves_to"]]
        if evolves_to:
            evolution_tree["evolves_to"] = evolves_to
        return evolution_tree

    return build_evolution_tree(data["chain"])

def has_branched_evolution(pokemon_id, evolution_line):
    evolution_spot = {}
    if pokemon_id == evolution_line["id"]:
        evolution_spot = evolution_line
    else:
        for e in evolution_line["evolves_to"]:
            if e["id"] == pokemon_id:
                evolution_spot = e

    if "evolves_to" in evolution_spot and len(evolution_spot["evolves_to"]) > 1:
        return True
    return False

def get_pokedex_names_en():
    data = fetch_data("https://pokeapi.co/api/v2/pokedex?limit=100")
    pokedexes = data["results"]
    pokedex_names = {}
    for pokedex in pokedexes:
        url = pokedex["url"]
        names = fetch_data(url)["names"]
        for name in names:
            if name["language"]["name"] == "en":
                pokedex_names[pokedex["name"]] = name["name"]
                break
    return pokedex_names

def get_generation_names_en():
    data = fetch_data("https://pokeapi.co/api/v2/generation")
    generations = data["results"]
    generation_names = {}
    for generation in generations:
        url = generation["url"]
        names = fetch_data(url)["names"]
        for name in names:
            if name["language"]["name"] == "en":
                generation_names[generation["name"]] = name["name"]
                break
    return generation_names

def get_evolution_chain_data():
    evolution_chains = fetch_data("https://pokeapi.co/api/v2/evolution-chain?limit=1000")
    array_size = int(evolution_chains["results"][-1]["url"].split("/")[-2]) + 1
    evolution_chain_data = [None] * array_size
    for chain in evolution_chains['results']:
        index = int(chain['url'].split('/')[-2])
        data = fetch_data(chain['url'])
        evolution_chain_data[index] = data
    return evolution_chain_data

def get_pokemon_data():
    pokemon_count = fetch_data("https://pokeapi.co/api/v2/pokemon")["count"]
    pokemon = fetch_data(f"https://pokeapi.co/api/v2/pokemon?limit={pokemon_count}")
    array_size = int(pokemon["results"][-1]["url"].split("/")[-2]) + 1
    pokemon_data = [None] * array_size
    for poke in pokemon["results"]:
        index = int(poke["url"].split("/")[-2])
        data = fetch_data(poke["url"])
        pokemon_data[index] = data
    return pokemon_data

def get_pokemon_species_data():
    species_count = fetch_data("https://pokeapi.co/api/v2/pokemon-species")["count"]
    pokemon_species = fetch_data(f"https://pokeapi.co/api/v2/pokemon-species?limit={species_count}")
    array_size = int(species_count) + 1
    pokemon_species_data = [None] * array_size
    for poke in pokemon_species["results"]:
        index = int(poke["url"].split("/")[-2])
        data = fetch_data(poke["url"])
        pokemon_species_data[index] = data
    return pokemon_species_data

def save_pokemon_data(pokemon_index, pokemon_species_index, pokemon_data, pokemon_species_data, type_names_en, stat_names_en, ability_names_en, generation_names_en, evolution_chain_data):
    id = pokemon_data[pokemon_index]["id"]

    trimmed_data = {}
    trimmed_data["id"] = id

    names = None
    if pokemon_data[pokemon_index]["is_default"]:
        names = pokemon_species_data[pokemon_species_index]["names"]
    elif pokemon_data[pokemon_index]["forms"]:
        forms = pokemon_data[pokemon_index]["forms"]
        for form in forms:
            if form["name"] == pokemon_data[pokemon_index]["name"]:
                names = fetch_data(form["url"])["names"]
                break
    if not names:
        names = pokemon_species_data[pokemon_species_index]["names"]
    for name in names:
        if name["language"]["name"] == "en":
            trimmed_data["name"] = name["name"]
            break
    if not "name" in trimmed_data:
        trimmed_data["name"] = pokemon_data[pokemon_index]["name"]

    trimmed_data["types"] = []
    types = pokemon_data[pokemon_index]["types"]
    for type in types:
        trimmed_data["types"] += [type_names_en[type["type"]["name"]]]

    trimmed_data["abilities"] = []
    abilities = pokemon_data[pokemon_index]["abilities"]
    for ability in abilities:
        trimmed_data["abilities"] += [{"name": ability_names_en[ability["ability"]["name"]], "is_hidden": ability["is_hidden"]}]

    if pokemon_data[pokemon_index]["is_default"]:
        trimmed_data["evolution_line"] = get_evolution_line(evolution_chain_data, pokemon_species_data[pokemon_species_index])
        trimmed_data["has_branched_evolution"] = has_branched_evolution(id, trimmed_data["evolution_line"])
    else:
        trimmed_data["evolution_line"] = {"id": pokemon_index}
        trimmed_data["has_branched_evolution"] = False

    trimmed_data["generation"] = generation_names_en[pokemon_species_data[pokemon_species_index]["generation"]["name"]]

    trimmed_data["base_stats"] = {}
    for stat in pokemon_data[pokemon_index]["stats"]:
        trimmed_data["base_stats"][stat_names_en[stat["stat"]["name"]]] = stat["base_stat"]

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

    if pokemon_index <= 1025 or pokemon_data[pokemon_index]["name"].contains("-mega") or pokemon_data[pokemon_index]["name"].contains("-gmax") or pokemon_data[pokemon_index]["name"].contains("-alola") or pokemon_data[pokemon_index]["name"].contains("-galar") or pokemon_data[pokemon_index]["name"].contains("-hisui") or pokemon_data[pokemon_index]["name"].contains("-paldea"):
        add_to_list(id, "pokemon_order.json")

    if pokemon_data[pokemon_index]["is_default"]:
        for variety in pokemon_species_data[pokemon_species_index]["varieties"]:
            if not variety["is_default"]:
                pokemon_variety_index = int(variety["pokemon"]["url"].split("/")[-2])
                save_pokemon_data(pokemon_variety_index, pokemon_species_index, pokemon_data, pokemon_species_data, type_names_en, stat_names_en, ability_names_en, generation_names_en, evolution_chain_data)

def get_types_data():
    types = [
        {
            "name": "Normal",
            "light_color": "#C1C2C1",
            "color": "#9FA19F",
            "dark_color": "#676967"
        },
        {
            "name": "Fighting",
            "light_color": "#FFAC59",
            "color": "#FF8000",
            "dark_color": "#A65300"
        },
        {
            "name": "Flying",
            "light_color": "#ADD2F5",
            "color": "#81B9EF",
            "dark_color": "#54789B"
        },
        {
            "name": "Poison",
            "light_color": "#B884DD",
            "color": "#9141CB",
            "dark_color": "#5E2A84"
        },
        {
            "name": "Ground",
            "light_color": "#B88E6F",
            "color": "#915121",
            "dark_color": "#5E3515"
        },
        {
            "name": "Rock",
            "light_color": "#CBC7AD",
            "color": "#AFA981",
            "dark_color": "#726E54"
        },
        {
            "name": "Bug",
            "light_color": "#B8C26A",
            "color": "#91A119",
            "dark_color": "#5E6910"
        },
        {
            "name": "Ghost",
            "light_color": "#A284A2",
            "color": "#704170",
            "dark_color": "#492A49"
        },
        {
            "name": "Steel",
            "light_color": "#98C2D1",
            "color": "#60A1B8",
            "dark_color": "#3E6978"
        },
        {
            "name": "Fire",
            "light_color": "#EF7374",
            "color": "#E62829",
            "dark_color": "#961A1B"
        },
        {
            "name": "Water",
            "light_color": "#74ACF5",
            "color": "#2980EF",
            "dark_color": "#1B539B"
        },
        {
            "name": "Grass",
            "light_color": "#82C274",
            "color": "#3FA129",
            "dark_color": "#29691B"
        },
        {
            "name": "Electric",
            "light_color": "#FCD659",
            "color": "#FAC000",
            "dark_color": "#A37D00"
        },
        {
            "name": "Psychic",
            "light_color": "#F584A8",
            "color": "#EF4179",
            "dark_color": "#9B2A4F"
        },
        {
            "name": "Ice",
            "light_color": "#81DFF7",
            "color": "#3DCEF3",
            "dark_color": "#28869E"
        },
        {
            "name": "Dragon",
            "light_color": "#8D98EC",
            "color": "#5060E1",
            "dark_color": "#343E92"
        },
        {
            "name": "Dark",
            "light_color": "#998B8C",
            "color": "#624D4E",
            "dark_color": "#403233"
        },
        {
            "name": "Fairy",
            "light_color": "#F5A2F5",
            "color": "#EF70EF",
            "dark_color": "#9B499B"
        }
    ]
    return types

def main():
    pokemon_data = get_pokemon_data()
    pokemon_species_data = get_pokemon_species_data()
    type_names_en = get_type_names_en()
    stat_names_en = get_stat_names_en()
    types_data = get_types_data()
    ability_names_en = get_ability_names_en()
    generation_names_en = get_generation_names_en()
    evolution_chain_data = get_evolution_chain_data()

    with open("types.json", "w") as f:
        f.write(json.dumps(types_data))

    generation_names = []
    for generation in generation_names_en:
        generation_names.append(generation_names_en[generation])
    with open("generation_names.json", "w") as f:
        f.write(json.dumps(generation_names))

    with open("pokemon_order.json", "w") as f:
        f.write("[]")

    species_count = fetch_data("https://pokeapi.co/api/v2/pokemon-species")["count"]
    for i in range(1, species_count + 1):
        save_pokemon_data(i, i, pokemon_data, pokemon_species_data, type_names_en, stat_names_en, ability_names_en, generation_names_en, evolution_chain_data)


if __name__ == "__main__":
    main()
