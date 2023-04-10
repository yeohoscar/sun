package Texas_Hold_Em;
import poker.*;

import java.util.ArrayList;
import java.util.List;

public abstract class RoundController {
    public static int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions

    public static final int SMALL_BLIND_AMOUNT = 1;

    public static final int BIG_BLIND_AMOUNT = 2*SMALL_BLIND_AMOUNT;
    protected ArrayList<TexasPlayer> roundPlayers;
    private int dealerIndex;

    protected DeckOfCards deck;
    protected int numPlayers;
    private int smallIndex;
    private int bigIndex;
    private int bigBlindAmount;
    protected List<Card> communityCards;

    protected ArrayList<PotOfMoney> pots = new ArrayList<>();

    private PotOfMoney mainPot = new PotOfMoney();
    private PrintGame printGame;


    public RoundController(DeckOfCards deck, ArrayList<TexasPlayer> players, List<Card> communityCards, int dealerIndex) {
        this.deck = deck;
        this.roundPlayers = players;
        roundPlayers.get(dealerIndex).setDealer(true);
        this.dealerIndex = dealerIndex;
        numPlayers = roundPlayers.size();
        this.bigBlindAmount=10;
        pots.add(mainPot);
        ArrayList<Integer> playersID= new ArrayList<>();
        for(int i = 0;i<players.size();i++){
            playersID.add(i);
        }
        pots.get(0).setPlayerIds(playersID);
        this.printGame = new PrintGame(roundPlayers, deck, pots.get(pots.size()-1), communityCards);
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
        roundPlayers.get(smallIndex).smallBlind(bigBlindAmount/2,pots.get(0));
        roundPlayers.get(bigIndex).bigBlind(bigBlindAmount,pots.get(0));
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean onePlayerLeft(){
        int counter=0;
        for(Player player:roundPlayers) {
            if(player.hasFolded()) {
                pots.get(pots.size() - 1).getPlayerIds().removeIf(id -> id == player.getId());
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
                    dealCommunityCards(3);

//                    printGame.table("pre-flop");
                    //TODO: three public cards should be displayed on the table.
                    break;
                }
                case 2 -> {
                    flopRound();
                    roundCounter++;
                    dealCommunityCards(1);
                    //printGame.table("flop");
                    //TODO: turn card should be displayed on the table
                    break;
                }
                case 3 -> {
                    turnRound();
                    roundCounter++;
                    dealCommunityCards(1);
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
        roundPlayers.get(currentIndex).setDeck(deck);
        System.out.println("\npot.getCurrentStake() in RoundController = "+pots.get(pots.size()-1).getCurrentStake());
        while(!onePlayerLeft()||!ActionClosed()){
            System.out.println("currentRound = "+currentRound);
            System.out.println("Current player: "+roundPlayers.get(currentIndex).getName());
            roundPlayers.get(currentIndex).nextAction(pots.get(pots.size()-1));

            if(needCreateSidePot(roundPlayers.get(currentIndex))){
                createSidePot(roundPlayers.get(currentIndex));
            }
            printGame.table(currentRound);

            currentIndex++;
            if(currentIndex==numPlayers){
                currentIndex=0;
            }
        }
    }

    public boolean needCreateSidePot(Player player) {

        if(!pots.get(pots.size()-1).getPlayerIds().contains(player.getId())){
            return false;
        }
        int activePlayer = pots.get(pots.size()-1).getPlayerIds().size();
        if(!player.hasFolded()){
            if(player.getStake()*activePlayer<=pots.get(pots.size()-1).getTotal()){
                return true;
            }
        }
        return false;
    }

    public void createSidePot(Player player) {
        PotOfMoney sidePot = new PotOfMoney();
        PotOfMoney lastPot = pots.get(pots.size()-1);
        ArrayList<Integer> newPlayerIds = new ArrayList<>(lastPot.getPlayerIds());
        newPlayerIds.removeIf(id -> id == player.getId());
        int activePlayer = pots.get(pots.size()-1).getPlayerIds().size();
        sidePot.setStake(lastPot.getCurrentStake());
        sidePot.setStake(lastPot.getTotal()-player.getStake()*activePlayer);
        sidePot.setPlayerIds(newPlayerIds);
        lastPot.setTotal(player.getStake()*activePlayer);
        pots.add(sidePot);
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
                        player.takePot(pots.get(pots.size()-1));
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
            if(player.getStake()==pots.get(pots.size()-1).getCurrentStake()||player.isAllIn()){
                callCounter++;
            }
        }
        if(foldCounter+callCounter==numPlayers){
            return true;
        }else {
            return false;
        }
    }

    protected abstract boolean needCreateSidePot(TexasPlayer player);

    public void removePlayer() {
        for(int i=0;i<numPlayers;i++){
            if(roundPlayers.get(i).isBankrupt()){
                roundPlayers.remove(i);
            }
        }
    }

    public void dealCommunityCards(int numCardsToBeDealt) {
        for (int i = 0; i < numCardsToBeDealt; i++) {
            communityCards.add(deck.dealNext());
        }
    }

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {}
    }
}
