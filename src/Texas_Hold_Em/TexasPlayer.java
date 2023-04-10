
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


import poker.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class TexasPlayer extends poker.Player {
	public final int NUM_CARDS_DEALT = 2;
	public final int NUM_CARDS_REQUIRED_FOR_FULL_HAND = 3;

	private int id;

	private Hand currentBestHand = null;

	private boolean allIn;

	protected boolean dealer = false;
	public DeckOfCards deckOfCards;
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public TexasPlayer(String name, int money,int id)	{
		super(name, money);
		this.id = id;
		allIn = false;
	}
	public void setDeck(DeckOfCards deck){
		deckOfCards=deck;
	}
	public DeckOfCards getDeckOfCards(){
		return deckOfCards;
	}

	public void smallBlind(int smallBlind,PotOfMoney pot){
		stake+=smallBlind;
		bank-=smallBlind;
		pot.raiseStake(smallBlind);
	}
	public void bigBlind(int bigBlind,PotOfMoney pot){
		stake+=bigBlind;
		bank-=bigBlind;
		pot.raiseStake(bigBlind);
	}
	//every player can act as a dealer
	public void setDealer(boolean dealer) {
		this.dealer = dealer;
	}
	public boolean isDealer(){
		return dealer;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Reset internal state for start of new hand of poker  
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void reset() {
		folded = false;
		allIn = false;
		stake  = 0;
	}

	public void resetStake() {
		stake = -1;
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

	public Hand getCurrentBestHand() {
		if (currentBestHand == null) {
			return hand;
		}
		return currentBestHand;
	}

	public boolean isAllIn() {
		return allIn;
	}

	public int getId() {
		return id;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Actions a player can make
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void allIn(PotOfMoney pot) {
		if (getBank() == 0) return;

		stake += bank;
		bank = 0;

		if (stake <= pot.getCurrentStake()*2){
			pot.addToPot(stake);
		} else {
			pot.raiseStake(stake - pot.getCurrentStake());
		}

		allIn =true;

		System.out.println("\n> " + getName() + " says: and I all in!\n");
	}

	public void winFromPot(int chips,PotOfMoney pot) {
		// when the winner of a hand takes the pot as his/her winnings

		System.out.println("\n> " + getName() + " says: I WIN " + addCount(chips, "chip", "chips") + "!\n");
		System.out.println(hand.toString());

		bank += chips;
		pot.takeFromPot(chips);
		System.out.println(this);
	}
	public void check() {
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

	public void allIn() {
		allIn = true;
	}

	public void setId(int id) {
		this.id = id;
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
			// first mover of the round

			if (shouldCheck(pot)) {
				stake = 0;
				check();
			} else if (shouldRaise(pot)) {
				stake = 0;
				raiseBet(pot);
			} else if (shouldAllIn(pot)) {
				stake = 0;
				allIn(pot);
			} else {
				fold();
			}
		} else {
			if (pot.getCurrentStake() > getStake()) {
				// existing bet must be covered	
				System.out.println("pot.getCurrentStake() in TexasPlayer = "+pot.getCurrentStake());
				if (shouldSee(pot)) {

					seeBet(pot);

					if (shouldAllIn(pot)) {
						allIn(pot);
					} else if (shouldRaise(pot))
						raiseBet(pot);
				} else
					fold();
			} else {
				if (shouldAllIn(pot)) {
					allIn(pot);
				} else {
					fold();
				}
			}
		}
	}
}