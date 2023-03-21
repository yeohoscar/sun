public class HumanPlayer extends Player {
    public HumanPlayer(String name, int bank) {
        super(name, bank);
    }

    public boolean askQuestion(String question) 	{
        System.out.print("\n>> " + question + " (y/n)?  ");

        byte[] input = new byte[100];

        try {
            System.in.read(input);

            for (int i = 0; i < input.length; i++)
                if ((char)input[i] == 'y' || (char)input[i] == 'Y')
                    return true;
        }
        catch (Exception e){};

        return false;
    }

    public void notifyInvalidAction(String action, String reason) {
        System.out.print("\n>> Cannot perform <" + action + "> due to <" + reason + ">");
    }

    @Override
    Action chooseAction() {
        System.out.print("\n>> Pick an option: 1. Hit  2. Stand  3. Double Down  4. Split  5. Surrender");

        byte[] input = new byte[100];

        try {
            System.in.read(input);

            for (int i = 0; i < input.length; i++)
                switch (((char) input[i])) {
                    case '1' -> {
                        return Action.HIT;
                    }
                    case '2' -> {
                        return Action.STAND;
                    }
                    case '3' -> {
                        return Action.DOUBLE;
                    }
                    case '4' -> {
                        return Action.SPLIT;
                    }
                    case '5' -> {
                        return Action.SURRENDER;
                    }
                    default -> {
                    }
                }

        }
        catch (Exception e){};
        return null;
    }

    @Override
    boolean hit(BlackjackHand hand) {
        System.out.println("\n> " + getName() + " says: I hit!\n");
        hand.addCard();
        if (hand.getValue() > 21) {
            surrender();
            return true;
        }
        return false;
    }

    @Override
    boolean split(BlackjackHand hand) {
        if (hand.getCard(0) != hand.getCard(1)) {
            notifyInvalidAction("split", "cards not the same");
            return false;
        }
    }

    @Override
    boolean stand(BlackjackHand hand) {
        System.out.println("\n> " + getName() + " says: I stand!\n");
        return true;
    }

    @Override
    boolean doubleDown(BlackjackHand hand) {
        if (getBank() < getStake()) {
            notifyInvalidAction("double down", "insufficient chips");
            return false;
        }
        System.out.println("\n> " + getName() + " says: I double down!\n");
        placeBet(getStake());
        hand.addCard();
        if (hand.getValue() > 21) {
            surrender();
        }
        return true;
    }
}
