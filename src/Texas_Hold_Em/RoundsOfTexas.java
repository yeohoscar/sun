
package Texas_Hold_Em;

import poker.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's

public class RoundsOfTexas extends RoundController {
    private PrintGame printGame;
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
                    for(PotOfMoney pot:pots){
                        player.takePot(pot);
                    }
                }
            }
        } else {
            HashMap<Integer, Integer> valueRank = new HashMap<>();
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


            for (int i = pots.size() - 1; i >= 0; i--) {
                PotOfMoney pot = pots.get(i);
                int potAmount = pot.getTotal();
                HashMap<Integer, Integer> winners = new HashMap<>();
                int highestHandValue=-1;
                // Find the eligible winners for this pot based on hand value
                for (int playerId : valueRank.keySet()) {
                    if (pot.getPlayerIds().contains(playerId)) {
                        int handValue = valueRank.get(playerId);
                        if (handValue > highestHandValue) {
                            highestHandValue = handValue;
                            winners.clear();
                            winners.put(playerId, handValue);
                        } else if (handValue == highestHandValue) {
                            winners.put(playerId, handValue);
                        }
                    }
                }

                // Divide the pot amount equally among the winners
                if (!winners.isEmpty()) {
                    int splitAmount = potAmount / winners.size();
                    for (int winnerId : winners.keySet()) {
                        TexasPlayer winner = getPlayerById(roundPlayers,winnerId);
                        winner.winFromPot(splitAmount,pot);

                    }
                }
            }


        }


    }
    public TexasPlayer getPlayerById(List<TexasPlayer> roundPlayers, int playerId) {
        for (TexasPlayer player : roundPlayers) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null; // Player with the given ID was not found
    }
    public ArrayList<Integer> highestHandValue( HashMap<Integer, Integer> valueRank){
        int maxValue =0;
        ArrayList<Integer> winners=new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : valueRank.entrySet()) {
            if (entry.getValue() > maxValue) {
                winners.clear();
                winners.add(entry.getKey());
                maxValue = entry.getValue();
            } else if (entry.getValue() == maxValue) {
                winners.add(entry.getKey());
            }
        }
        // Define a custom Comparator that compares players based on their stake
        Comparator<Player> compareByStake = Comparator.comparing(Player::getStake);
        // Sort the 'winners' list based on player stake, using the custom Comparator
        Collections.sort(winners, (id1, id2) -> compareByStake.compare(roundPlayers.get(id1), roundPlayers.get(id2)));
        return winners;
    }
    @Override
    public boolean needCreateSidePot(TexasPlayer player) {

        if(!getActivePot().getPlayerIds().contains(player.getId())){
            return false;
        }
        int activePlayer = getActivePot().getPlayerIds().size();
        if(!player.hasFolded()&&player.isAllIn()){
            if(player.getStake()*activePlayer<=getActivePot().getTotal()){
                return true;
            }
        }
        return false;
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