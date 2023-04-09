
package Texas_Hold_Em;

import poker.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's

public class RoundsOfTexas extends RoundController {
    private PrintGame printGame;
    public static final int SMALL_BLIND_AMOUNT = 1;

    public static final int BIG_BLIND_AMOUNT = 2*SMALL_BLIND_AMOUNT;
    private ArrayList<TexasPlayer> roundPlayers;
    public RoundsOfTexas(DeckOfCards deck, ArrayList<TexasPlayer> texasPlayers, List<Card> communityCards, int dealerIndex) {
        super(deck, texasPlayers, communityCards, dealerIndex);
        this.roundPlayers = texasPlayers;
        //this.printGame = new PrintGame(texasPlayers, deck, pot);

        initComputerPlayerWithCommunityCards(communityCards);
    }

    private void initComputerPlayerWithCommunityCards(List<Card> communityCards) {
        for (TexasPlayer player : roundPlayers) {
            if (player instanceof ComputerTexasPlayer) {
                ((ComputerTexasPlayer) player).setCommunityCards(communityCards);
            }
        }
    }


//    @Override
//    public void blindBet() {
//        super.blindBet();
//        printGame.table("deal");
//    }

    /*@Override
    public void roundCounter(int counter) {
        int roundCounter = counter;
        while (onePlayerLeft() && roundCounter != 5) {
            switch (roundCounter) {
                case 1 -> {
                    preFlopRound();
                    roundCounter++;
                    printGame.table("pre-flop");
                    //TODO: three public cards should be displayed on the table.
                    break;
                }
                case 2 -> {
                    flopRound();
                    roundCounter++;
                    printGame.table("flop");
                    //TODO: turn card should be displayed on the table
                    break;
                }
                case 3 -> {
                    turnRound();
                    roundCounter++;
                    printGame.table("turn");
                    //TODO: river card should be displayed on the table
                    break;
                }
                default -> {
                    riverRound();
                    roundCounter++;
                    printGame.table("river");
                    break;
                }




            }
        }
    }*/

    @Override
    public void showDown() {
        if (onePlayerLeft()) {
            for (TexasPlayer player : roundPlayers) {
                if (!player.hasFolded()) {
                    player.takePot(pot);
                }
            }
        } else {
            HashMap<Integer, Integer> valueRank = new HashMap<>();
            int maxValue = 0;
            ArrayList<Integer> winners = new ArrayList<>();
            // calculate handValue for each player
            for (int i = 0 ;i<roundPlayers.size();i++) {
                TexasPlayer player =roundPlayers.get(i);
                if (!player.hasFolded()) {
                    Card[] communityCardsArr = new Card[communityCards.size()];
                    player.findBestHand(communityCards.toArray(communityCardsArr), deck);
                    int handValue = player.getCurrentBestHand().getValue();
                    valueRank.put(i, handValue);
                }
            }
            // find who has the largest handValue
            for (Map.Entry<Integer, Integer> entry : valueRank.entrySet()) {
                if (entry.getValue() > maxValue) {
                    winners.clear();
                    winners.add(entry.getKey());
                    maxValue = entry.getValue();
                } else if (entry.getValue() == maxValue) {
                    winners.add(entry.getKey());
                }
            }

            boolean allinPlayer = false;
            for(int i :winners){
                if(roundPlayers.get(i).isAllIn()){
                    allinPlayer=true;
                }
            }
            //no allin player
            if(!allinPlayer){
                for(int i :winners){
                    roundPlayers.get(i).takePot(pot,winners.size());
                }
            }else {
                
            }


        }


    }


    @Override
    public void removePlayer() {
        for(int i=0;i<numPlayers;i++){
            if(roundPlayers.get(i).getBank()<BIG_BLIND_AMOUNT){
                roundPlayers.remove(i);
            }
        }
    }
}