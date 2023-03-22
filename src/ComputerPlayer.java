public class ComputerPlayer extends Player {
    public ComputerPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    Action chooseAction(BlackjackHand dealerHand) {
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
         *              double down
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
    }
}
