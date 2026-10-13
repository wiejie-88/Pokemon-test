package pokemon;

public enum Type {

	FIRE(0),
	WATER(1),
	GRASS(2),
	GROUND(3),
	ROCK(4),
	ICE(5);

	private int id;
	
	private static final double[][] EFFECTIVENESS_CHART = {
			//   FIRE   WATER  GRASS  GROUND  ROCK  ICE
			{ 1.0,   0.5,   2.0,   1.0,   0.5,  2.0 }, // FIRE
			{ 2.0,   0.5,   0.5,   2.0,   2.0,  1.0 }, // WATER
			{ 0.5,   2.0,   0.5,   2.0,   2.0,  1.0 }, // GRASS
			{ 2.0,   1.0,   0.5,   1.0,   2.0,  1.0 }, // GROUND
			{ 2.0,   1.0,   1.0,   0.5,   1.0,  2.0 }, // ROCK
			{ 0.5,   0.5,   2.0,   2.0,   1.0,  0.5 }  // ICE
	};

	private Type(int id) {
		this.id = id;
	}

	public int getTypeID() {
		return id;
	}

	public static double getEffectiveness(Type attackType, Type defenseType) {
		return EFFECTIVENESS_CHART[attackType.getTypeID()][defenseType.getTypeID()];
	}

}

