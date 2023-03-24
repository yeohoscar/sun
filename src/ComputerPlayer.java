import poker.Card;

public class ComputerPlayer extends Player {
    private Card dealerCard;
    public ComputerPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    Action chooseAction(BlackjackHand hand) {
        if (hand.canSplit()) {
            if (shouldSplit(hand)) {
                return Action.SPLIT;
            }
        }
        if (hand.hasAce()) {

            int value = this.hand.get(0).getValue();
            if(value>21){
                //this is the case when A = 11, and computer player bust, make A = 1 to avoid bust
                value = value-10;
            }
            //else we keep the default value A = 11


            if(value==12){
                if(dealerCard.getValue()>=4 && dealerCard.getValue()<=6){
                   return Action.STAND;
                }
                else {
                    return Action.HIT;
                }
            }
            else if(value>=13 && value<=16){
                if(dealerCard.getValue()>=2 && dealerCard.getValue()<=6){
                    return Action.STAND;
                }else {
                    return Action.HIT;
                }
            }
            else if(value==11){
                if(dealerCard.getName()=="Ace"){
                    return Action.HIT;
                }
                else {
                    return Action.DOUBLE;
                }
            }
            else {
                return Action.STAND;
            }
        }

        if (hand.getValue() == 17) {
            return Action.STAND;
        }
        if (hand.getValue() == 16) {
            if (dealerCard.getValue() == 9 || dealerCard.getValue() == 10 || dealerCard.getName() == "Ace") {
                return Action.FOLD;
            } else if (dealerCard.getValue() >= 2 && dealerCard.getValue() <= 6) {
                return Action.STAND;
            } else {
                return Action.HIT;
            }
        }
        if (hand.getValue() == 15) {
            if (dealerCard.getValue() == 10) {
                return Action.FOLD;
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
            return Action.DOUBLE;
        }
        if (hand.getValue() == 10) {
            if (dealerCard.getValue() == 10 || dealerCard.getName() == "Ace") {
                return Action.HIT;
            } else {
                return Action.DOUBLE;
            }
        }
        if (hand.getValue() == 9) {
            if (dealerCard.getValue() >= 3 && dealerCard.getValue() <= 6) {
                return Action.DOUBLE;
            } else {
                return Action.HIT;
            }
        }
        if (hand.getValue() == 8) {
            return Action.HIT;
        }

        /**
         * If two cards equal
         *      if cards == ace or 8
         *          split
         *      else if cards == 10
         *          stand
         *      else if cards ==
         *
         * If ace is in hand
         *      if rest_total == 2 || rest_total == 3
         *          if dealerHand.getFaceUp() == 5 || dealerHand.getFaceUp() == 6
         *           double down
         *          else hit
         *      else if rest_total == 4 || == 5
         *          if dealerHand.getFaceUp() >= 4 || dealerHand.getFaceUp() <= 6
         *              double down
         *          else hit
         *      else if rest_total == 6
         *          if dealerHand.getFaceUp() >= 3 || dealerHand.getFaceUp() <= 6
         *              double down
         *          else hit
         *      else if rest_total == 7
         *          if dealerHand.getFaceUp() >= 3 || dealerHand.getFaceUp() <= 6
         *              double down
         *          else if dealerHand.getFaceUp() == 2 || dealerHand.getFaceUp() == 7 || dealerHand.getFceUp() == 8
         *              stand
         *          else hit
         *      else stand
         * else
         *
         */
        return null;
    }

    private boolean shouldSplit(BlackjackHand hand) {
        if (hand.getCard(0).isAce() || hand.getCard(0).getValue() == 8) {
            return true;
        } else if (hand.getCard(0).getValue() == 10 || hand.getCard(0).getValue() == 5) {
            return false;
        } else if (hand.getCard(0).getValue() == 9) {
            return dealerCard.getValue() != 7 && dealerCard.getValue() != 10 && dealerCard.getValue() != 11;
        } else if (hand.getCard(0).getValue() == 7 || hand.getCard(0).getValue() == 3 || hand.getCard(0).getValue() == 2) {
            return dealerCard.getValue() >= 2 && dealerCard.getValue() <= 7;
        } else if (hand.getCard(0).getValue() == 6) {
            return dealerCard.getValue() >= 2 && dealerCard.getValue() <= 6;
        } else {
            return dealerCard.getValue() == 5 || dealerCard.getValue() == 6;
        }
    }

    public void setDealerCard(Card dealerCard) {
        this.dealerCard = dealerCard;
    }
}
