package pokeball;
import java.util.Random;

public class PokeBall {

	private String name;
	private int catchRate;
	
	public PokeBall() {
		this("Pokeball", 1);
	}

	public PokeBall(String name, int catchRate) {
        this.name = name;
        this.catchRate = catchRate;
    }

	public String getName() {
		return name;
	}

	public boolean catchPokemon(int grade) {
		if (catchRate == 100) {
			return true;
		} else {
			double probability = Math.min(1.0, (double) catchRate / (grade * 3));

			Random random = new Random();
			double randomValue = random.nextDouble();

			return probability > randomValue;
		}
	}
}
