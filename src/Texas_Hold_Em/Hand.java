package Texas_Hold_Em;

public interface Hand {
    int getValue();

    Hand categorize();

    int getRiskWorthiness();

    void throwaway(int cardPos);

    Hand discard();

    int getNumDiscarded();
}
