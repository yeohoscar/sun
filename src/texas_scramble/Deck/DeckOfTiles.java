package texas_scramble.Deck;

import poker.Card;
import poker.DeckOfCards;
import texas_hold_em.Deck;
import texas_hold_em.Hand;

import java.util.HashMap;
import java.util.Random;

public class DeckOfTiles implements Deck {
    public static final int NUM_TILES = 100;

    private Tile[] deck = new Tile[NUM_TILES];

    private int next = 0; // the next tile to be dealt

    private Random dice = new Random(System.currentTimeMillis());

    private HashMap<String, Integer> storeTiles = new HashMap<>();
    public DeckOfTiles() {
        //TODO: Init all tiles
        initializeTiles();
        reset();
    }
    private void initializeTiles(){
        storeTiles.put("A", 9);
        storeTiles.put("B", 2);
        storeTiles.put("C", 2);
        storeTiles.put("D", 4);
        storeTiles.put("E", 12);
        storeTiles.put("F", 2);
        storeTiles.put("G", 3);
        storeTiles.put("H", 2);
        storeTiles.put("I", 9);
        storeTiles.put("J", 1);
        storeTiles.put("K", 1);
        storeTiles.put("L", 4);
        storeTiles.put("M", 2);
        storeTiles.put("N", 6);
        storeTiles.put("O", 8);
        storeTiles.put("P", 2);
        storeTiles.put("Q", 1);
        storeTiles.put("R", 6);
        storeTiles.put("S", 4);
        storeTiles.put("T", 6);
        storeTiles.put("U", 4);
        storeTiles.put("V", 2);
        storeTiles.put("W", 2);
        storeTiles.put("X", 1);
        storeTiles.put("Y", 2);
        storeTiles.put("Z", 1);
        storeTiles.put(" ", 2);
    }
    public HashMap<String, Integer> getAllTiles(){
        return storeTiles;
    }
    @Override
    public void reset() {
        next = 0;
        shuffle();
    }

    @Override
    public void shuffle() {

    }

    @Override
    public Card dealNext() {
        return null;
    }

    @Override
    public Hand dealHand() {
        return null;
    }
}
