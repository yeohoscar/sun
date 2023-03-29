
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


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
		while (bet <= 0 || bet > bank) {
			System.out.print("Enter your bet amount (you have " + bank + " chips): ");
			try{
				if (input.hasNextInt()) {
					bet = input.nextInt();
					if (bet <= 0) {
						System.out.println("Invalid bet amount! Please enter a positive value.");
					} else if (bet > bank) {
						System.out.println("You don't have enough chips! Please enter a smaller value.");
					}
				}else{
					input.next();
					System.out.println("Invalid input! Please enter a valid number.");
				}
			}catch (Exception e) {}
		}

		hands.get(FIRST_HAND).setStake(bet);
		bank -= bet;

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
	// Ask the Human Player what cards to discard
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void discard() {
		System.out.println("\nYou have been dealt the following hand: \n\n" + getHand());
		
		System.out.print("\n>> You can discard up to " + MAX_DISCARD + " cards (e.g., 1,3): ");
		
		byte[] input = new byte[100];
		
		int numDiscarded = 0;
		
		try {
			System.in.read(input);
	
			for (int i = 0; i < input.length; i++)
				if ((char)input[i] >= '0' && (char)input[i] < '5') {
					throwaway(input[i] - '0', false);
					
					numDiscarded++;
					
					if (numDiscarded == MAX_DISCARD)
						break;
				}
		}
		catch (Exception e){};		
							 
		if (numDiscarded > 0) {
			reorganizeHand();
			
			System.out.println("\nYour hand now looks like: \n\n" + getHand());
			
			if (askQuestion("Would you like to fold"))
				fold();
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
		if (getStake() == 0)
			return true;
		else
			return askQuestion("Do you want to see the bet of " +
							   addCount(pot.getCurrentStake() - getStake(), "chip", "chips"));	
	}

	public boolean shouldRaise(PotOfMoney pot) {
		return askQuestion("Do you want to raise the bet by 1 chip");	
	}
}
