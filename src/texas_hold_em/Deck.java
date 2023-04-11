package texas_hold_em;

import poker.Card;

// Deck interface for use with polymorphism

public interface Deck {
    void reset();
    void shuffle();
    Card dealNext();
    Hand dealHand();
}
