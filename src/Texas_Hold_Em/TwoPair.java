
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker


public class TwoPair extends TexasHand {
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public TwoPair(Card[] hand, DeckOfCards deck) {
		super(hand, deck);
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the riskworthiness of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getRiskWorthiness() {
		return 100 - TexasHand.TWOPAIR_RISK;
	}
	

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the value of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getValue() {
		if (getCard(0).getRank() == getCard(1).getRank()) {
			if (getCard(2).getRank() == getCard(3).getRank())
				return TexasHand.TWOPAIR_VALUE + getCard(0).getValue()*100 + getCard(2).getValue()*10 + getCard(4).getValue();
			else
				return TexasHand.TWOPAIR_VALUE + getCard(0).getValue()*100 + getCard(3).getValue()*10 + getCard(2).getValue();
		}
		else
			return TexasHand.TWOPAIR_VALUE + getCard(1).getValue()*100 + getCard(3).getValue()*10 + getCard(0).getValue();
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Discard and redeal some cards
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public TexasHand discard() {
		if (getCard(0).getRank() == getCard(1).getRank()) {
			if (getCard(2).getRank() == getCard(3).getRank())
				return discard(4);
			else
				return discard(2);
		}
		else
			return discard(0);
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		return "Two Pair: " + super.toString();
	}
	
}

