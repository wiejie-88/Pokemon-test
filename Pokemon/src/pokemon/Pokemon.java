package pokemon;

public class Pokemon {

	private String id;
	private String name;
	private Type type;
	private int maxHp;
	private int currentHp;
	private int atk;
	private int def;
	private int grade;
	private Move move;

	public Pokemon(String id, String name, Type type, int hp, int atk, int def, int grade, Move move) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.maxHp = hp;
		this.atk = atk;
		this.def = def;
		this.grade = grade;
		this.move = move;
		currentHp = maxHp;
	}
	
	public String getMoveName() {
		return move.getName();
	}
	
	public int getMoveDamage() {
		return move.getDamage();
	}
	
	public Type getMoveType() {
		return move.getType();
	}
	
	public String getId() {
		return id;
	}
	
	public int getAttack() {
		return atk;
	}
	
	public int getDefense() {	
		return def;
	}
	
	public int getGrade() {
		return grade;
	}
	
	public void setDefense(int def) {
		this.def = def;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}

	public void attack(Pokemon opponent, int damage) {
		opponent.takeDamage(damage);
	}
	
	public void takeDamage(int damage) {
        currentHp -= damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

	public boolean isFainted() {
		return currentHp <= 0;
	}
	
	public String toString() {
		return String.format("%s (%s, HP: %d/%d)", name, type, currentHp, maxHp);
	}

	public String toGradeString() {
		return String.format("%s (%s, Grade: %d)", name, type, grade);
	}

	public String toDetailString() {
		return String.format("ID: %s\nNAME: %s\nTYPE: %s\nHP: %d/%d\nATK: %d\nDEF: %d\nGRADE: %d\nMOVE: %s(%s, Damage: %d)", id, name, type, currentHp, maxHp, atk, def, grade, getMoveName(), getMoveType(), getMoveDamage());
	}
}
