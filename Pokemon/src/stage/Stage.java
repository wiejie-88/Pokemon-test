package stage;

import java.util.ArrayList;
import java.util.Random;

import core.DataBase;
import pokemon.Pokemon;

public class Stage {

	private String name;
	private Random random = new Random();

	public Stage(String name) {
		this.name = name;
	}

	public ArrayList<Pokemon> getSpawnablePokemon() {
		return DataBase.loadAllPokemons();
	}

	public ArrayList<Pokemon> generateWildPokemon() {

		ArrayList<Pokemon> wildPokemons = new ArrayList<>();

		while (wildPokemons.size() < 2) {
			wildPokemons.add(getSpawnablePokemon().get(random.nextInt(getSpawnablePokemon().size())));
		}

		return wildPokemons;
	}
	
	public ArrayList<Pokemon> generateStarterPokemon() {
		ArrayList<Pokemon> starterPokemons = new ArrayList<>();
		
		while (starterPokemons.size() < 3) {
			Pokemon p = getSpawnablePokemon().get(random.nextInt(getSpawnablePokemon().size()));
			if (p.getGrade() != 3 && !starterPokemons.contains(p)) {
				starterPokemons.add(p);
			}
		}
		return starterPokemons;
	}

	public String getName() {
		return name;
	}
}
