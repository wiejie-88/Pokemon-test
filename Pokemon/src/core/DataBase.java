package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import pokemon.FirePokemon;
import pokemon.GrassPokemon;
import pokemon.GroundPokemon;
import pokemon.IcePokemon;
import pokemon.Move;
import pokemon.Pokemon;
import pokemon.RockPokemon;
import pokemon.Type;
import pokemon.WaterPokemon;

public class DataBase {
    private static final String PLAYER_FILE = "player.txt";
    private static final String POKEMON_FILE = "pokemon.csv";

    public static void save(Player p) {
    	ArrayList<Player> players = loadAllPlayers();
        boolean playerExists = false;

        // Check if the player exists and update their data
        for (int i = 0; i < players.size(); i++) {
            Player existingPlayer = players.get(i);
            if (existingPlayer.getId().equals(p.getId())) {
                players.set(i, p);  // Update existing player
                playerExists = true;
                break;
            }
        }

        // If player doesn't exist, add them to the list
        if (!playerExists) {
            players.add(p);
        }

        // Write all players back to the file (overwrite the existing data)
        try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_FILE));
            for (Player player : players) {
                writer.write(player.getId() + "," + player.getName() + "," + player.getTotalScore());
                writer.newLine();
                for (Pokemon pokemon : player.getInventoryPokemons()) {
                    writer.write(pokemon.getId());
                    writer.newLine();
                }
                writer.newLine(); // Add a blank line between players
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Pokemon> loadAllPokemons() {
    	ArrayList<Pokemon> pokemonList = new ArrayList<>();
        
        try {
        	BufferedReader reader = new BufferedReader(new FileReader(POKEMON_FILE));
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Skip the header or any empty lines
                if (line.trim().isEmpty()) continue;
                
                if (line.startsWith("#")) {
                	String[] columns = line.split(",");

                	// Parse each Pokémon's data
                	String id = columns[0];
                	String name = columns[1];
                	Type type = Type.valueOf(columns[2].toUpperCase());
                	int hp = Integer.parseInt(columns[3]);
                	int atk = Integer.parseInt(columns[4]);
                	int def = Integer.parseInt(columns[5]);
                	int grade = Integer.parseInt(columns[6]);
                	String moveName = columns[7];
                	Type moveType = Type.valueOf(columns[8].toUpperCase());
                	int movePower = Integer.parseInt(columns[9]);
                	
                	Move move = new Move(moveName, moveType, movePower);
                	
                	Pokemon pokemon = null;
                	switch(type) {
                	case Type.GRASS:
                		pokemon = new GrassPokemon(id, name, hp, atk, def, grade, move);
                		break;
                	case Type.FIRE:
                		pokemon = new FirePokemon(id, name, hp, atk, def, grade, move);
                		break;
                	case Type.WATER:
                		pokemon = new WaterPokemon(id, name, hp, atk, def, grade, move);
                		break;
                	case Type.ICE:
                		pokemon = new IcePokemon(id, name, hp, atk, def, grade, move);
                		break;
                	case Type.ROCK:
                		pokemon = new RockPokemon(id, name, hp, atk, def, grade, move);
                		break;
                	case Type.GROUND:
                		pokemon = new GroundPokemon(id, name, hp, atk, def, grade, move);
                		break;
                	}
                	
                	// Add the Pokémon to the list
                	if (pokemon != null) {
                		pokemonList.add(pokemon);
                	}
                }
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pokemonList;
    }
    
    public static ArrayList<Player> loadAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        try {
        	BufferedReader reader = new BufferedReader(new FileReader(PLAYER_FILE));
            String line;
            Player player = null;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;  // Skip empty lines

                String[] parts = line.split(",");
                if (parts.length > 1) {
                    // New player line: id, name, score
                    String id = parts[0];
                    String name = parts[1];
                    int highestScore = Integer.parseInt(parts[2]);
                    player = new Player(name, id, highestScore);
                    players.add(player);
                }

                // If a player is already initialized, check if this line is a Pokémon ID
                if (player != null && !line.isEmpty() && line.split(",").length == 1) {
                    for (Pokemon p : loadAllPokemons()) {
                        if (line.equals(p.getId())) {
                            player.addToInventory(p);  // Add the Pokémon to inventory
                            break;
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }
    
    public static Pokemon loadPokemon(String id) {
    	for (Pokemon p : loadAllPokemons()) {
    		if (p.getId().equals(id)) {
    			return p;
    		}
    	}
    	return null;
    }
    
    public static Player loadPlayer(String id) {
        for (Player p : loadAllPlayers()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null; // Return null if player doesn't exist
    }
    
    public static boolean isPlayerExists(String id) {
        for (Player p : loadAllPlayers()) {
            if (p.getId().equals(id)) {
                return true; // Player exists
            }
        }
        return false; // Player does not exist
    }
    
    public static void displayTopScores() {
        ArrayList<Player> players = loadAllPlayers();
        
        // Manually sort players by highest score in descending order using bubble sort
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < players.size() - 1; j++) {
                Player p1 = players.get(j);
                Player p2 = players.get(j + 1);
                if (p1.getTotalScore() < p2.getTotalScore()) {
                    // Swap players if p1 has a lower score than p2
                    players.set(j, p2);
                    players.set(j + 1, p1);
                }
            }
        }

        // Display top 5 players
        for (int i = 0; i < Math.min(5, players.size()); i++) {
            Player p = players.get(i);
            System.out.println((i + 1) + ". " + p.getName() + ": " + p.getTotalScore());
        }
    }
}