package core;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import pokemon.Pokemon;
import pokemon.Type;
import ui.AttackGaugeUI;
import ui.SpiritBoostUI;

public class Battle {

	private Player player;
	private ArrayList<Pokemon> ally = new ArrayList<>();
	private ArrayList<Pokemon> foe = new ArrayList<>();
	private AttackGaugeUI attackGaugeUI;
	private SpiritBoostUI spiritBoostUI;

	private static Scanner scanner = new Scanner(System.in);
	private static Random random = new Random();

	private int turn = 1;
	private boolean playerRound;

	public Battle(Player player, ArrayList<Pokemon> ally, ArrayList<Pokemon> foe) {
		this.player = player;
		this.ally = ally;
		this.foe = foe;
	}
	
	private void switchRound() {
		playerRound = !playerRound;
	}

	public boolean isGameOver() {
		return isWin() || isLose();
	}

	public boolean isWin() {
		return foe.isEmpty();
	}

	public boolean isLose() {
		return ally.isEmpty();
	}

	private int calculateDamage(int attackerAttack, int defenderDefense, int moveDamage, double effectiveness, int spirit) {
		return (int) Math.floor((((attackerAttack * moveDamage) / defenderDefense) * effectiveness) + spirit);
	}

	private int selectTarget() {
		int numberOfPokemonLeft = foe.size();
		for (int i = 0; i < numberOfPokemonLeft; i++) {
			System.out.println("[" + (i + 1) + "] " + foe.get(i));
		}

		int choice = -1;
		do {
			try {
				System.out.print("Select target to attack: ");
				choice = scanner.nextInt();

				if (choice < 1 || choice > numberOfPokemonLeft)
					System.out.println("Invalid choice. Please try again.");

        	} catch (InputMismatchException e) {
        		System.out.println("Invalid input. Please try again.");
        		scanner.nextLine();
        	}
		} while (choice < 1 || choice > numberOfPokemonLeft);

		return choice - 1;
	}
	
	private boolean canGetDefenseRose() {
		System.out.print("Press Enter to Flip a coin: ");
        scanner.nextLine();

        // Flip first coin
        if (random.nextInt(2) != 0) {
        	System.out.println("It's a TAIL!");
            return false;
        }

        // Flip second coin if first was HEADS
        System.out.println("It's a HEAD!");
        System.out.print("Press Enter to Flip another coin: ");
        scanner.nextLine();

        if (random.nextInt(2) != 0) {
        	System.out.println("It's a TAIL!");
        	return false;
        }
        
        System.out.println("It's a HEAD!");
        return true;
	}
	
	private void allyDefenseBoost() {
		if (turn >= 2) {
			System.out.println("\n\033[1m<< Defense Roulette >>\033[0m");
			System.out.println("Flip 2 coins. If two heads, gain a Defense Boost.\n");
			if (canGetDefenseRose()) {
				for (Pokemon a : ally) {
					a.setDefense((int) (a.getDefense() * 1.1));
				}
				System.out.println("Your Pokemon gain a Defense Boost!");
			} else {
				System.out.println("Your Pokemon did not gain a Defense Boost.");	
			}
		} 
	}
	
	private void foeDefenseBoost() {
		if (turn >= 2 && random.nextInt(4) == 0) {
			for (Pokemon f : foe) {
				f.setDefense((int) (f.getDefense() * 1.1));
			}
			System.out.println("Opponent's Pokemon gain a Defense Boost.");
		}
	}

	private void printTeamInfo(ArrayList<Pokemon> team) {
		for (Pokemon p : team) {
			System.out.println(p);
		}
	}

	private void printBattleInfo(Pokemon attacker, Pokemon defender, int damage, double effectiveness) {
		String effectivenessDescription;

		if (damage == 0) {
			System.out.printf("\n%s use %s! But it doesn't affect %s.\n", attacker.getName(), attacker.getMoveName(), defender.getName());
		}
		else {
			if (effectiveness == 2.0) {
				effectivenessDescription = "It's super effective!";
			} else if (effectiveness == 0.5) {
				effectivenessDescription = "It's not very effective!";
			} else {
				effectivenessDescription = "";
			}
			System.out.printf("\n%s use %s! Dealing %d damage to %s. %s\n", attacker.getName(),
					attacker.getMoveName(), damage, defender.getName(), effectivenessDescription);
		}
	}
	
