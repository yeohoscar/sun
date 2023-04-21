package texas_scramble;

public record Tile(String name, int value) implements HandElement {
    @Override
    public String toString() {
        if (name.length() == 0) {
            return "[BLANK]";
        }
        return "[" + name + "]";
    }
}