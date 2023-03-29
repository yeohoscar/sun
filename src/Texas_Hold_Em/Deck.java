package Texas_Hold_Em;

import poker.Card;

public interface Deck {
    void reset();
    void shuffle();
    Card dealNext();
    Hand dealHand();
}