	private void allyAttack() {
		System.out.println("\n\n=========================================================================================");
		System.out.print("ROUND " + turn + ": " + "Ally's Turn!\n");
		System.out.println("=========================================================================================\n");
		foeDefenseBoost();

		for (Pokemon a : ally) {
			System.out.printf("\n\033[1mIt's Ally %s's turn!\033[0m\n\n", a.getName());
			
			Pokemon target = foe.get(selectTarget());
			
			System.out.println("\n\033[1m<< Spirit Boost >>\033[0m");
			System.out.println("Boost spirit to increase Attack by mashing the buttons. You have 5 seconds!\n");
			System.out.print("Are you Ready? Press ENTER to continue: ");
			scanner.nextLine();

			spiritBoostUI = new SpiritBoostUI();
			scanner.nextLine();
			int spirit = spiritBoostUI.chargeSpirit();
			
			System.out.println("\n" + a.getName() + " has charged " + spirit + " spirit!");
			double effectiveness = Type.getEffectiveness(a.getMoveType(), target.getType());
			int damage = calculateDamage(a.getAttack(), target.getDefense(), a.getMoveDamage(), effectiveness, spirit);
			
			a.attack(target, damage);
			printBattleInfo(a, target, damage, effectiveness);
			printTeamInfo(foe);
			
			if (target.isFainted()) {
				System.out.printf("\nFoe %s is fainted.\n", target.getName());
				foe.remove(target);

				switch(target.getGrade()) {
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
				
				if (isGameOver()) {
					break;
				}
			}
		}
	}
	
	private void foeAttack() {
		System.out.println("\n\n=========================================================================================");
		System.out.print("ROUND " + turn + ": " + "Foe's Turn!\n");
		System.out.println("=========================================================================================\n");
		allyDefenseBoost();

		for (Pokemon f : foe) {
			System.out.printf("\n\033[1mIt's Foe %s's turn!\033[0m\n", f.getName());
			Pokemon target = ally.get(random.nextInt(ally.size()));
			
			int spirit = Math.max(10, random.nextInt(30));
			double effectiveness = Type.getEffectiveness(f.getMoveType(), target.getType());
			int damage = calculateDamage(f.getAttack(), target.getDefense(), f.getMoveDamage(), effectiveness, spirit);
			
			f.attack(target, damage);
			printBattleInfo(f, target, damage, effectiveness);
			printTeamInfo(ally);
			
			if (target.isFainted()) {
				System.out.printf("\nAlly %s is fainted.\n", target.getName());
				ally.remove(target);
				
				if (isGameOver()) {
					break;
				}
			}
		}
	}

	public void start() {
		System.out.println("\nWild " + foe.get(0).getName() + " and " + foe.get(1).getName() + " appear!");
		
		System.out.println("\n\033[1mAre you Ready To Battle?\033[0m\n");
		System.out.print("Press Enter to Start Battle: ");
		scanner.nextLine();
		
		System.out.println("\n\033[1m--------------------------------- << BATTLE START !! >> ---------------------------------\033[0m");
		System.out.println("\n\033[1m<< Attack Gauge >>\033[0m");
		System.out.println("Hit the SMASH button to charge energy. Whoever hits 100% will attack first!\n");
		System.out.print("Are you Ready? Press ENTER to continue: ");
		scanner.nextLine();

		attackGaugeUI = new AttackGaugeUI();
		playerRound = attackGaugeUI.isPlayerAttackFirst();
		System.out.println(playerRound ? "You Go First!" : "Your Opponent Goes First!");

		while (!isGameOver()) {

			if (playerRound) {
				allyAttack();
			}
			else {
				foeAttack();
			}
			turn++;
			switchRound();
		}
		System.out.println("\n\033[1mGame Over!\033[0m\n");

		if (isWin())
			System.out.println("You Win! Time to Catch Pokemon!");
		if (isLose())
			System.out.println("You Lose!");

		System.out.println("\n\033[1m--------------------------------- << BATTLE ENDS !! >> ----------------------------------\033[0m\n");
		System.out.println(player);
	}
}
