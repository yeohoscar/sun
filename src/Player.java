import poker.DeckOfCards;

abstract class Player {
    private int bank = 0;
    private int stake = 0;
    private String name = "Player";
    private BlackjackHand hand = null;

    private boolean outOfGame = false;

    public Player(String name, int money) {
        this.name = name;
        bank = money;
        reset();
    }

    public void reset() {
        outOfGame = false;

        stake  = 0;
    }

    public BlackjackHand getHand() {
        return hand;
    }

    public int getBank() {
        return bank;
    }

    public int getStake() {
        return stake;
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

    public void dealTo(DeckOfCards deck) {
        hand = deck.dealHand();
    }

    public void surrender() {
        if (!isOutOfGame()) {
            System.out.println("\n> " + getName() + " says: I surrender!\n");
        }
        outOfGame = true;
    }

    public void placeInitialBet(int minBet) {
        if (bank - minBet >= 0) return;

        stake += bet;
        bank -= bet;

        System.out.println("\n> " + getName() + " says: I bet with " + bet + " chip!\n");
    }

    abstract Action chooseAction();

    public void nextAction() {
        if (isOutOfGame()) return;

        if (isBankrupt()) {
            System.out.println("\n> " + getName() + " says: I'm out!\n");

            surrender();

            return;
        }

        switch (chooseAction()) {
            case HIT -> hit(); break;
            case SPLIT -> split(); break;
            case STAND -> stand(); break;
            case DOUBLE -> double(); break;
            case SURRENDER -> surrender(); break;
        }
    }
}
