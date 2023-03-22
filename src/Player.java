abstract class                                   Player {
    public static final int FIRST_HAND = 0;

    private int bank = 0;
    private int stake = 0;
    private String name = "Player";
    protected BlackjackHand hand[] = null;

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

    public BlackjackHand[] getHand() {
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
        hand[FIRST_HAND] = deck.dealBlackJackHand();
    }

    public void surrender() {
        if (!isOutOfGame()) {
            System.out.println("\n> " + getName() + " says: I surrender!\n");
        }
        outOfGame = true;
    }

    public void placeBet(int bet) {
        if (bank - bet >= 0) return;

        stake += bet;
        bank -= bet;

        System.out.println("\n> " + getName() + " says: I bet with " + bet + " chip!\n");
    }

    abstract Action chooseAction();

    abstract boolean hit(BlackjackHand hand);
    abstract boolean split(BlackjackHand hand);
    abstract boolean stand(BlackjackHand hand);
    abstract boolean doubleDown(BlackjackHand hand);

    public void takeTurn() {
        if (isOutOfGame()) return;

        if (isBankrupt()) {
            System.out.println("\n> " + getName() + " says: I'm out!\n");

            surrender();

            return;
        }

        for (BlackjackHand hand : hand) {
            // TODO: action loop check if busted or stand
            boolean actionCompleted = false;
            while (!isOutOfGame() || !actionCompleted) {
                switch (chooseAction()) {
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
                    case SURRENDER -> surrender();
                }
            }
        }
    }
}
