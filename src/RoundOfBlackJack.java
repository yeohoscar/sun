public class RoundOfBlackJack {
    private DeckOfCards deck;
    private Player[] players;

    public RoundOfBlackJack(DeckOfCards deck, Player[] players) {
        this.deck = deck;
        this.players = players;
    }

    public void play() {


        //TODO initial dealer hands

        for (Player player : players) {
           if(!player.isOutOfGame()){
               switch (player.getClass().getSimpleName()) {

                   case "HumanPlayer":
                        //TODO player.placeBet();
                       break;
                   default:
                       //TODO ComputerPlayer.placeBet();
                       break;
               }
               player.dealTo(deck);
               player.dealTo(deck);
                //TODO dealer show one card
               while(player.getHand().getValue()<21){
                   switch(player.chooseAction()){
                       case HIT -> player.dealTo(deck);
                       case SPLIT -> player.split(player.hand);
                       case STAND -> {
                           break;
                       }
                       case DOUBLE -> player.dealTo(deck);player.placeBet();
                       default -> player.surrender();
                   }

               }
           }
        }
        //TODO dealer show hand

        //TODO while dealer hands value less than 17
        //dealer hit

        for (Player player : players) {
            if(!player.isOutOfGame()){
                
            }else{

            }
        }


    }
}
