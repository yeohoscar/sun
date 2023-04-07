
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


import poker.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

abstract class TexasPlayer extends poker.Player {
	private int bank       		= 0;		 // the total amount of money the player has left, not counting his/her
									    	 // stake in the pot
	private int smallBlind = 1;

	private int bigBlind = 2*smallBlind;
	private int stake      		= 0;		 // the amount of money the player has thrown into the current pot 
	
	private String name    		= "Player";  // the unique identifying name given to the player
	
	private Hand hand 		= null;      // the hand dealt to this player
	
	private boolean folded 		= false;     // set to true when the player folds (gives up)

	public final int NUM_CARDS_DEALT = 2;
	public final int NUM_CARDS_REQUIRED_FOR_FULL_HAND = 3;

	private Hand currentBestHand = null;


	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public TexasPlayer(String name, int money)	{
		super(name, money);
	}





	public boolean suitsOnHandAreSame(Card[] hand){
		return hand[0].getSuit().equals(hand[1].getSuit());
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Reset internal state for start of new hand of poker  
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void reset() {
		folded = false;
		
		stake  = 0;
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display Behaviour 
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		if (hasFolded())
			return "> " + getName() + " has folded, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
		else
			return "> " + getName() + " has  " + addCount(getBank(), "chip", "chips") + " in the bank";
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public Hand getHand() {
		return hand;
	}

    public void allIn(PotOfMoney pot) {
		if (getBank() == 0) return;
		if(bank<=pot.getCurrentStake()){
			pot.addToPot(stake);
		}else{
			pot.raiseStake(bank-pot.getCurrentStake());
		}
		stake += bank;
		bank = 0;
		allin=true;
		System.out.println("\n> " + getName() + " says: and I all in!\n");

	}

	public Hand getCurrentBestHand() {
		return currentBestHand;
	}

	public void check(PotOfMoney pot) {
		System.out.println("\n> " + getName() + " says: I check!\n");
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Modifiers
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	@Override
	public void dealTo(DeckOfCards deck) {
		hand = deck.dealHand(NUM_CARDS_DEALT);
	}

	// Computes best hand using player's hole cards and the public cards

	public void findBestHand(Card[] publicCards, DeckOfCards deck) {
		if (publicCards.length < NUM_CARDS_REQUIRED_FOR_FULL_HAND) {
			return;
		}

		Card[] data = new Card[5];

		int aLen = hand.getHand().length;
		int bLen = publicCards.length;

		Card[] result = new Card[aLen + bLen];
		System.arraycopy(hand.getHand(), 0, result, 0, aLen);
		System.arraycopy(publicCards, 0, result, aLen, bLen);

		List<Hand> hands = new ArrayList<>();
		combinationUtil(result, data, 0, result.length - 1, 0, 5, hands, deck);

		Hand bestHand = hands.get(0);
		for (Hand hand : hands) {
			if (bestHand.getValue() < hand.getValue()) {
				bestHand = hand;
			}
		}

		currentBestHand = bestHand;
	}

	// Utility recursive function for generating all combinations of cards and adding to list

	private void combinationUtil(Card[] arr, Card[] data, int start, int end, int index, int r, List<Hand> hands, DeckOfCards deck) {
		// data combines hand with 5 cards
		if (index == r) {
			Hand newHand = new PokerHand(Arrays.copyOf(data, data.length), deck);
			newHand = newHand.categorize();
			hands.add(newHand);
			return;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			combinationUtil(arr, data, i+1, end, index+1, r, hands, deck);
		}
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Key decisions a player must make
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

    abstract boolean shouldAllIn(PotOfMoney pot);

    abstract boolean shouldCheck(PotOfMoney pot);

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Game actions are scheduled here
	//--------------------------------------------------------------------//
	//---------------------------------------------------------------------//
	
	public void nextAction(PotOfMoney pot) {
		if (hasFolded()) return;  // no longer in the game

		if (isBankrupt() || pot.getCurrentStake() - getStake() > getBank()) {
			// not enough money to cover the bet

			System.out.println("\n> " + getName() + " says: I'm out!\n");

			fold();

			return;
		}

		if (pot.getCurrentStake() == 0) {
			// first mover of the game

			if (shouldOpen(pot))  // will this player open the betting?
				openBetting(pot);
			else if (shouldCheck(pot)) {
				check(pot);
			} else {
				fold();
			}
		} else {
			if (pot.getCurrentStake() > getStake()) {
				// existing bet must be covered	

				if (shouldSee(pot)) {
					seeBet(pot);

					if (shouldAllIn(pot)) {
						allIn(pot);
					} else if (shouldRaise(pot))
						raiseBet(pot);
				} else
					fold();
			}
		}
	}
}