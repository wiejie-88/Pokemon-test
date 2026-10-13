package pokemon;

public class RockPokemon extends Pokemon{

	public RockPokemon(String id, String name, int maxHp, int atk, int def, int grade, Move move) {
		super(id, name, Type.ROCK, maxHp, atk, def, grade, move);
	}
	
}
