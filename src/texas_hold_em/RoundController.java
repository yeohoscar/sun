package texas_hold_em;
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
    private int smallBlindAmount;
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
        this.smallBlindAmount=5;
        pots.add(mainPot);
        ArrayList<Integer> playersID= new ArrayList<>();
        for(TexasPlayer player: players){
            playersID.add(player.getId());
        }
        pots.get(0).setPlayerIds(playersID);
        this.printGame = new PrintGame(roundPlayers, deck, pots, communityCards);
        this.communityCards = communityCards;
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
        roundPlayers.get(smallIndex).smallBlind(smallBlindAmount,pots.get(0));
        roundPlayers.get(bigIndex).bigBlind(smallBlindAmount,pots.get(0));
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean onePlayerLeft(){
        int counter=0;
        for(TexasPlayer player:roundPlayers) {
            if(player.hasFolded()) {
                pots.get(pots.size() - 1).getPlayerIds().removeIf(id -> id == player.getId());
                counter++;
            }
        }
        return counter == numPlayers - 1;
    }

    public void roundCounter(int counter){
        int roundCounter=counter;

        while (!onePlayerLeft() && roundCounter != 5) {

                    switch (roundCounter) {
                case 1 -> {
                    preFlopRound();
                    roundCounter++;
                    dealCommunityCards(3);
                    System.out.println("\n\nThree Public Cards are released\n");
                }
                case 2 -> {
                    printGame.table(Rounds.FLOP);
                    flopRound();
                    roundCounter++;
                    dealCommunityCards(1);
                    System.out.println("\n\nTurn Card is released\n");
                }
                case 3 -> {
                    printGame.table(Rounds.TURN);
                    turnRound();
                    roundCounter++;
                    dealCommunityCards(1);
                    System.out.println("\n\nRiver Card is released\n");
                }
                default -> {
                    printGame.table(Rounds.RIVER);
                    riverRound();
                    roundCounter++;

                    printGame.table(Rounds.SHOWDOWN);
                }
            }
            resetStakes();
        }
    }
    public void roundMove (Rounds currentRound) {
        int currentIndex = firstMovePlayerIndex(currentRound);
        int activePlayer = 0;
        for(TexasPlayer player:roundPlayers){
            if (!player.hasFolded()){activePlayer++;}
        }
        roundPlayers.get(currentIndex).setDeck(deck);
        while (!onePlayerLeft() && !ActionClosed()) {
            delay(DELAY_BETWEEN_ACTIONS);
            TexasPlayer currentPlayer = roundPlayers.get(currentIndex);
            if(!currentPlayer.hasFolded()||!currentPlayer.isAllIn()){
                currentPlayer.nextAction(getActivePot());
                printGame.table(currentRound);
            }

            currentIndex++;

            if (currentIndex == numPlayers){
                currentIndex = 0;
            }
        }
        onePlayerLeft();
        createSidePot(activePlayer);

    }

    abstract public void createSidePot(int activePlayer);

    public void preFlopRound(){
        blindBet();
        //after small blind and big blind, deal two cards to each player
        for(Player player : roundPlayers){
            player.dealTo(deck);
            System.out.println(player);
        }
        //print the table before the game starts
        System.out.println();
        printGame.table(Rounds.PRE_FLOP);
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
                        player.takePot(getActivePot());
                }
            }
        }
    }
    // get who move first
    public int firstMovePlayerIndex(Rounds currentRound) {
        int index ;
        if(currentRound==Rounds.PRE_FLOP){
            index = bigIndex+1;
        }else {
            index = dealerIndex+1;
        }
        if(index==(numPlayers)){
            index=0;
        }
        System.out.println("INDEX="+index);
        while(roundPlayers.get(index).hasFolded()){
            index++;
            if(index==numPlayers){
                index=0;
            }
        }
        return index;
    }
    //everyone called or folded
    public Boolean ActionClosed(){
        int foldCounter =0;
        int callCounter = 0;
        for(TexasPlayer player : roundPlayers){
            if(player.hasFolded()){
                foldCounter++;
            }
            //TODO: should player.getStake()==pot.getCurrentStake() ?
            else if(player.getStake()==getActivePot().getCurrentStake()||player.isAllIn()){
                callCounter++;
            }
        }
        return foldCounter + callCounter == numPlayers;
    }



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

    // Get the pot everyone is betting with

    public PotOfMoney getActivePot() {
        return pots.get(pots.size() - 1);
    }

    // Reset pot and player stakes to prepare for new round of betting

    public void resetStakes() {
        getActivePot().setStake(0);

        for (TexasPlayer player : roundPlayers) {
            player.resetStake();
        }
    }

    // Utility method to delay actions

    public void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
