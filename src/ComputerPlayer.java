import poker.Card;

public class ComputerPlayer extends Player {
    private Card dealerCard;
    public ComputerPlayer(String name, int money) {
        super(name, money);
    }

    // Determines what action Computer player will take

    @Override
    Action chooseAction(BlackjackHand hand) {
        if (canSplit(hand)) {
            if (shouldSplit(hand)) {
                System.out.println("Total value = "+hand.getValue());
                System.out.println("Enter split");
                return Action.SPLIT;
            }
        }
        if (hand.hasAce()) {
            System.out.println("Total value = "+hand.getValue());
            System.out.println("Enter Ace");
            return softTotalActions(hand);
        }
        return hardTotalActions(hand);
    }

    // Mutator

    public void setDealerCard(Card dealerCard) {
        this.dealerCard = dealerCard;
    }

    // Utility methods to check if action is allowed

    private boolean canSurrender(BlackjackHand hand) {
        return hand.getNumCardsInHand() == 2;
    }

    private boolean canDoubleDown(BlackjackHand hand) {
        return hand.getNumCardsInHand() == 2 && getBank() >= hand.getStake();
    }

    private boolean canSplit(BlackjackHand hand) {
        return hand.getNumCardsInHand() == 2 && hand.getCard(0).getValue() == hand.getCard(1).getValue() && getBank() >= hand.getStake();
    }

    // Methods to determine action using a Blackjack strategy table

    private boolean shouldSplit(BlackjackHand hand) {
        switch (hand.getCard(0).getValue()) {
            case 10, 5:
                return false;
            case 2, 3, 6, 7:
                if (dealerCard.getValue() >= 8 && dealerCard.getValue() <= 11 || hand.getValue() == 6 && dealerCard.getValue() == 7) {
                    return false;
                }
            case 9:
                if (dealerCard.getValue() == 7 || dealerCard.getValue() == 10 || dealerCard.getValue() == 11) {
                    return false;
                }
            case 4:
                if (dealerCard.getValue() < 5 && dealerCard.getValue() > 6) {
                    return false;
                }
        }
        return true;
    }

    private Action softTotalActions(BlackjackHand hand) {
        if(hand.getValue()<=21) {
            switch (hand.getValue() - 11) {
                case 9, 10 -> {
                    return Action.STAND;
                }
                case 8 -> {
                    if (dealerCard.getValue() == 6 && canDoubleDown(hand)) {
                        return Action.DOUBLE;
                    }
                    return Action.STAND;
                }
                case 7 -> {
                    if (dealerCard.getValue() <= 9) {
                        return Action.HIT;
                    } else if (dealerCard.getValue() >= 2 && dealerCard.getValue() <= 6 && canDoubleDown(hand)) {
                        return Action.DOUBLE;
                    }
                    return Action.STAND;
                }
                case 6 -> {
                    if (dealerCard.getValue() >= 3 && dealerCard.getValue() <= 6 && canDoubleDown(hand)) {
                        return Action.DOUBLE;
                    }
                    return Action.HIT;
                }
                case 5, 4 -> {
                    if (dealerCard.getValue() >= 4 && dealerCard.getValue() <= 6 && canDoubleDown(hand)) {
                        return Action.DOUBLE;
                    }
                    return Action.HIT;
                }
                case 3, 2 -> {
                    if (dealerCard.getValue() >= 5 && dealerCard.getValue() <= 6 && canDoubleDown(hand)) {
                        return Action.DOUBLE;
                    }
                    return Action.HIT;
                }
            }
        }else {
            return Action.STAND;
        }
        return null;
    }

    private Action hardTotalActions(BlackjackHand hand) {
        if (hand.getValue() >= 17) {
            return Action.STAND;
        }
        if (hand.getValue() == 16) {
            if (dealerCard.getValue() >= 9 && canSurrender(hand)) {
                return Action.SURRENDER;
            } else if (dealerCard.getValue() >= 2 && dealerCard.getValue() <= 6) {
                return Action.STAND;
            } else {
                return Action.HIT;
            }
        }
        if (hand.getValue() == 15) {
            if (dealerCard.getValue() == 10 && canSurrender(hand)) {
                return Action.SURRENDER;
            } else if (dealerCard.getValue() >= 2 && dealerCard.getValue() <= 6) {
                return Action.STAND;
            } else {
                return Action.HIT;
            }
        }
        if (hand.getValue() >= 13 && hand.getValue() <= 14) {
            if (dealerCard.getValue() >= 2 && dealerCard.getValue() <= 6) {
                return Action.STAND;
            } else {
                return Action.HIT;
            }
        }
        if (hand.getValue() == 12) {
            if (dealerCard.getValue() >= 4 && dealerCard.getValue() <= 6) {
                return Action.STAND;
            } else {
                return Action.HIT;
            }
        }
        if (hand.getValue() == 11) {
            if (canDoubleDown(hand)) {
                return Action.DOUBLE;
            }
            return Action.HIT;
        }
        if (hand.getValue() == 10) {
                if (dealerCard.getValue() != 10 && dealerCard.getValue() != 11) {
                if (canDoubleDown(hand)) {
                    return Action.DOUBLE;
                }
            }
            return Action.HIT;
        }
        if (hand.getValue() == 9) {
            if (dealerCard.getValue() >= 3 && dealerCard.getValue() <= 6) {
                return Action.DOUBLE;
            } else {
                if (canDoubleDown(hand)) {
                    return Action.DOUBLE;
                }
                return Action.HIT;
            }
        }
        if (hand.getValue() <= 8) {
            return Action.HIT;
        }
        return null;
    }
}
