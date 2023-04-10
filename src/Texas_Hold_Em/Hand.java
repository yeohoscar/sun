package Texas_Hold_Em;

import poker.Card;

// Hand interface for use with polymorphism

public interface Hand {
    int getValue();

    Hand categorize();

    int getRiskWorthiness();

    void throwaway(int cardPos);

    Hand discard();

    int getNumDiscarded();

    Card[] getHand();
}
