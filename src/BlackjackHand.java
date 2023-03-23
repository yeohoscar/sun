import poker.Card;

public class BlackjackHand {
    public static final int MAX_NUM_CARDS = 5;
    public static final int NUM_CARDS_DEALT = 2;
    private Card[] hand;
    private DeckOfCards deck;

    private int stake = 0;
    private int numCardsInHand = NUM_CARDS_DEALT;

    // Constructors

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

    public BlackjackHand(BlackjackHand hand) {
        this.deck = hand.deck;
        this.hand = new Card[MAX_NUM_CARDS];
        this.stake = hand.stake;
        this.numCardsInHand = hand.numCardsInHand - 1;
        setCard(0, hand.getCard(0));
        hand.setNumCardsInHand(numCardsInHand - 1);
    }

    // Display hand

    public String toString() {
        String desc = "\n      Stake: " + stake;

        for (int i = 0; i < numCardsInHand; i++)
            desc = desc + "\n      " + i + ":  " + getCard(i).toString();

        return desc + "\n";
    }

    // Utility method to deal a card to hand

    public void addCard() {
        if (numCardsInHand == 5) return;
        setCard(numCardsInHand, deck.dealNext());
        numCardsInHand++;
    }

    // Modifier

    public void setCard(int num, Card card) {
        if (num >= 0 && num <= numCardsInHand)
            hand[num] = card;
    }

    public void setStake(int stake) {
        this.stake = stake;
    }

    public void setNumCardsInHand(int numCards) {
        numCardsInHand = numCards;
    }


    // Accessor

    public Card getCard(int num) {
        if (num >= 0 && num < numCardsInHand)
            return hand[num];
        else
            return null;
    }

    public int getStake() {
        return stake;
    }

    public int getNumCardsInHand() {
        return numCardsInHand;
    }

    // Calculates value of hand

    public int getValue() {
        int i = 0, total = 0;
        while (i < 5 && hand[i] != null) {
            total += hand[i].getValue();
            i++;
        }
        return total;
    }

    public boolean canSplit() {
        return hand[0] == hand[1];
    }

    public Card[] getHand(){
        return hand;
    }

    public boolean hasAce() {
        for (int i = 0; i < numCardsInHand; i++) {
            if (hand[i].isAce()) {
                return true;
            }
        }
        return false;
    }
}
