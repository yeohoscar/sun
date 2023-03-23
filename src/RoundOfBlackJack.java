import poker.Card;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RoundOfBlackJack {
    private DeckOfCards deck;
    private Player[] players;

    public RoundOfBlackJack(DeckOfCards deck, Player[] players) {
        this.deck = deck;
        this.players = players;
    }

    public void play() {


        DealerPlayer dealer = (DealerPlayer) players[players.length - 1];
        dealer.dealTo(deck);
        dealer.takeTurn();
        Card faceUpCard = dealer.showOneCard();

        for (Player player : players) {
           if(!player.isBankrupt()&&player.getClass().getSimpleName()!="DealerPlayer"){
               switch (player.getClass().getSimpleName()) {
                   case "HumanPlayer":
                        //TODO player.placeBet();
                       System.out.println("How many bets do you want to place?");
                       Scanner scanner = new Scanner(System.in);

                       int numOfBets = 0;
                       try {
                           numOfBets = scanner.nextInt();
                       } catch (InputMismatchException e) {
                           System.out.println("Invalid input. Please enter a valid integer value.");
                       }
                       player.placeBet(numOfBets);

                       break;
                   case "ComputerPlayer":
                       //TODO player.placeBet();
                       player.placeBet(2);
                       break;
                   default:
                       break;
               }
               player.takeTurn();
               if(dealer.hasBusted()){

               }

               for(BlackjackHand hand : player.getHand())
                   if(hand.getValue()>21){
                       //player busted
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
