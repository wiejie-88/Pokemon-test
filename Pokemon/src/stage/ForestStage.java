package stage;

import java.util.ArrayList;

import core.DataBase;
import pokemon.Pokemon;
import pokemon.RockPokemon;
import pokemon.GrassPokemon;

public class ForestStage extends Stage {

	public ForestStage() {
		super("Forest");
	}

	@Override
	public ArrayList<Pokemon> getSpawnablePokemon() {

		ArrayList<Pokemon> spawnablePokemons = new ArrayList<>();

		for (Pokemon p : DataBase.loadAllPokemons()) {
			if (p instanceof GrassPokemon || p instanceof RockPokemon) {
				if (p.getGrade() != 3) {
					spawnablePokemons.add(p);
					spawnablePokemons.add(p);
				}
				else {
					spawnablePokemons.add(p);
				}
			}
			else {
				spawnablePokemons.add(p);
			}
		}

		return spawnablePokemons;
	}
}
