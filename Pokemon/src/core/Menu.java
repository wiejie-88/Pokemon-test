package core;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
	
	private Game game;
	private Scanner scanner = new Scanner(System.in);

	public Menu(Game game) {
		this.game = game;
	}

	public void open() {
		System.out.println("\nWelcome to Pokémon Ga-Olé!\n");
        System.out.println("1. Sign up");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        int option = -1;
        
        do {
        	try {
        		System.out.print("\nSelect an option to proceed: ");
                option = scanner.nextInt();

                if (option < 1 || option > 3) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine();
            }
        }
        while (option < 1 || option > 3);
        
        switch(option) {
	        case 1:
	        	signup();
	        	break;
	        case 2:
	        	login();
	        	break;
	        case 3:
	        	System.out.println("Exiting game... Thank you for playing!");
	        	System.exit(0);
	        	break;
        }
	}
	
	public void signup() {
		System.out.print("Enter your Username: ");
        String username = scanner.next();

        // Generate new player ID
        String newPlayerId = getNextAvailablePlayerId();

        // Create a new player with the generated ID
        Player newPlayer = new Player(username, newPlayerId, 0);
        game.setPlayer(newPlayer);
        DataBase.save(newPlayer);

        System.out.println("Sign-up successful! Your Player ID is: " + newPlayer.getId());
	}

	public void login() {
		boolean playerExists = false;
	    
	    // Loop until a valid player ID is entered
	    while (!playerExists) {
	        System.out.print("Please enter your Player ID: ");
	        String id = scanner.next();

	        if (DataBase.isPlayerExists(id)) {
	            // Load the player and set it to the game
	            game.setPlayer(DataBase.loadPlayer(id));
	            System.out.println("\nWelcome back, " + game.getPlayer().getName() + "!");
	            playerExists = true;
	        } else {
	            System.out.println("Player ID does not exist. Please try again.");
	        }
	    }
	}

    // Helper method to generate the next available Player ID in the format "P01", "P02", etc.
    private String getNextAvailablePlayerId() {
        ArrayList<Player> players = DataBase.loadAllPlayers();
        int highestId = 0;

        // Find the highest existing player ID number
        for (Player player : players) {
            String id = player.getId();
            if (id.startsWith("P")) {
                try {
                    int currentId = Integer.parseInt(id.substring(1));  // Remove 'P' and parse the number
                    highestId = Math.max(highestId, currentId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        highestId++;
        return String.format("P%02d", highestId);  // Format as "P01", "P02", ...
    }
}
