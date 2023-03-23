public class RoundOfBlackJack {
    private DeckOfCards deck;
    private Player[] players;

    public RoundOfBlackJack(DeckOfCards deck, Player[] players) {
        this.deck = deck;
        this.players = players;
    }

    public void play() {


        Player dealer = players[players.length - 1];
        dealer.dealTo(deck);
        dealer.takeTurn();

        for (Player player : players) {
           if(!player.isBankrupt()&&player.getClass().getSimpleName()!="DealerPlayer"){
               switch (player.getClass().getSimpleName()) {
                   case "HumanPlayer":
                        //TODO player.placeBet();
                       break;
                   case "ComputerPlayer":
                       //TODO player.placeBet();
                       break;
                   default:
                       break;
               }
               player.takeTurn();



               if(dealer.hasBusted()){

               }

               for(BlackjackHand hand : player.getHand()){
                   //TODO not sure when spilt
                   if(player.hasBusted()){
                       dealer.winBet(hand.getStake());

                   }else if(dealer.hasBusted()||hand.getValue()>dealer.getHand().get(0).getValue()){
                       //player win
                       player.winBet(hand.getStake()*2);
                       dealer.lossBet(hand.getStake());

                   }else if(hand.getValue()==dealer.getHand().get(0).getValue()){
                       //draw
                       player.winBet(hand.getStake());
                   }else{
                       //dealer win
                       dealer.winBet(hand.getStake());
                   }
               }
           }
        }




    }
}
