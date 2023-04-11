package Texas_Hold_Em;

import poker.PotOfMoney;
import java.util.Scanner;

public class HumanTexasPlayer extends TexasPlayer {
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public HumanTexasPlayer(String name, int money,int id) {
		super(name, money, id);
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

			for (int i = 0; i < input.length; i++){
				if ((char)input[i] == 'y' || (char)input[i] == 'Y'){
					return true;
				}
			}
		}
		catch (Exception e){};

		return false;
	}

	// Prompts user to input number of chips to raise by

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
				if (pot.getCurrentStake() == 0) {
					if (raiseAmount < RoundController.BIG_BLIND_AMOUNT) {
						System.out.println("Raise amount must be at least the big blind amount.");
					}
				} else {
					if (raiseAmount < pot.getCurrentStake() || raiseAmount > bank) {
						System.out.println("Invalid input.");
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid input.");
				scanner.nextLine(); // Clear the scanner buffer
			}
		}

		System.out.println("You want to raise " + raiseAmount + " chips.");
		stake+=raiseAmount;
		bank-=raiseAmount;
		pot.raiseStake(raiseAmount);
		System.out.println("\n> " + getName() + " says: I raise by " + raiseAmount + " chip!\n");

		if(bank==0){
			allIn();
		}
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
}
