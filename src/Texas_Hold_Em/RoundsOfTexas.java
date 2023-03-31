
package Texas_Hold_Em;
import poker.*;
// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's

public class RoundsOfTexas {
	public static int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
	
	private Player[] players;
	private int dealerIndex;
	private DeckOfTexasCards deck;
	private int numPlayers;
	private int smallIndex;
	private int bigIndex;
	private int numSolventPlayers;
	private int currentMaxStake;
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public RoundsOfTexas(DeckOfTexasCards deck, Player[] players, int dealerIndex, int numSolventPlayers) {
		this.deck    = deck;
		this.players = players;
		this.dealerIndex = dealerIndex;
		this.numSolventPlayers = numSolventPlayers;
		numPlayers = players.length;

		//in each game, there must have small blind and big blind
		//and two players will make small blind and big blind
		System.out.println("Small Blind and Big Blind: ");
		blindBet(dealerIndex, players.length-1);

		System.out.println("\n\nNew Deal:\n\n");
		//after small blind and big blind, deal two cards to each player
		deal();
		
//		while (!canOpen()) deal();  // continue to redeal until some player can open

//		openRound();

//		discard();
	}

	//determine the index of smallBlind and bigBlind, and two players will make small blind and big blind
	public void blindBet(int dealerIndex, int sizeOfPlayers){
		if(dealerIndex==(sizeOfPlayers-1)){
			smallIndex = sizeOfPlayers;
			bigIndex = 0;
		}else if(dealerIndex==sizeOfPlayers){
			smallIndex = 0;
			bigIndex = 1;
		}else {
			smallIndex = dealerIndex+1;
			bigIndex = dealerIndex+2;
		}
		players[smallIndex].smallBlind();
		players[bigIndex].bigBlind();
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
	
								
	
	public int getNumActivePlayers() {
	    // how many players have not folded yet?

		int count = 0;
		
		for (int i = 0; i < getNumPlayers(); i++)
			if (getPlayer(i) != null && !getPlayer(i).hasFolded() && !getPlayer(i).isBankrupt())
				count++;
		
		return count;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Modifiers
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void removePlayer(int num) {
		if (num >= 0 && num < numPlayers)
		{
			System.out.println("\n> " + players[num].getName() + " leaves the game.\n");
			
			players[num] = null;
		}
	}	
	

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Find the player with the best hand
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumBestPlayer(boolean display) {
		int bestHandScore = 0, score = 0, bestPos = 0;
			
		Player bestPlayer = null, currentPlayer = null;
			
		for (int i = 0; i < getNumPlayers(); i++) {
			currentPlayer = getPlayer(i);
				
			if (currentPlayer == null || currentPlayer.hasFolded())
				continue;
				
			score = currentPlayer.getHand().getValue();
				
			if (score > bestHandScore) {
				if (display) {
					if (bestHandScore == 0)
						System.out.println("> " + currentPlayer.getName() + " goes first:\n" + 
											 currentPlayer.getHand());
					else
						System.out.println("> " + currentPlayer.getName() + " says 'Read them and weep:'\n" + 
											 currentPlayer.getHand());
				}
				
				bestPos		  = i;
				bestHandScore = score;
				bestPlayer    = currentPlayer;
			}
			else
			if (display)
				System.out.println("> " + currentPlayer.getName() + " says 'I lose':\n" + 
										  currentPlayer.getHand());
		}

		return bestPos;
	}
		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Restart all the players and deal them into the game
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void deal() {
		for (int i = 0; i < getNumPlayers(); i++) {
			if (getPlayer(i) != null) {
				if (getPlayer(i).isBankrupt())
					removePlayer(i);
				else {
					getPlayer(i).reset();
					getPlayer(i).dealTo(deck);
					
					System.out.println(getPlayer(i));
				}
			}
		}
		
		System.out.println("\n");
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Open this round of poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	/*public void openRound()	{
		Player player = null;
		
		System.out.println("");
		
		for (int i = 0; i < numPlayers; i++) {
			player = getPlayer(i);
			
			if (player == null || player.isBankrupt()) 
				continue;
			
			if (player.getHand() instanceof High)
				System.out.println("> " + player.getName() + " says: I cannot open.");
			else
				System.out.println("> " + player.getName() + " says: I can open.");
		}
	}*/


	public void preFlopRound(int currentIndex, PotOfMoney pot){
		Player currentPlayer;
		while(!players[currentIndex].isDealer()){
			if(!players[currentIndex].isBankrupt() && players[currentIndex] != null){
				currentPlayer = players[currentIndex];
				//current player takes his own action
				currentPlayer.nextAction(pot);
				/*//TODO: maybe we don't need to determine if currentPlayer is Human or Computer with switch?
				//		each player can take four actions: check, bet, call, raise, fold, all-in, so we can call takeTurn only once
				//				/*switch (currentPlayer.getClass().getSimpleName()){
				//					case "HumanPlayer" -> {
				//						currentPlayer.takeTurn(currentMaxStake);
				//						break;
				//					}
				//					case "ComputerPlayer" -> {
				//						currentPlayer.takeTurn(currentMaxStake);
				//						break;
				//					}
				//					default -> {
				//						break;
				//					}
				//				}*/
			}
			//if the player next to dealer is not the last one, then after this player takes actions, currentIndex increases by one
			//else, the player next to dealer is the last one in the array, currentIndex should be reassigned to zero
			if(currentIndex!=players.length-1){
				currentIndex++;
			}else {
				currentIndex=0;
			}
		}
		players[currentIndex].nextAction(pot);//this player is the dealer, which is the last one takes actions in this subround

	}
	public void subRoundHelper(int currentIndex, PotOfMoney pot){
		Player currentPlayer;
		while(!players[currentIndex].isDealer()){
			if(!players[currentIndex].isBankrupt() && players[currentIndex] != null && !players[currentIndex].hasFolded()){
				currentPlayer = players[currentIndex];

				//current player takes his own action based on current max stake and all public cards
				//TODO: players' actions also depend on public cards
				currentPlayer.nextAction(pot);
			}
			//if the player next to dealer is not the last one, then after this player takes actions, currentIndex increases by one
			//else, the player next to dealer is the last one in the array, currentIndex should be reassigned to zero
			if(currentIndex!=players.length-1){
				currentIndex++;
			}else {
				currentIndex=0;
			}
		}
		//TODO: dealer's actions also depend on public cards
		players[currentIndex].nextAction(pot);//this player is the dealer, which is the last one takes actions in this subround
	}
	public void flopRound(int currentIndex, PotOfMoney pot){
		subRoundHelper(currentIndex, pot);
	}
	public void turnRound(int currentIndex, PotOfMoney pot){
		subRoundHelper(currentIndex, pot);
	}
	public void riverRound(int currentIndex, PotOfMoney pot){
		subRoundHelper(currentIndex, pot);
	}
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play a round of poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void play() {
		//TODO: 1-Enter Pre-flop round, this round should start from the first player after the Dealer,
		// 		  players should check, bet, call, raise, fold, all-in(dealer can't fold, but he can check, bet, call, raise or all-in??? can dealer choose all actions???).
		// 		  After this round finished,
		// 				  if only one player call or raise, then all stakes in the pot belongs to this player, and game continue
		// 				  else stakes of all players will be added to pot, and game continue.
		// 		 **three public cards should be displayed on the table, all players need to combine cards on their hands
		// 	       with these three public cards.
		// 		2-Enter Flop round, this round should start from the first player that not fold after dealer.
		// 		  After this round finished,
		//				  if only one player call or raise, then all stakes in the pot belongs to this player, and game continue
		//				  else stakes of all players will be added to pot, and game continue.
		// 		 **another public card is displayed, this is called 'Turn Card'.
		// 		3-Enter Turn round, this round should start from the first player that not fold after dealer.
		// 		After this round finished,
		//				  if only one player call or raise, then all stakes in the pot belongs to this player, and game continue
		//				  else stakes of all players will be added to pot, and game continue.
		//		 **another public card is displayed, this is called 'River Card'.
		//		4-Enter River round, this round should start from the first player that not fold after dealer.
		//		After this round finished,
		//				  if only one player call or raise, then all stakes in the pot belongs to this player, and game continue
		//				  else stakes of all players will be added to pot, and game continue.
		//		5-Finally, if there are more than one unfolded players in the game, they have to showdown to determine the winner.


		PotOfMoney pot = new PotOfMoney();
		
		int numActive = getNumActivePlayers();
		
		int stake = -1;
		
		Player currentPlayer = null;

		int indexOfFirstPlayerAfterDealer;
		if(dealerIndex==players.length-1){
			indexOfFirstPlayerAfterDealer=0;
		}else {
			indexOfFirstPlayerAfterDealer=dealerIndex+1;
		}
		int roundCounter = 1;
		while(noWinnerProduced && roundCounter!=5){
			switch (roundCounter){
				case 1 ->{
					preFlopRound(indexOfFirstPlayerAfterDealer, pot);
					roundCounter++;
					//TODO: three public cards should be displayed on the table.
					break;
				}
				case 2 ->{
					flopRound(indexOfFirstPlayerAfterDealer, pot);
					roundCounter++;
					//TODO: turn card should be displayed on the table
					break;
				}
				case 3 ->{
					turnRound(indexOfFirstPlayerAfterDealer, pot);
					roundCounter++;
					//TODO: river card should be displayed on the table
					break;
				}
				default -> {
					riverRound(indexOfFirstPlayerAfterDealer, pot);
					roundCounter++;
					break;
				}
			}
			if(onlyOnePlayerNotFold()){
				break;
			}
		}
		//TODO: after four rounds, all not folded players will show their cards and determine who will win the game


		// while the stakes are getting bigger and there is at least one active player,
		// then continue to go around the table and play

		/*while (stake < pot.getCurrentStake() && numActive > 0) {
			stake = pot.getCurrentStake();
			
			for (int i = 0; i < getNumPlayers(); i++) {
				currentPlayer = getPlayer(i);
				
				if (currentPlayer == null || currentPlayer.hasFolded())
					continue;   
				
				delay(DELAY_BETWEEN_ACTIONS);
				
				if (numActive == 1) {
					// this must be the last player
				
					currentPlayer.takePot(pot);
					return;
				}
				else
					currentPlayer.nextAction(pot);
				
				if (currentPlayer.hasFolded()) // must have just folded
					numActive--;
			}
		}

		if (numActive == 0) {
			// no player is left in the game
			
			System.out.println("\nNo Players left in the game.\n");
			return;
		}
				
		Player bestPlayer = getPlayer(getNumBestPlayer(true));
			
		if (bestPlayer != null)
			bestPlayer.takePot(pot);*/
	}
	public boolean onlyOnePlayerNotFold(){
		int count=0;
		for(Player player: players){
			if(!player.hasFolded()){
				count++;
			}
		}
		return count == 1;
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Some small but useful helper routines
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	private void delay(int numMilliseconds) {
		try {
			Thread.sleep(numMilliseconds);
		} catch (Exception e) {}
	}
}  