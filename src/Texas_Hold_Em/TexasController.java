
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A GameOfPoker is a sequence of one or more RoundOfPoker's


import poker.DeckOfCards;

public class TexasController extends MainController
{	

	@Override
	public void setUp(String[] names, int bank) {
		super.setUp(names, bank);
		deck  = new DeckOfCards();
	}


}
