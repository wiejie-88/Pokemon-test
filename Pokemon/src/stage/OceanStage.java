package stage;

import java.util.ArrayList;

import core.DataBase;
import pokemon.Pokemon;
import pokemon.IcePokemon;
import pokemon.WaterPokemon;

public class OceanStage extends Stage {

	public OceanStage() {
		super("Ocean");
	}

	@Override
	public ArrayList<Pokemon> getSpawnablePokemon() {

		ArrayList<Pokemon> spawnablePokemons = new ArrayList<>();

		for (Pokemon p : DataBase.loadAllPokemons()) {
			if (p instanceof WaterPokemon || p instanceof IcePokemon) {
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
