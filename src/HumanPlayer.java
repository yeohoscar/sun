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



    public void placeBet(Card faceUpCard) {
        System.out.println("The face up card of dealer is: "+faceUpCard.getSuit()+" "+faceUpCard.getName());
    }

    public void notifyInvalidAction(String action, String reason) {
        System.out.print("\n>> Cannot perform <" + action + "> due to <" + reason + ">");
    }

    @Override
    Action chooseAction(BlackjackHand hand) {
        System.out.print("\n>> Pick an option: 1. Hit  2. Stand  3. Double Down  4. Split  5. Fold");

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
                        if (canDoubleDown(hand))
                            return Action.DOUBLE;
                    }
                    case '4' -> {
                        if (canSplit(hand))
                            return Action.SPLIT;
                    }
                    case '5' -> {
                        return Action.FOLD;
                    }
                    default -> {
                    }
                }

        }
        catch (Exception e){};
        return null;
    }

    private boolean canDoubleDown(BlackjackHand hand) {
        if (getBank() < hand.getStake()) {
            notifyInvalidAction("double down", "insufficient chips");
            return false;
        }
        return true;
    }

    private boolean canSplit(BlackjackHand hand) {
        if (hand.getNumCardsInHand() == 2) {
            notifyInvalidAction("split", "can only split on opening hand");
            return false;
        }
        if (hand.getCard(0) != hand.getCard(1)) {
            notifyInvalidAction("split", "cards not the same");
            return false;
        }
        if (getBank() < hand.getStake()) {
            notifyInvalidAction("split", "insufficient chips");
            return false;
        }
        return true;
    }
}
