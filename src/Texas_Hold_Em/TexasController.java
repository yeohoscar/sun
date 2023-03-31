
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A GameOfPoker is a sequence of one or more RoundOfPoker's


public class TexasController extends MainController
{	

	
	public TexasController(String[] names, int bank) {

		numPlayers = names.length;
		
		players = new Player[numPlayers];
		
		for (int i = 0; i < numPlayers; i++)
			if (i == 0)
				players[i] = new HumanPlayer(names[i].trim(), bank);
			else
				players[i] = new ComputerPlayer(names[i].trim(), bank);
		
		deck  = new DeckOfTexasCards();
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play Poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//




	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Launcher for application
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	/*public static void main(String[] args) {
		String[] names = {"Human", "Tom", "Dick", "Harry"};

		System.out.println("\nWelcome to the Automated Poker Machine ...\n\n");

		System.out.print("\nWhat is your name?  ");

		byte[] input = new byte[100];

		try {
			System.in.read(input);

			names[0] = new String(input);
		}
		catch (Exception e){};

		int startingBank = 10;

		System.out.println("\nLet's play POKER ...\n\n");

		GameOfPoker game = new GameOfPoker(names, startingBank);

		game.play();
	}*/
	

}
