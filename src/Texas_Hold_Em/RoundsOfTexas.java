
package Texas_Hold_Em;
import poker.*;

import java.util.ArrayList;
// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's

public class RoundsOfTexas {
	public static int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
	
	private ArrayList<TexasPlayer> texasPlayers;
	private int dealerIndex;
	private DeckOfCards deck;
	private int numPlayers;
	private int smallIndex;
	private int bigIndex;
	private int numSolventPlayers;
	private int currentMaxStake;
	private PotOfMoney pot = new PotOfMoney();
	private PrintGame printGame;
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public RoundsOfTexas(DeckOfCards deck, ArrayList<TexasPlayer> texasPlayers, int dealerIndex) {
		this.deck    = deck;
		this.texasPlayers = texasPlayers;
		this.dealerIndex = dealerIndex;
		this.numSolventPlayers = numSolventPlayers;
		numPlayers = texasPlayers.size();

		//in each game, there must have small blind and big blind
		//and two players will make small blind and big blind
		System.out.println("Small Blind and Big Blind: ");
		blindBet(dealerIndex, texasPlayers.size()-1);

		System.out.println("\n\nNew Deal:\n\n");
		//after small blind and big blind, deal two cards to each player
		deal();
		this.printGame = new PrintGame(texasPlayers, deck, pot);
		printGame.table("deal");
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
		while((texasPlayers.get(smallIndex)==null && texasPlayers.get(smallIndex).hasFolded()) && (texasPlayers.get(bigIndex)==null && texasPlayers.get(bigIndex).hasFolded()) && smallIndex == bigIndex){
			if(texasPlayers.get(smallIndex)==null || texasPlayers.get(smallIndex).hasFolded()){
				smallIndex++;
			}else if(texasPlayers.get(bigIndex)==null || texasPlayers.get(bigIndex).hasFolded()){
				bigIndex++;
			}else if(smallIndex==bigIndex){
				bigIndex++;
			}
			if(smallIndex==texasPlayers.size()){
				smallIndex=0;
			}
			if(bigIndex==texasPlayers.size()){
				bigIndex=0;
			}
		}

		texasPlayers.get(smallIndex).smallBlind();
		texasPlayers.get(bigIndex).bigBlind();
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumPlayers() {
		return numPlayers;
	}
	
	
	public TexasPlayer getPlayer(int num) {
		if (num >= 0 && num <= numPlayers)
			return texasPlayers.get(num);
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
			System.out.println("\n> " + texasPlayers.get(num).getName() + " leaves the game.\n");
			
			texasPlayers.set(num, null);
		}
	}	
	

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Find the player with the best hand
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumBestPlayer(boolean display) {
		int bestHandScore = 0, score = 0, bestPos = 0;
			
		TexasPlayer bestTexasPlayer = null, currentTexasPlayer = null;
			
		for (int i = 0; i < getNumPlayers(); i++) {
			currentTexasPlayer = getPlayer(i);
				
			if (currentTexasPlayer == null || currentTexasPlayer.hasFolded())
				continue;
				
			score = currentTexasPlayer.getHand().getValue();
				
			if (score > bestHandScore) {
				if (display) {
					if (bestHandScore == 0)
						System.out.println("> " + currentTexasPlayer.getName() + " goes first:\n" +
											 currentTexasPlayer.getHand());
					else
						System.out.println("> " + currentTexasPlayer.getName() + " says 'Read them and weep:'\n" +
											 currentTexasPlayer.getHand());
				}
				
				bestPos		  = i;
				bestHandScore = score;
				bestTexasPlayer = currentTexasPlayer;
			}
			else
			if (display)
				System.out.println("> " + currentTexasPlayer.getName() + " says 'I lose':\n" +
										  currentTexasPlayer.getHand());
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
		TexasPlayer currentTexasPlayer;
		while(!texasPlayers.get(currentIndex).isDealer()){
			if(!texasPlayers.get(currentIndex).isBankrupt() && texasPlayers.get(currentIndex) != null){
				currentTexasPlayer = texasPlayers.get(currentIndex);
				//current player takes his own action
				currentTexasPlayer.nextAction(pot);
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
			if(currentIndex!= texasPlayers.size()-1){
				currentIndex++;
			}else {
				currentIndex=0;
			}
		}
		texasPlayers.get(currentIndex).nextAction(pot);//this player is the dealer, which is the last one takes actions in this subround
	}
	public void subRoundHelper(int currentIndex, PotOfMoney pot){
		TexasPlayer currentTexasPlayer;
		while(!texasPlayers.get(currentIndex).isDealer()){
			if(!texasPlayers.get(currentIndex).isBankrupt() && texasPlayers.get(currentIndex) != null && !texasPlayers.get(currentIndex).hasFolded()){
				currentTexasPlayer = texasPlayers.get(currentIndex);

				//current player takes his own action based on current max stake and all public cards
				//TODO: players' actions also depend on public cards
				currentTexasPlayer.nextAction(pot);
			}
			//if the player next to dealer is not the last one, then after this player takes actions, currentIndex increases by one
			//else, the player next to dealer is the last one in the array, currentIndex should be reassigned to zero
			if(currentIndex!= texasPlayers.size()-1){
				currentIndex++;
			}else {
				currentIndex=0;
			}
		}
		//TODO: dealer's actions also depends on public cards
		texasPlayers.get(currentIndex).nextAction(pot);//this player is the dealer, which is the last one takes actions in this subround
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



		int numActive = getNumActivePlayers();
		
		int stake = -1;
		
		TexasPlayer currentTexasPlayer = null;

		int indexOfFirstPlayerAfterDealer;
		if(dealerIndex== texasPlayers.size()-1){
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
					printGame.table("pre-flop");
					//TODO: three public cards should be displayed on the table.
					break;
				}
				case 2 ->{
					flopRound(indexOfFirstPlayerAfterDealer, pot);
					roundCounter++;
					printGame.table("flop");
					//TODO: turn card should be displayed on the table
					break;
				}
				case 3 ->{
					turnRound(indexOfFirstPlayerAfterDealer, pot);
					roundCounter++;
					printGame.table("turn");
					//TODO: river card should be displayed on the table
					break;
				}
				default -> {
					riverRound(indexOfFirstPlayerAfterDealer, pot);
					roundCounter++;
					printGame.table("river");
					break;
				}
			}
			printGame.table("showDown");
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
		for(TexasPlayer texasPlayer : texasPlayers){
			if(!texasPlayer.hasFolded()){
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