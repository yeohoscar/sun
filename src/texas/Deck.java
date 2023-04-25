package texas;

import texas.scramble.Hand.HandElement;

// Deck interface for use with polymorphism

public interface Deck {
    void reset();
    void shuffle();
    HandElement dealNext();
    Hand dealHand();

    Hand dealHand(int numCardsToBeDealt);
}
