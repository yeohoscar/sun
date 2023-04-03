
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


import poker.DeckOfCards;
import poker.PotOfMoney;

abstract class TexasPlayer extends poker.Player {
	public final int NUM_CARDS_DEALT = 2;

	private boolean dealer = false;
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public TexasPlayer(String name, int money)	{
		super(name, money);
	}

	//every player can act as a dealer
	public void setDealer(boolean dealer) {
		this.dealer = dealer;
	}

	public boolean isDealer(){
		return dealer;
	}
	public void smallBlind(){

	}
	public void bigBlind(){

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

		stake += bank;
		bank = 0;

		pot.raiseStake(stake);

		System.out.println("\n> " + getName() + " says: and I all in!\n");
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
			else
				if (shouldCheck(pot)) {
					check(pot);
				} else {
					fold();
				}
		}
		else {
			if (pot.getCurrentStake() > getStake()) {
				// existing bet must be covered	
			
				if (shouldSee(pot)) {
					seeBet(pot);

					if (shouldAllIn(pot)) {
						allIn(pot);
					} else if (shouldRaise(pot))
						raiseBet(pot);
				}
				else
					fold();
			}
		}
	}
}