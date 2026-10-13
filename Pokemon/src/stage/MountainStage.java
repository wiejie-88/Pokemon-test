package stage;

import java.util.ArrayList;

import core.DataBase;
import pokemon.FirePokemon;
import pokemon.GroundPokemon;
import pokemon.Pokemon;

public class MountainStage extends Stage {

	public MountainStage() {
		super("Mountain");
	}

	@Override
	public ArrayList<Pokemon> getSpawnablePokemon() {

		ArrayList<Pokemon> spawnablePokemons = new ArrayList<>();

		for (Pokemon p : DataBase.loadAllPokemons()) {
			if (p instanceof FirePokemon || p instanceof GroundPokemon) {
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
