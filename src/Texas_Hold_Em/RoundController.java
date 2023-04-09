package Texas_Hold_Em;
import poker.*;

import java.util.ArrayList;

public abstract class RoundController {
    public static int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
    protected ArrayList<TexasPlayer> roundPlayers;
    private int dealerIndex;
    protected DeckOfCards deck;
    protected int numPlayers;
    private int smallIndex;
    private int bigIndex;
    private int bigBlindAmount;
    protected Hand communityCards;

    protected PotOfMoney pot = new PotOfMoney();
    private PrintGame printGame;


    public RoundController(DeckOfCards deck, ArrayList<TexasPlayer> players, int dealerIndex) {
        this.deck = deck;
        this.roundPlayers = players;
        roundPlayers.get(dealerIndex).setDealer(true);
        this.dealerIndex = dealerIndex;
        numPlayers = roundPlayers.size();
        this.bigBlindAmount=10;
        this.printGame = new PrintGame(roundPlayers, deck, pot, communityCards);

    }


    public void play() {
        //TODO: if there are more than one unfolded players in the game, they have to showdown to determine the winner.
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
                    communityCards=deck.dealHand(3);
//                    printGame.table("pre-flop");
                    //TODO: three public cards should be displayed on the table.
                    break;
                }
                case 2 -> {
                    flopRound();
                    roundCounter++;
                    communityCards=deck.dealHand(1);
                    //printGame.table("flop");
                    //TODO: turn card should be displayed on the table
                    break;
                }
                case 3 -> {
                    turnRound();
                    roundCounter++;
                    communityCards=deck.dealHand(1);
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
    public void roundMove(Rounds currentRound){
        int currentIndex=firstMovePlayerIndex();
        while(!onePlayerLeft()||!ActionClosed()){
            if(roundPlayers.get(currentIndex) instanceof ComputerTexasPlayer){
                ((ComputerTexasPlayer) roundPlayers.get(currentIndex)).predicateRiskTolerance(communityCards.getHand(), deck, currentRound);
            }

            roundPlayers.get(currentIndex).nextAction(pot);

            printGame.table(currentRound);

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
        roundMove(Rounds.PRE_FLOP);
    }
    public void flopRound(){
        roundMove(Rounds.FLOP);
    }
    public void turnRound(){
        roundMove(Rounds.TURN);
    }
    public void riverRound(){
        roundMove(Rounds.RIVER);
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
        for(TexasPlayer player : roundPlayers){
            if(player.hasFolded()){
                foldCounter++;
            }
            //TODO: should player.getStake()==pot.getCurrentStake() ?
            if(player.getStake()==pot.getCurrentStake()||player.isAllin()){
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
