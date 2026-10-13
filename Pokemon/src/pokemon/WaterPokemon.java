package pokemon;

public class WaterPokemon extends Pokemon{

	public WaterPokemon(String id, String name, int maxHp, int atk, int def, int grade, Move move) {
		super(id, name, Type.WATER, maxHp, atk, def, grade, move);
	}
	
}
