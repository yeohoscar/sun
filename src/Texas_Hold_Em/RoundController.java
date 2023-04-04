package Texas_Hold_Em;
import poker.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public abstract class RoundController {
    public static int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions


    private ArrayList<? extends Player> roundPlayers;


    private int dealerIndex;
    private DeckOfCards deck;
    private int numPlayers;
    private int smallIndex;
    private int bigIndex;
    private PotOfMoney pot = new PotOfMoney();


    public RoundController(DeckOfCards deck, ArrayList<? extends Player> players, int dealerIndex)  {
        this.deck    = deck;
        roundPlayers = new ArrayList<>(players);
        this.dealerIndex = dealerIndex;
        numPlayers = roundPlayers.size();
    }

    public PotOfMoney getPot() {
        return pot;
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


        blindBet(dealerIndex, roundPlayers.size()-1);
        roundCounter(1);



        //TODO decided who win

        //TODO remove player who all-in and loss or has less than big blind chips
    }


    public void blindBet(int dealerIndex, int sizeOfPlayers){
        System.out.println("Small Blind and Big Blind: ");

        System.out.println("\n\nNew Deal:\n\n");
        //after small blind and big blind, deal two cards to each player
        for(Player player : roundPlayers){
            player.dealTo(deck);
            System.out.println(player);
        }
        if(dealerIndex==(sizeOfPlayers-1)){
            smallIndex = sizeOfPlayers;
            bigIndex = 0;
        }else if(dealerIndex==sizeOfPlayers){
            smallIndex = 0;
            bigIndex = 1;
        }else {
            smallIndex = dealerIndex+1;
            bigIndex = dealerIndex+2;
        }
        roundPlayers.get(smallIndex).smallBlind();
        roundPlayers.get(bigIndex).bigBlind();
    }


    public int getNumBestPlayer(boolean display) {
        int bestHandScore = 0, score = 0, bestPos = 0;

        Player bestPlayer = null, currentPlayer = null;

        for (int i = 0; i < getNumPlayers(); i++) {
            currentPlayer = roundPlayers.get(i);

            if (currentPlayer.hasFolded())
                continue;

            score = currentPlayer.getHand().getValue();

            if (score > bestHandScore) {
                if (display) {
                    if (bestHandScore == 0)
                        System.out.println("> " + currentPlayer.getName() + " goes first:\n" +
                                currentPlayer.getHand());
                    else
                        System.out.println("> " + currentPlayer.getName() + " says 'Read them and weep:'\n" +
                                currentPlayer.getHand());
                }

                bestPos		  = i;
                bestHandScore = score;
                bestPlayer = currentPlayer;
            }
            else
            if (display)
                System.out.println("> " + currentPlayer.getName() + " says 'I lose':\n" +
                        currentPlayer.getHand());
        }

        return bestPos;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean noWinnerProduced(){
        int counter=0;
        for(Player player : roundPlayers){
            if(player.hasFolded()){
                counter++;
            }
        }
        if(counter==numPlayers-1){
            return false;
        }else {
            return true;
        }
    }

    public void roundCounter(int counter){
        int roundCounter=counter;
        while (noWinnerProduced() && roundCounter != 5) {
            switch (roundCounter) {
                case 1 -> {
                    preFlopRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    //printGame.table("pre-flop");
                    //TODO: three public cards should be displayed on the table.
                    break;
                }
                case 2 -> {
                    flopRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    //printGame.table("flop");
                    //TODO: turn card should be displayed on the table
                    break;
                }
                case 3 -> {
                    turnRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    //printGame.table("turn");
                    //TODO: river card should be displayed on the table
                    break;
                }
                default -> {
                    riverRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    //printGame.table("river");
                    break;
                }
            }
            //printGame.table("showDown");
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

    public void removePlayer(int num) {
        if (num >= 0 && num < numPlayers)
        {
            System.out.println("\n> " + roundPlayers.get(num).getName() + " leaves the game.\n");

           roundPlayers.remove(num);
        }
    }

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {}
    }


}