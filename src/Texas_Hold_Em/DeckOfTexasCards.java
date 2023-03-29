package Texas_Hold_Em;

import poker.Card;
import poker.FaceCard;
import poker.NumberCard;
import poker.PokerHand;

import java.util.Random;

public class DeckOfTexasCards implements Deck {
    public static final String[] suits 	= {"hearts", "diamonds", "clubs", "spades"};

    public static final int NUMCARDS 	= 52;  // number of cards in a deck


    private Card[] deck 				= new Card[NUMCARDS];  // the actual sequence of cards

    private int next 					= 0; // the next card to be dealt

    private Random dice     			= new Random(System.currentTimeMillis());

    public DeckOfTexasCards() {
        for (int i = 0; i < suits.length; i++) {
            deck[next++] = new NumberCard("Ace", suits[i], 1, 14);
            deck[next++] = new NumberCard("Deuce", suits[i], 2);
            deck[next++] = new NumberCard("Three", suits[i], 3);
            deck[next++] = new NumberCard("Four", suits[i], 4);
            deck[next++] = new NumberCard("Five", suits[i], 5);
            deck[next++] = new NumberCard("Six", suits[i], 6);
            deck[next++] = new NumberCard("Seven", suits[i], 7);
            deck[next++] = new NumberCard("Eight", suits[i], 8);
            deck[next++] = new NumberCard("Nine", suits[i], 9);
            deck[next++] = new NumberCard("Ten", suits[i], 10);
            deck[next++] = new FaceCard("Jack", suits[i], 11);
            deck[next++] = new FaceCard("Queen", suits[i], 12);
            deck[next++] = new FaceCard("King", suits[i], 13);
        }

        reset();
    }

    @Override
    public void reset() {
        next = 0;

        shuffle();
    }

    @Override
    public void shuffle() {
        Card palm = null;

        int alpha = 0, beta = 0;

        for (int i = 0; i < NUMCARDS*NUMCARDS; i++) {
            alpha       = Math.abs(dice.nextInt())%NUMCARDS;
            beta        = Math.abs(dice.nextInt())%NUMCARDS;

            palm        = deck[alpha];
            deck[alpha] = deck[beta];
            deck[beta]  = palm;
        }
    }

    @Override
    public Card dealNext() {
        if (next >= NUMCARDS)
            return new FaceCard("Joker", "no suit", 0);  // deck is empty
        else
            return deck[next++];
    }

    @Override
    public Hand dealHand() {
        Hand hand = new TexasHand(this);

        return hand.categorize();
    }
}
