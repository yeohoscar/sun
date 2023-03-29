
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A GameOfPoker is a sequence of one or more RoundOfPoker's


public class TexasController
{	
	private Player[] players;
	
	private Deck deck;
	
	private int numPlayers;
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
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
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumPlayers() {
		return numPlayers;	
		
	}
	
	
	public Player getPlayer(int num) {
		if (num >= 0 && num <= numPlayers)
			return players[num];
		else
			return null;
	}

	
	public int getNumSolventPlayers() { 
		// how many players still have money left?
	
		int count = 0;
		
		for (int i = 0; i < getNumPlayers(); i++)
			if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
				count++;
		
		return count;
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play Poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void play()	{
		int dealerIndex=0;
		int numSolventPlayers = players.length;
		while (getNumSolventPlayers() > 1) {
			deck.reset();//before each game starts, shuffle the deck
			numSolventPlayers=getNumSolventPlayers();

			//every player can act as dealer, dealer also needs to place bet, but dealer is the last one to place bet
			players[dealerIndex] = new DealerPlayer(players[dealerIndex].getName(), players[dealerIndex].getBank());
			players[dealerIndex].setDealer(true);

			//start a game, there are four rounds within a game: Pre-flop, Turn, River and the one after River.
			RoundsOfTexas round = new RoundsOfTexas(deck, players, dealerIndex, numSolventPlayers);
			round.play();
			
			try {
				System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");
				
				byte[] input = new byte[100];
				
				System.in.read(input);
				
				for (int i = 0; i < input.length; i++)
					if ((char)input[i] == 'q' || (char)input[i] == 'Q')
						return;
				dealerIndex += 1;
			}
			catch (Exception e) {};

		}
	}


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
