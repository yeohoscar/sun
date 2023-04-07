
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus

import poker.Player;
import poker.PotOfMoney;

import java.util.Scanner;

public class HumanTexasPlayer extends TexasPlayer {
	public static int MAX_DISCARD	=	3; // maximum number of cards a player can discard

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public HumanTexasPlayer(String name, int money) {
		super(name, money);
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// User Interface
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	// Ask the user a question and return true if the user says yes, 
	// otherwise return false

	public boolean askQuestion(String question) 	{
		System.out.print("\n>> " + question + " (y/n)?  ");

		byte[] input = new byte[100];

		try {
			System.in.read(input);

			for (int i = 0; i < input.length; i++)
				if ((char)input[i] == 'y' || (char)input[i] == 'Y')
					return true;
		}
		catch (Exception e){};

		return false;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Key decisions a player must make
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public boolean shouldOpen(PotOfMoney pot) {
		return true;
	}

	public boolean shouldSee(PotOfMoney pot) {
		if (getStake() == 0)
			return true;
		else
			return askQuestion("Do you want to see the bet of " +
					addCount(pot.getCurrentStake() - getStake(), "chip", "chips"));
	}

	public boolean shouldRaise(PotOfMoney pot) {
		return askQuestion("Do you want to raise the bet?");
	}

	public boolean shouldAllIn(PotOfMoney pot) {
		return askQuestion("Do you want to all in?");
	}

	public boolean shouldCheck(PotOfMoney pot) {
		if (pot.getCurrentStake() == 0) {
			return askQuestion("Do you want to check?");
		} else {
			return false;
		}
	}

	@Override
	public void raiseBet(PotOfMoney pot) {
		if (getBank() == 0||pot.getCurrentStake()>bank*2) {
			System.out.println("No enough chips");
			return;
		}
		Scanner scanner = new Scanner(System.in);
		int raiseAmount = -1;

		while (raiseAmount < pot.getCurrentStake() || raiseAmount > bank) {
			System.out.print("\n>> How many chips do you want to raise? ");
			try {
				raiseAmount = scanner.nextInt();
				if (raiseAmount < pot.getCurrentStake() || raiseAmount > bank) {
					System.out.println("Invalid input.");
				}
			} catch (Exception e) {
				System.out.println("Invalid input.");
				scanner.nextLine(); // Clear the scanner buffer
			}
		}

		System.out.println("You want to raise " + raiseAmount + " chips.");
		stake+=raiseAmount;
		bank-=raiseAmount;
		pot.raiseStake(raiseAmount- pot.getCurrentStake());
		System.out.println("\n> " + getName() + " says: and I raise you 1 chip!\n");

		if(bank==0){
			allIn();
		}
	}
}
