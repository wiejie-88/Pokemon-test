package core;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import pokeball.GreatBall;
import pokeball.MasterBall;
import pokeball.PokeBall;
import pokeball.UltraBall;
import pokemon.Pokemon;
import stage.ForestStage;
import stage.MountainStage;
import stage.OceanStage;
import stage.Stage;

public class Game {

	private Menu menu;
	private Player player;
	private Stage stage;
	private Battle battle;
	private PokeBall ball;
	private Scanner scanner = new Scanner(System.in);
	
	private Random random = new Random();
	
	private boolean running = false;
	
	public Game() {
		this.player = null;
		this.menu = new Menu(this);
	}
	
	public void setPlayer(Player p) {
		this.player = p;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void start() {
		running = true;
		
		while (running) {
			// Login & Sign Up
			System.out.println(TITLE_ASCII);
			System.out.println("\n\033[1m--------------------------------------- << MENU >> --------------------------------------\033[0m");
			menu.open();
			
			System.out.println(TRAINER_ASCII);
			
			System.out.println("\n\033[1mAre you Ready To CATCH'EM ALL?\033[0m\n");
			System.out.print("Press Enter to Start Playing: ");
			scanner.nextLine();

			// Select Stage
			System.out.println("\n\033[1m----------------------------------- << SELECT STAGE >> ----------------------------------\033[0m");
			int stageOption = selectStage();

			switch(stageOption) {
				case 1:
					stage = new ForestStage();
					break;
				case 2:
					stage = new MountainStage();
					break;
				case 3:
					stage = new OceanStage();
					break;
			}
			System.out.println("You've chosen " + stage.getName() + "!");
			
			// Select starter poke
			ArrayList<Pokemon> starterPokemons = stage.generateStarterPokemon();
			selectStarterPokemon(starterPokemons);
			
			// Select Poke to battle
			ArrayList<Pokemon> playerTeam = selectPokemonToBattle();
			ArrayList<Pokemon> wildPokemons = stage.generateWildPokemon();
			
			// Battle
			battle = new Battle(player, playerTeam, new ArrayList<>(wildPokemons));
			battle.start();
			
			// Catch Poke
			if (battle.isWin()) {
				System.out.println("\n\033[1m-------------------------------- << CATCHING POKEMON >> ---------------------------------\033[0m");
				System.out.println("\nDraw Pokéballs and go CATCH'EM ALL! \n");
				scanner.nextLine();
				catchPokemon(wildPokemons);
			}
			
			// Save Player
			System.out.print("Press Enter to save your progress: ");
			scanner.nextLine();
			System.out.println("\nSaving your Progress...");
			DataBase.save(player);
			
			System.out.println("\n\033[1m------------------------------------ << TOP SCORE >> ------------------------------------\033[0m");
			DataBase.displayTopScores();
			System.out.println("\033[1m-----------------------------------------------------------------------------------------\033[0m");
			
			System.out.print("\nPress Enter to go back to Main Menu: ");
			scanner.nextLine();
		}
	}
	
	private int selectStage() {
		System.out.println("\nSelect Battle Stage.\n");
        System.out.println("1. Forest (Higher chance of encountering Grass and Rock Pokémon)");
        System.out.println("2. Mountain (Higher chance of encountering Fire and Ground Pokémon)");
        System.out.println("3. Ocean (Higher chance of encountering Water and Ice Pokémon)");
        
        int stageNo = -1;
        
        do {
        	try {
        		System.out.print("\nChoose a Stage: ");
        		stageNo = scanner.nextInt();
        		
        		if (stageNo < 1 || stageNo > 3) {
        			System.out.println("Invalid choice. Please try again.");
        		}
        	} catch (InputMismatchException e) {
        		System.out.println("Invalid input. Please try again.");
        		scanner.nextLine();
        	}
        }
        while (stageNo < 1 || stageNo > 3);
        
        return stageNo;
	}
	
	private void selectStarterPokemon(ArrayList<Pokemon> starterPokemons) {
		System.out.println("\n\n\033[1m<< Catch Starter Pokémon >>\033[0m");
		System.out.println("Look! Wild Pokémons appear. Select one Pokémon to Catch.\n");
		for (int i = 0; i < starterPokemons.size(); i++) {
			System.out.println((i + 1) + ". " + starterPokemons.get(i).toGradeString());
		}
		
		int choice = -1;
		do {
			try {
				System.out.print("\nChoose a Pokémon to Catch: ");
				choice = scanner.nextInt() - 1;
				
				if (choice < 0 || choice >= starterPokemons.size()) {
					System.out.println("Invalid choice. Please try again.");
					continue;
				}
				
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please try again.");
        		scanner.nextLine();
			}
		}
		while (choice < 0 || choice >= starterPokemons.size());
		
		Pokemon ps = starterPokemons.get(choice);
		System.out.println("You've caught " + ps.getName() + "! " + ps.getName() + " added into your inventory.");
		player.addToInventory(ps);
	}
	
	private ArrayList<Pokemon> selectPokemonToBattle() {
		ArrayList<Pokemon> playerTeam = new ArrayList<>();
		
		ArrayList<Pokemon> playerInventory = player.getInventoryPokemons();
		System.out.println("\n\033[1m------------------------------------ << INVENTORY >> ------------------------------------\033[0m\n");
		player.displayInventory();
		System.out.println("\033[1m-----------------------------------------------------------------------------------------\033[0m");

		System.out.println("\n\n\033[1m<< Select Pokémon to Battle >>\033[0m");
		System.out.println("Select up to two Pokémon to Battle.\n");
		if (playerInventory.size() <= 1) {
			playerTeam.add(playerInventory.get(0));
			// Rental Poke
			playerTeam.add(DataBase.loadPokemon("#010"));
			System.out.println("Since you only has one Pokémon in your inventory, we will rent you a Charizard.\n");
		}
		else {
			System.out.println("Your Inventory:");
			for (int i = 0; i < playerInventory.size(); i++) {
				System.out.println((i + 1) + ". " + playerInventory.get(i).toGradeString());
			}
			
			int first = -1;
			int second = -1;
			
			try {
				do {
					try {
						System.out.print("\nSelect first Pokémon: ");
						first = scanner.nextInt() - 1;
						
						if (first < 0 || first >= playerInventory.size()) {
							System.out.println("Invalid choice. Please try again.");
							continue;
						}
					} catch (InputMismatchException e) {
						System.out.println("Invalid input. Please try again.");
		        		scanner.nextLine();
					}
				}
				while (first < 0 || first >= playerInventory.size());
				
				do {
					try {
						System.out.print("\nSelect second Pokémon: ");
						second = scanner.nextInt() - 1;
						
						if (second < 0 || second >= playerInventory.size()) {
							System.out.println("Invalid choice. Please try again.");
							continue;
						}

						if (second == first) {
							System.out.println("You already have this Pokémon in your team.");
							continue;
						}
					} catch (InputMismatchException e) {
						System.out.println("Invalid input. Please try again.");
		        		scanner.nextLine();
					}
				}
				while (second < 0 || second >= playerInventory.size() || second == first);
				
				playerTeam.add(playerInventory.get(first));
				playerTeam.add(playerInventory.get(second));
				
				
				
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please try again.");
			}
		}

		System.out.println("\n\n\033[1m<< Your Team >>\033[0m\n");
		for (Pokemon p : playerTeam) {
			System.out.println(p);
		}
		System.out.println("\n\033[1m-----------------------------------------------------------------------------------------\033[0m");
		
		return playerTeam;
	}
	
	
	private void catchPokemon(ArrayList<Pokemon> wildPokemons) {
		
		System.out.print("Press Enter to draw a PokéBall: ");
		scanner.nextLine();

		int draw = random.nextInt(100);
		
		if (draw == 99) {
			ball = new MasterBall();
		}
		else if (draw >= 90) {
			ball = new UltraBall();
		}
		else if (draw >= 60) {
			ball = new GreatBall();
		}
		else {
			ball = new PokeBall();
		}
		System.out.println("\nYou've obtained a " + ball.getName() + "!\n");
		

		for (Pokemon p : wildPokemons) {
			System.out.print("Press Enter to Catch: ");
			scanner.nextLine();

			if (ball.catchPokemon(p.getGrade())) {
				System.out.println("GOTCHA! You've caught " + p.getName() + "! " + p.getName() + " added into your inventory.\n");
				player.addToInventory(p);
				
				switch(p.getGrade()) {
					case 1:
						player.updateScore(5);
						break;
					case 2:
						player.updateScore(10);
						break;
					case 3:
						player.updateScore(20);
						break;
				}
			}
			else {
				System.out.println("It's so close! Wild " + p.getName() + " ran away...\n");
			}
		}
	}
	
	private static final String TITLE_ASCII =
            "                                  ,'                                   \n" +
            "    _.----.        ____         ,'  _\\   ___    ___     ____          \n" +
            "_,-'       `.     |    |  /`.   \\,-'    |   \\  /   |   |    \\  |`.    \n" +
            "\\      __    \\    '-.  | /   `.  ___    |    \\/    |   '-.   \\ |  |   \n" +
            " \\.    \\ \\   |  __  |  |/    ,','_  `.  |          | __  |    \\|  |   \n" +
            "   \\    \\/   /,' _`.|      ,' / / / /   |          ,' _`.|     |  |   \n" +
            "    \\     ,-'/  /   \\    ,'   | \\/ / ,`.|         /  /   \\  |     |   \n" +
            "     \\    \\ |   \\_/  |   `-.  \\    `'  /|  |    ||   \\_/  | |\\    |   \n" +
            "      \\    \\ \\      /       `-.`.___,-' |  |\\  /| \\      /  | |   |   \n" +
            "       \\    \\ `.__,'|  |`-._    `|      |__| \\/ |  `.__,'|  | |   |   \n" +
            "        \\_.-'       |__|    `-._ |              '-.|     '-.| |   |   \n" +
            "                                `'                            '-._|   ";
	
	private static final String TRAINER_ASCII = 
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⣤⣤⣶⣿⢿⡶⣶⣦⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣤⣶⡿⣟⣯⢿⣽⣟⡾⣯⣟⡷⣯⣟⣽⣻⢿⣶⡤⣀⠀⠀⠀⠀⠀⠀⠀⠀\n"+
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⣿⣻⢾⣽⣻⣞⣯⣿⢾⣽⣳⢯⡿⣵⣻⢾⣽⣻⣞⣷⡈⢦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣳⢯⣟⣾⣳⢟⣾⣿⣟⡾⣽⣯⢟⡷⣯⣟⣾⡳⣟⣾⣷⠘⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣳⢯⣟⣾⣳⣿⣿⣿⣿⣿⣿⣿⣿⣯⣟⣷⣻⢾⣽⣻⢾⣽⡆⢹⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣏⡿⣾⢷⣿⣿⣿⡟⣿⢿⡿⣿⣿⢷⡿⣎⣷⢿⣾⣹⢿⡞⣇⢸⣆⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n"+
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢨⣿⢾⡽⣯⣿⡿⠟⠓⠛⢛⣿⣿⣿⣿⣿⣿⣿⠿⠛⠛⣛⣿⣿⣿⣿⣿⣿⣷⣦⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣠⣴⣿⣿⣿⠿⠷⣶⣄⣀⠀⣸⡿⢻⣿⣿⣿⡿⠃⣀⣴⢾⡟⠛⢻⣿⣿⣿⣿⣿⣿⣿⡿⣷⣦⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⢉⣩⣿⣿⣿⡇⠀⢠⠟⠩⡙⣿⠿⡀⣽⣿⣿⢯⠻⠞⡏⡖⡄⠹⡄⠀⢻⣿⣿⣿⣿⣿⣿⣿⡿⠻⠿⠷⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            " ⠀⠀⠀⠀⠀⠀⠀⢀⡶⠟⣿⣿⣿⣿⣷⡀⢸⠀⣧⣷⠙⠀⠀⢹⠿⠁⠀⠀⠀⡇⣧⡇⠀⢷⢀⣾⡿⣿⣿⣿⣿⣿⣿⣿⡶⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣾⣿⣿⣿⠧⢯⣧⣾⠀⢸⣿⠢⠀⠀⠀⠀⠀⠀⠀⠀⠇⢿⠃⠀⡾⢞⡯⠾⢼⣿⣿⣿⣿⣿⢯⡖⠒⠲⡄⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "  ⠀⠀⠀⠀⠀⠀⠀⣫⣾⣿⣿⣿⣇⣼⢻⠉⢧⢤⡯⣴⠀⠀⠠⣿⠀⠀⠀⠀⣙⣿⣿⠿⠁⣿⡇⠀⠀⠹⣿⣿⣿⣦⠏⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠠⣿⣿⣿⣿⣿⣿⣮⣿⡧⠺⠟⠋⠉⢀⣀⣠⣤⡤⢶⠶⡻⣿⠉⠁⢠⠂⢻⣇⠀⠀⠀⢿⣰⣢⣿⡀⠀⠀⢰⣿⣦⣀⠀⢀⣀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠛⠻⠿⣇⡀⠀⠀⠀⠈⢿⣇⠧⣙⢎⡱⣽⠃⠀⢀⡞⢠⣾⡿⢦⣀⣠⣾⣿⣟⡿⣷⣄⣴⣿⡿⣽⣿⠟⠁⠀⣻⡀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠙⠲⢤⡀⠀⠀⠻⣮⣱⣮⠟⠁⠀⣠⣞⣥⣿⡟⣶⣿⢿⣟⣯⣷⣻⣽⣿⣻⡿⣽⣻⣽⠃⠀⠀⣠⠞⠁⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠓⣦⣀⡀⠉⠀⢀⣠⢾⡟⠉⣺⣿⣻⢿⡽⣿⣞⣿⣿⡿⠿⢿⣿⣿⣽⢯⣿⣷⣶⣾⣇⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⣀⡤⠖⠛⠉⣿⠙⠛⠛⢋⣡⠞⠁⠀⣿⣿⣿⣻⣿⣿⣿⣽⣿⢸⣈⡇⣹⣯⣝⣟⣻⣻⣿⠟⠛⠒⢤⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠖⠉⠀⠀⠀⣠⣾⣥⣴⣶⣾⠟⠁⠀⠀⠀⣳⠀⠀⣀⠀⠀⠀⠙⢿⣶⣿⣿⡿⠋⠀⠀⠀⣾⠀⠀⠀⣠⡿⠃\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠰⢿⣤⣀⠀⠀⢀⣼⣿⣿⣿⣿⠟⠁⣠⣤⣀⡀⣰⣻⠖⠉⠉⠙⣆⠀⠀⠀⠈⠉⠁⠀⠀⢀⠔⠂⠈⠙⣻⠟⠁⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠖⢚⣿⢿⣶⣤⣾⣿⣿⣟⣿⣟⣤⣶⡟⣿⢻⣷⣿⡏⠀⠀⠀⣀⡿⠀⠀⠀⠀⠀⠀⠀⠐⠈⠀⠀⠀⣠⠟⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⠀⣾⢯⢷⣞⣽⢻⣿⣿⣿⣿⣯⣳⢷⡻⣭⡟⣾⣿⠳⣤⡤⠶⣏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡴⠁⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⡾⠀⣿⣛⣾⣺⣿⣾⣿⣿⣿⡃⣻⣷⢫⣟⣵⣻⡵⣿⡄⣸⠀⠀⠈⢿⡢⢤⣄⣀⣀⣀⣠⣤⢴⣻⠏⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡟⢸⣯⠷⣿⣿⣿⣿⣿⢿⣟⣿⢻⣞⣻⡼⣞⣵⣻⡼⣷⡄⠀⠀⠀⠀⠙⢤⣤⣤⣤⣤⣤⠞⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⡼⠁⢸⣯⣿⣿⣿⡿⣽⣾⣿⣿⣞⣻⣼⢳⡽⣞⣵⡳⣽⢻⣿⣦⣤⣀⣀⣀⣀⢹⣆⣀⠜⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n";

}
