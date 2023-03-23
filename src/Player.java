import poker.Card;

import java.util.ArrayList;

abstract class Player {
    private static final int FIRST_HAND = 0;
    private static final int MAX_HAND_VALUE = 21;

    private int bank = 0;
    private String name = "Player";
    protected ArrayList<BlackjackHand> hand = new ArrayList<>();

    private boolean outOfGame = false;

    private boolean folded = false;
    private boolean busted = false;

    public Player(String name, int money) {
        this.name = name;
        bank = money;
        reset();
    }

    public void reset() {
        folded = false;
        busted = false;
    }

    public ArrayList<BlackjackHand> getHand() {
        return hand;
    }

    public void showHand(){
        System.out.print(name+"'s hand: ");
        for(BlackjackHand hand : getHand()){
            for(Card card:hand.getHand()){
                System.out.print(card.getSuit()+" "+card.getName()+";");
            }
            System.out.println();
        }
        System.out.println();
    }

    public int getBank() {
        return bank;
    }

    public String getName() {
        return name;
    }

    public boolean isBankrupt() {
        // no more money left

        return bank == 0;
    }

    public boolean isOutOfGame() {
        return outOfGame;
    }

    public boolean hasFolded() {
        return folded;
    }

    public boolean hasBusted() {
        return busted;
    }

    public void dealTo(DeckOfCards deck) {
        hand.add(deck.dealBlackJackHand());
    }

    public void leaveGame() {
        outOfGame = true;
    }

    public boolean isBusted(BlackjackHand hand) {
        if (hand.getValue() > MAX_HAND_VALUE) {
            System.out.println("\n> " + getName() + " says: I bust!\n");
            busted = true;
        }
        return false;
    }

    public void placeBet(int bet) {
        if (bank - bet <= 0) return;

        hand.get(FIRST_HAND).setStake(bet);
        bank -= bet;

        System.out.println("\n> " + getName() + " says: I bet with " + bet + " chip!\n");
    }






    public void winBet(int bet){
        bank+=bet;
    }
    public void lossBet(int bet){
        bank-=bet;
    }




    abstract Action chooseAction(BlackjackHand hand);

    boolean hit(BlackjackHand hand) {
        System.out.println("\n> " + getName() + " says: I hit!\n");
        hand.addCard();
        return isBusted(hand);
    }
    boolean split(BlackjackHand hand) {
        bank -= hand.getStake();
        BlackjackHand splitHand = new BlackjackHand(hand);
        hit(splitHand);
        hit(hand);
        getHand().add(splitHand);
        return false;
    }
    boolean stand(BlackjackHand hand) {
        System.out.println("\n> " + getName() + " says: I stand!\n");
        return true;
    }
    boolean doubleDown(BlackjackHand hand) {
        System.out.println("\n> " + getName() + " says: I double down!\n");
        placeBet(hand.getStake());
        hand.addCard();
        isBusted(hand);
        return true;
    }

    boolean fold() {
        folded = true;
        return true;
    }

    public void takeTurn(Card dealerFaceUpCard) {
        if (isOutOfGame()) return;

        if (isBankrupt()) {
            System.out.println("\n> " + getName() + " says: I'm out!\n");
            leaveGame();
            return;
        }

        for (BlackjackHand hand : hand) {
            boolean actionCompleted = false;
            while (!hasBusted() && !actionCompleted) {
                System.out.println("\n      Dealer's card: " + dealerFaceUpCard + hand.toString());
                switch (chooseAction(hand)) {
                    case HIT -> {
                        actionCompleted = hit(hand);
                    }
                    case SPLIT -> {
                        actionCompleted = split(hand);
                    }
                    case STAND -> {
                        actionCompleted = stand(hand);
                    }
                    case DOUBLE -> {
                        actionCompleted = doubleDown(hand);
                    }
                    case FOLD -> {
                        actionCompleted = fold();
                    }
                }
            }
        }
    }
}
