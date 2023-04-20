package texas_scramble.Hand;

import poker.Card;
import poker.DeckOfCards;
import texas_hold_em.Hand;
import texas_scramble.Deck.Tile;

import java.util.Arrays;

public class ScrambleHand implements Hand {
    private Tile[] hand;  								// the actual sequence of cards

    private DeckOfCards deck; 							// the deck from which the hand is made

    private int discarded = 0; 	// the number of cards already discarded and redealt in this hand

    @Override
    public int getValue() {
        return Arrays.stream(hand).mapToInt(Tile::value).sum();
    }

    @Override
    public Hand categorize() {
        return null;
    }

    @Override
    public int getRiskWorthiness() {
        return 0;
    }

    @Override
    public Card[] getHand() {
        return new Card[0];
    }

    @Override
    public void throwaway(int cardPos) {

    }

    @Override
    public Hand discard() {
        return null;
    }

    @Override
    public int getNumDiscarded() {
        return 0;
    }
}
