package texas_scramble;

import poker.Card;
import texas_hold_em.Deck;
import texas_hold_em.Hand;

import java.util.Arrays;

public class ScrambleHand implements Hand {
    private Tile[] hand;  								// the actual sequence of cards

    private Deck deck; 							// the deck from which the hand is made

    public ScrambleHand(Tile[] hand, Deck deck) {
        this.hand = hand;
        this.deck = deck;
    }

    @Override
    public int getValue() {
        int value = Arrays.stream(hand).mapToInt(Tile::value).sum();
        if (hand.length == 7) value += 50;
        return value;
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
