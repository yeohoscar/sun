package texas_scramble.Deck;

import texas_scramble.Hand.HandElement;

public record Tile(String name, int value) implements HandElement {

    @Override
    public String toString() {
        return "[" + name + "]";
    }
}
