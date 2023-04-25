package texas_scramble.Deck;

import texas_scramble.Hand.HandElement;

public record Tile(String name, int value) implements HandElement {
    @Override
    public String toString() {
        if (name.length() == 0) {
            return "[BLANK]";
        }
//        return "[" + name + "]";
        return name;
    }
    @Override
    public int getValue(){
        return this.value;
    }
}