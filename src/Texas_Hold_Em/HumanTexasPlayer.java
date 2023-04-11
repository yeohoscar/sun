package Texas_Hold_Em;

import poker.PotOfMoney;
import java.util.Scanner;

import static Texas_Hold_Em.Action.*;

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

			for (int i = 0; i < input.length; i++)
				if ((char)input[i] == 'y' || (char)input[i] == 'Y')
					return true;
		}
		catch (Exception e){};

		return false;
	}

	public void notifyInvalidAction(String action, String reason) {
		System.out.print("\n>> Cannot perform <" + action + "> due to <" + reason + ">\n\n");
	}

	// Prompts user to input number of chips to raise by

	@Override
	public void raiseBet(PotOfMoney pot) {
		Scanner scanner = new Scanner(System.in);
		int raiseAmount = -1;

		while (raiseAmount < pot.getCurrentStake() || raiseAmount > bank) {
			System.out.print("\n>> How many chips do you want to raise to? ");
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

		System.out.println("You want to raise to " + raiseAmount + " chips.");
		int needed = raiseAmount - stake;
		stake += needed;
		bank -= needed;
		pot.raiseStake(raiseAmount - pot.getCurrentStake());
		System.out.println("\n> " + getName() + " says: I raise to " + raiseAmount + " chips!\n");

		if(bank==0){
			allIn();
		}
	}

	public Action chooseAction(PotOfMoney pot) {
		byte[] input = new byte[100];
		Action chosenAction = null;
		try {
			while (chosenAction == null) {
				System.out.print("\n>> Pick an option: 1. Check  2. See  3. Raise  4. All In  5. Fold  ");
				System.in.read(input);

				for (byte b : input) {
					switch (((char) b)) {
						case '1' -> {
							if (shouldCheck(pot)) chosenAction = CHECK;
						}
						case '2' -> {
							if (shouldSee(pot)) chosenAction = SEE;
						}
						case '3' -> {
							if (shouldRaise(pot)) chosenAction = RAISE;
						}
						case '4' -> {
							if (shouldAllIn(pot)) chosenAction = ALL_IN;
						}
						case '5' -> {
							chosenAction = FOLD;
						}
						default -> {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chosenAction;
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
		if (pot.getCurrentStake() - stake > bank) {
			notifyInvalidAction("see", "insufficient chips");
			return false;
		}
		return askQuestion("Do you want to see the bet of " +
					addCount(pot.getCurrentStake() - getStake(), "chip", "chips"));
	}

	public boolean shouldRaise(PotOfMoney pot) {
		if (bank < pot.getCurrentStake() * 2 - stake) {
			notifyInvalidAction("raise", "insufficient chips");
			return false;
		}
		return askQuestion("Do you want to raise the bet?");
	}

	public boolean shouldAllIn(PotOfMoney pot) {
		return askQuestion("Do you want to all in?");
	}

	public boolean shouldCheck(PotOfMoney pot) {
		if (pot.getCurrentStake() == 0) {
			return askQuestion("Do you want to check?");
		} else {
			notifyInvalidAction("check", "a bet has already been placed");
			return false;
		}
	}
}
