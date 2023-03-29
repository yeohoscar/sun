
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


import poker.PotOfMoney;

public class HumanPlayer extends Player {

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public HumanPlayer(String name, int money) {
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
		return askQuestion("Do you want to raise the bet by 1 chip");
	}
}
