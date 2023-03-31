
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus

import poker.PotOfMoney;

import java.util.Scanner;

public class HumanPlayer extends Player {
	public static int MAX_DISCARD	=	3; // maximum number of cards a player can discard

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public HumanPlayer(String name, int money) {
		super(name, money);
	}

	public void placeBet(int bet) {
		Scanner input = new Scanner(System.in);
		while (bet <= 0 || bet > getBank()) {
			System.out.print("Enter your bet amount (you have " + getBank() + " chips): ");
			try{
				if (input.hasNextInt()) {
					bet = input.nextInt();
					if (bet <= 0) {
						System.out.println("Invalid bet amount! Please enter a positive value.");
					} else if (bet > getBank()) {
						System.out.println("You don't have enough chips! Please enter a smaller value.");
					}
				}else{
					input.next();
					System.out.println("Invalid input! Please enter a valid number.");
				}
			}catch (Exception e) {}
		}

		getBank() -= bet;

		System.out.println("\n> " + getName() + " says: I bet with " + bet + " chip!\n");
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
		return askQuestion("Do you want to raise the bet by 1 chip");
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
}
