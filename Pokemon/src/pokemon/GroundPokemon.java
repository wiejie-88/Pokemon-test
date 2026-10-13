package pokemon;

public class GroundPokemon extends Pokemon{

	public GroundPokemon(String id, String name, int maxHp, int atk, int def, int grade, Move move) {
		super(id, name, Type.GROUND, maxHp, atk, def, grade, move);
	}
}

