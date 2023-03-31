package Texas_Hold_Em;

public interface Hand {
    int getValue();

    Hand categorize();

    int getRiskWorthiness();
}
