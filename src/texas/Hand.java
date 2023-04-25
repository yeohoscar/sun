package texas;

import texas.scramble.Hand.HandElement;

// Hand interface for use with polymorphism

public interface Hand {
    int getValue();

    Hand categorize();

    int getRiskWorthiness();

    void throwaway(int cardPos);

    Hand discard();

    int getNumDiscarded();

    HandElement[] getHand();
}
