package texas_scramble.Deck;

import poker.Card;
import texas_hold_em.Deck;
import texas_hold_em.Hand;

import java.util.Random;

public class DeckOfTiles implements Deck {
    public static final int NUM_TILES = 100;

    private Tile[] deck = new Tile[NUM_TILES];

    private int next = 0; // the next tile to be dealt

    private Random dice = new Random(System.currentTimeMillis());

    public DeckOfTiles() {
        //TODO: Init all tiles
        reset();
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
