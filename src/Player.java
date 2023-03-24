import poker.Card;

import java.util.ArrayList;

abstract class Player {
    protected static final int FIRST_HAND = 0;

    protected int bank = 0;
    private String name = "Player";
    protected ArrayList<BlackjackHand> hands = new ArrayList<>();

    private boolean outOfGame = false;

    // Constructor

    public Player(String name, int money) {
        this.name = name;
        bank = money;
    }

    // Accessors

    public ArrayList<BlackjackHand> getHands() {
        return hands;
    }


    public int getBank() {
        return bank;
    }

    public String getName() {
        return name;
    }

    public boolean isBankrupt() {
        // no more money left
        boolean noStake = true;
        for (BlackjackHand hand : hands) {
            if (hand.getStake() != 0) {
                noStake = false;
                break;
            }
        }
        return bank == 0 && noStake;
    }

    public boolean isOutOfGame() {
        return outOfGame;
    }

    // Modifiers

    public void dealTo(DeckOfCards deck) {
        hands = new ArrayList<>();
        hands.add(deck.dealBlackJackHand());
    }

    public void leaveGame() {
        outOfGame = true;
    }

    public void placeBet(int bet){
        if(bet<=bank){
            hands.get(FIRST_HAND).setStake(bet);
            bank -= bet;

            System.out.println("\n> " + getName() + " says: I bet with " + bet + " chip!\n");
        }
    }

    // Handle bet payouts

    public void winBet(int bet){
        bank+=bet;
    }
    public void lossBet(int bet){
        bank-=bet;
    }


    // Method for player to pick action to do

    abstract Action chooseAction(BlackjackHand hand);

    // Actions a player can take

    boolean split(BlackjackHand hand) {
        bank -= hand.getStake();
        BlackjackHand splitHand = new BlackjackHand(hand);
        splitHand.hit(name);
        hand.hit(name);
        getHands().add(splitHand);
        return false;
    }
    boolean stand() {
        System.out.println("\n> " + getName() + " says: I stand!\n");
        return true;
    }

    // Schedule game actions

    public void takeTurn(Card dealerFaceUpCard) {
        if (isOutOfGame()) return;

        if (isBankrupt()) {
            System.out.println("\n> " + getName() + " says: I'm out!\n");
            leaveGame();
            return;
        }

        for (BlackjackHand hand : hands) {
            boolean actionCompleted = false;
            while (!(hand.isBusted() || actionCompleted) ) {
                System.out.println(">> "+name+"'s turn!");
                System.out.println("\n      Dealer's card: " + dealerFaceUpCard + hand);
                switch (chooseAction(hand)) {
                    case HIT -> {
                        actionCompleted = hand.hit(name);
                        if(hand.isBusted()){
                            System.out.println("\n      Dealer's card: " + dealerFaceUpCard + hand);
                            System.out.println("\n> " + name + " says: I busted!\n");
                        }
                    }
                    case SPLIT -> {
                        bank -= hand.getStake();
                        actionCompleted = split(hand);
                    }
                    case STAND -> {
                        actionCompleted = stand();
                    }
                    case DOUBLE -> {
                        bank-=hand.getStake();
                        actionCompleted = hand.doubleDown(name);
                    }
                    case SURRENDER -> {
                        actionCompleted = hand.surrender(name);
                    }
                    default -> {}
                }
                delay(500);
            }
        }
    }

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {}
    }
}
