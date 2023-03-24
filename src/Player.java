import poker.Card;

import java.util.ArrayList;

abstract class Player {
    protected static final int FIRST_HAND = 0;
    private static final int MAX_HAND_VALUE = 21;

    protected int bank = 0;
    private String name = "Player";
    protected ArrayList<BlackjackHand> hands = new ArrayList<>();

    private boolean outOfGame = false;

    private boolean surrendered = false;
    private boolean busted = false;


    // Constructor

    public Player(String name, int money) {
        this.name = name;
        bank = money;
        reset();
    }

    // Reset internal state for each round of Blackjack

    public void reset() {
        surrendered = false;
        busted = false;
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

    public boolean hasSurrendered() {
        return surrendered;
    }

    public boolean hasBusted() {
        return busted;
    }

    // Modifiers

    public void dealTo(DeckOfCards deck) {
        hands.add(deck.dealBlackJackHand());
    }

    public void leaveGame() {
        outOfGame = true;
    }

    public boolean isBusted(BlackjackHand hand) {
        if (hand.getValue() > MAX_HAND_VALUE) {
            System.out.println(hand);
            System.out.println("\n> " + getName() + " says: I bust!\n");
            busted = true;
        }
        return false;
    }

    public void placeBet(int bet){
        if(bet<=bank){
            hands.get(FIRST_HAND).setStake(bet);
            bank -= bet;

            System.out.println("\n> " + getName() + " says: I bet with " + bet + " chip!\n");
        }
    }
//    public void placeBet(int bet) {
//        Scanner input = new Scanner(System.in);
//        while (bet <= 0 || bet > bank) {
//            System.out.print("Enter your bet amount (you have " + bank + " chips): ");
//            bet = input.nextInt();
//            if (bet <= 0) {
//                System.out.println("Invalid bet amount! Please enter a positive value.");
//            } else if (bet > bank) {
//                System.out.println("You don't have enough chips! Please enter a smaller value.");
//            }
//            else {
//                System.out.println("The bet must be a number!!");
//            }
//        }
//
//        hand.get(FIRST_HAND).setStake(bet);
//        bank -= bet;
//
//        System.out.println("\n> " + getName() + " says: I bet with " + bet + " chip!\n");
//    }






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
        getHands().add(splitHand);
        return false;
    }
    boolean stand(BlackjackHand hand) {
        System.out.println("\n> " + getName() + " says: I stand!\n");
        return true;
    }
    boolean doubleDown(BlackjackHand hand) {
        if(hand.getStake()>bank){
            System.out.println("No enough bet for double!");
            return false;
        }else {
            System.out.println("\n> " + getName() + " says: I double down!\n");
            hand.setStake(hand.getStake()*2);
            hand.addCard();
            isBusted(hand);
            return true;
        }
    }

    boolean surrender() {
        surrendered = true;
        return true;
    }

    public void takeTurn(Card dealerFaceUpCard) {
        if (isOutOfGame()) return;

        if (isBankrupt()) {
            System.out.println("\n> " + getName() + " says: I'm out!\n");
            leaveGame();
            return;
        }

        for (BlackjackHand hand : hands) {
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
                    case SURRENDER -> {
                        actionCompleted = surrender();
                    }
                }
            }
        }
    }
}
