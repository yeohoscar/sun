import poker.Card;
import poker.DeckOfCards;

public class BlackjackHand {
    public static final int MAX_NUM_CARDS = 5;
    public static final int NUM_CARDS_DEALT = 2;
    private Card[] hand;
    private DeckOfCards deck;

    public BlackjackHand(Card[] hand, DeckOfCards deck) {
        this.hand = hand;
        this.deck = deck;
    }

    public BlackjackHand(DeckOfCards deck) {
        this.deck = deck;
        hand = new Card[MAX_NUM_CARDS];

        for (int i = 0; i < NUM_CARDS_DEALT; i++) {
            setCard(i, deck.dealNext());
        }
    }
}
