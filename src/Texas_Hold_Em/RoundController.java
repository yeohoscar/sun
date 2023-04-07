package Texas_Hold_Em;
import poker.*;

import java.util.ArrayList;

public abstract class RoundController {
    public static int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
    protected ArrayList<? extends Player> roundPlayers;
    private int dealerIndex;
    protected DeckOfCards deck;
    protected int numPlayers;
    private int smallIndex;
    private int bigIndex;
    private int bigBlindAmount;
    protected Hand communityCards;

    protected PotOfMoney pot;


    public RoundController(DeckOfCards deck, ArrayList<? extends Player> players, int dealerIndex) {
        this.deck = deck;
        this.roundPlayers = players;
        this.dealerIndex = dealerIndex;
        numPlayers = roundPlayers.size();
        this.bigBlindAmount=10;
    }


    public void play() {
        //TODO: 1-Enter Pre-flop round, this round should start from the first player after the Dealer,
        // 		  players should check, bet, call, raise, fold, all-in.
        // 		  After this round finished,
        // 				  if only one player call or raise, then all stakes in the pot belongs to this player, and round finish
        // 				  else stakes of all players will be added to pot, and game continue.
        // 		 **three public cards should be displayed on the table, all players need to combine cards on their hands
        // 	       with these three public cards.
        // 		2-Enter Flop round, this round should start from the first player that not fold after dealer.
        // 		  After this round finished,
        //				  if only one player call or raise, then all stakes in the pot belongs to this player, and game continue
        //				  else stakes of all players will be added to pot, and game continue.
        // 		 **another public card is displayed, this is called 'Turn Card'.
        // 		3-Enter Turn round, this round should start from the first player that not fold after dealer.
        // 		After this round finished,
        //				  if only one player call or raise, then all stakes in the pot belongs to this player, and game continue
        //				  else stakes of all players will be added to pot, and game continue.
        //		 **another public card is displayed, this is called 'River Card'.
        //		4-Enter River round, this round should start from the first player that not fold after dealer.
        //		After this round finished,
        //				  if only one player call or raise, then all stakes in the pot belongs to this player, and game continue
        //				  else stakes of all players will be added to pot, and game continue.
        //		5-Finally, if there are more than one unfolded players in the game, they have to showdown to determine the winner.


        roundCounter(1);



        //TODO decided who win
        showDown();
        //TODO remove player who all-in and loss or has less than big blind chips
        removePlayer();
    }


    public void blindBet(){
        System.out.println("Small Blind and Big Blind: ");
        System.out.println("\n\nNew Deal:\n\n");

        if(dealerIndex==(numPlayers-2)){
            smallIndex = numPlayers-1;
            bigIndex = 0;
        }else if(dealerIndex==numPlayers-1){
            smallIndex = 0;
            bigIndex = 1;
        }else {
            smallIndex = dealerIndex+1;
            bigIndex = dealerIndex+2;
        }
        roundPlayers.get(smallIndex).smallBlind(bigBlindAmount/2,pot);
        roundPlayers.get(bigIndex).bigBlind(bigBlindAmount,pot);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean onePlayerLeft(){
        int counter=0;
        for(Player player : roundPlayers){
            if(player.hasFolded()){
                counter++;
            }
        }
        if(counter==numPlayers-1){
            return true;
        }else {
            return false;
        }
    }

    public void roundCounter(int counter){
        int roundCounter=counter;
        while (!onePlayerLeft() && roundCounter != 5) {
            switch (roundCounter) {
                case 1 -> {
                    preFlopRound();
                    roundCounter++;
                    //printGame.table("pre-flop");
                    //TODO: three public cards should be displayed on the table.
                    break;
                }
                case 2 -> {
                    flopRound();
                    roundCounter++;
                    //printGame.table("flop");
                    //TODO: turn card should be displayed on the table
                    break;
                }
                case 3 -> {
                    turnRound();
                    roundCounter++;
                    //printGame.table("turn");
                    //TODO: river card should be displayed on the table
                    break;
                }
                default -> {
                    riverRound();
                    roundCounter++;
                    //printGame.table("river");
                    break;
                }
            }
            //printGame.table("showDown");
        }
    }
    public void roundMove(){
        int currentIndex=firstMovePlayerIndex();
        while(!onePlayerLeft()||!ActionClosed()){
            roundPlayers.get(currentIndex).nextAction(pot);
            currentIndex++;
            if(currentIndex==numPlayers){
                currentIndex=0;
            }
        }
    }

    public void preFlopRound(){
        blindBet();
        //after small blind and big blind, deal two cards to each player
        for(Player player : roundPlayers){
            player.dealTo(deck);
            System.out.println(player);
        }
        roundMove();
    }
    public void flopRound(){
        communityCards=deck.dealHand(3);
        roundMove();
    }
    public void turnRound(){
        communityCards=deck.dealHand(1);
        roundMove();
    }
    public void riverRound(){
        communityCards=deck.dealHand(1);
        roundMove();
    }
    public void showDown(){
        if(onePlayerLeft()){
            for(Player player : roundPlayers){
                if(!player.hasFolded()){
                        player.takePot(pot);
                }
            }
        }
    }

    public int firstMovePlayerIndex() {
        int index = dealerIndex+1;
        if(dealerIndex==(numPlayers)){
            index=0;
        }
        while(roundPlayers.get(index).hasFolded()){
            index++;
            if(index==numPlayers){
                index=0;
            }
        }
        return index;
    }
    public Boolean ActionClosed(){
        int foldCounter =0;
        int callCounter = 0;
        for(Player player : roundPlayers){
            if(player.hasFolded()){
                foldCounter++;
            }
            if(player.getStake()==pot.getCurrentStake()||player.hasAllin()){
                callCounter++;
            }
        }
        if(foldCounter+callCounter==numPlayers){
            return true;
        }else {
            return false;
        }
    }

    public void removePlayer() {
        for(int i=0;i<numPlayers;i++){
            if(roundPlayers.get(i).isBankrupt()){
                roundPlayers.remove(i);
            }
        }
    }

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {}
    }


}
