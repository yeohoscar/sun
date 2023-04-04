
package Texas_Hold_Em;
import poker.*;

import java.util.ArrayList;
// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's

public class RoundsOfTexas extends RoundController{
	private PrintGame printGame;
	private PotOfMoney pot;
	public RoundsOfTexas(DeckOfCards deck, ArrayList<TexasPlayer> texasPlayers, int dealerIndex)  {
		super(deck, texasPlayers, dealerIndex);
		this.printGame = new PrintGame(texasPlayers, deck, getPot());
		this.pot = getPot();
	}


	@Override
	public void blindBet(int dealerIndex, int sizeOfPlayers) {
		super.blindBet(dealerIndex, sizeOfPlayers);
		printGame.table("deal");
	}

	@Override
	public void roundCounter(int counter) {
			int roundCounter=counter;
			while (noWinnerProduced() && roundCounter != 5) {
				switch (roundCounter) {
					case 1 -> {
						preFlopRound(firstMovePlayerIndex(), pot);
						roundCounter++;
						printGame.table("pre-flop");
						//TODO: three public cards should be displayed on the table.
						break;
					}
					case 2 -> {
						flopRound(firstMovePlayerIndex(), pot);
						roundCounter++;
						printGame.table("flop");
						//TODO: turn card should be displayed on the table
						break;
					}
					case 3 -> {
						turnRound(firstMovePlayerIndex(), pot);
						roundCounter++;
						printGame.table("turn");
						//TODO: river card should be displayed on the table
						break;
					}
					default -> {
						riverRound(firstMovePlayerIndex(), pot);
						roundCounter++;
						printGame.table("river");
						break;
					}
				}
				printGame.table("showDown");
			}

	}

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

}  