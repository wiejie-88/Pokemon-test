package core;
import java.util.ArrayList;

import pokemon.Pokemon;

public class Player {

	private String id;
    private String name;
    private int score;
    private int totalScore;
    private ArrayList<Pokemon> inventoryPokemons = new ArrayList<>(); 

    public Player(String name, String id) {
    	this.id = id;
        this.name = name;
        this.score = 0;
        this.totalScore = 0;
    }
    
    public Player(String name, String id, int totalScore) {
    	this.name = name;
    	this.id = id;
    	this.score = 0;
    	this.totalScore = totalScore;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Pokemon> getInventoryPokemons() {
        return inventoryPokemons;
    }

    public void addToInventory(Pokemon p) {
		inventoryPokemons.add(p);
    }
    
    public void removeFromInventory(Pokemon p) {
        inventoryPokemons.remove(p);
    }
    
    public void clearInventory() {
    	inventoryPokemons.clear();
    }

    public int getScore() {
        return score;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setScore(int score) {
    	if (score > 0)
    		this.score = score;
    }

    public void setTotalScore(int tscore) {
    	if (tscore > 0)
    		this.totalScore = tscore;
    }

    public void updateScore(int score) {
    	if (score > 0) {
    		this.score += score;
    		this.totalScore += score;
    	}
    }
    
    public void displayInventory() {
    	for (Pokemon p : inventoryPokemons) {
    		System.out.println(p.toDetailString() + "\n");
    	}
    }

    public String toString() {
        return String.format("%s(ID: %s), Current Score: %d, Total Score: %d", name, id, score, totalScore);
    }
}
