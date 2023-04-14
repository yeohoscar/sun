package texas_scramble;

public class Tile {
    private final String name;

    private final int value;

    public Tile(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[" + name + "]";
    }
}
