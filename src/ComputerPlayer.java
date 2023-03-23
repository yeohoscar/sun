import poker.Card;

public class ComputerPlayer extends Player {
    private Card dealerCard;
    public ComputerPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    Action chooseAction(BlackjackHand hand) {
        if (hand.canSplit()) {
            if (hand.getCard(0).isAce() || hand.getCard(0).getName() == "Eight") {
                return Action.SPLIT;
            } else if (hand.getCard(0).isFaceCard() || hand.getCard(0).isTen()) {
                return Action.STAND;
            } else if (hand.getCard(0).getValue() == 9) {
                if (dealerCard.getValue() == 7 || dealerCard.getValue() == 10 || dealerCard.getValue() == 11) {
                    return Action.STAND;
                } else {
                    return Action.SPLIT;
                }
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
            else {
                return Action.STAND;
            }

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

    public void setDealerCard(Card dealerCard) {
        this.dealerCard = dealerCard;
    }
}
