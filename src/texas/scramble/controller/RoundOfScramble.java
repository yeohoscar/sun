package texas.scramble.controller;

import poker.Card;
import poker.DeckOfCards;
import poker.Player;
import poker.PotOfMoney;
import texas.RoundController;
import texas.Rounds;
import texas.TexasComputerPlayer;
import texas.TexasPlayer;
import texas.hold_em.HoldEmComputerPlayer;
import texas.scramble.deck.*;
import texas.scramble.hand.HandElement;
import texas.scramble.player.ComputerScramblePlayer;
import texas.scramble.player.HumanScramblePlayer;
import texas.scramble.print_game.PrintScramble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundOfScramble extends RoundController {
    private ArrayList<TexasPlayer> roundPlayers;
    private List<Tile> communityTiles;

    public RoundOfScramble(DeckOfTiles deck, ArrayList<TexasPlayer> texasPlayers, int dealerIndex) {
        super(deck, texasPlayers, dealerIndex);
        this.roundPlayers = texasPlayers;
        this.communityTiles = new ArrayList<>();

        this.printGame = new PrintScramble(texasPlayers, pots, communityTiles);
        initComputerPlayerWithCommunityTiles(communityTiles);
    }

    private void initComputerPlayerWithCommunityTiles(List<Tile> communityTiles) {
        for (TexasPlayer player : roundPlayers) {
            if (player instanceof HoldEmComputerPlayer) {
                ((TexasComputerPlayer) player).setCommunityElements(communityTiles);
            }
        }
    }

    @Override
    public void showDown() {
        if (onePlayerLeft()) {
            for (TexasPlayer player : roundPlayers) {
                if (!player.hasFolded()) {
                    for (PotOfMoney pot : pots) {
                        player.takePot(pot);
                    }
                }
            }
        } else {
            HashMap<Integer, Integer> valueRank = new HashMap<>();
            // calculate handValue for each player
            for (int i = 0; i < roundPlayers.size(); i++) {
                TexasPlayer player = roundPlayers.get(i);
                if (!player.hasFolded()) {
                    //TODO get words and FinalValue For each Player
                    if (player instanceof HumanScramblePlayer) {
                        valueRank.put(i, ((HumanScramblePlayer) player).submitWord(communityTiles));
                    }else if(player instanceof ComputerScramblePlayer) {
                        String[] playerHand = combineToString(communityTiles, (Tile[]) player.getHand().getHand());
                        HashMap<String, Integer> CPUWords = ((ComputerScramblePlayer) player).submitWords(playerHand);
                        int handValue = 0;
                        for (Map.Entry<String, Integer> entry : CPUWords.entrySet()) {
                            System.out.println(player.getName()+" submitted word \"" + entry.getKey() + "\" Value = " + entry.getValue());
                            handValue+=entry.getValue();
                        }
                        valueRank.put(i, handValue);
                    }
                }
            }
            // find who has the largest handValue
            for (int i = pots.size() - 1; i >= 0; i--) {
                PotOfMoney pot = pots.get(i);
                int potAmount = pot.getTotal();
                HashMap<Integer, Integer> winners = new HashMap<>();
                int highestHandValue = -1;
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
                        TexasPlayer winner = getPlayerById(roundPlayers, winnerId);
                        winner.winFromPot(splitAmount, pot);

                    }
                }
            }


        }
    }

    @Override
    public void roundMove(Rounds currentRound) {
        //decide who move first
        int currentIndex = firstMovePlayerIndex(currentRound);
        int activePlayer = 0;
        for (TexasPlayer player : roundPlayers) {
            if (!player.hasFolded()) {
                activePlayer++;
            }
        }
        roundPlayers.get(currentIndex).setDeck((DeckOfTiles) deck);
        //loop until every one called or folded
        while (!onePlayerLeft() && !ActionClosed()) {
            TexasPlayer currentPlayer = roundPlayers.get(currentIndex);
            if (!currentPlayer.hasFolded() && !currentPlayer.isAllIn()) {
                delay(DELAY_BETWEEN_ACTIONS);
                currentPlayer.setOnTurn(true);
                if (currentPlayer instanceof TexasComputerPlayer)
                    ((TexasComputerPlayer) currentPlayer).setCommunityElements(communityTiles);
                currentPlayer.nextAction(getActivePot());
                printGame.table(currentRound);
                currentPlayer.setOnTurn(false);
            }

            currentIndex++;

            if (currentIndex == numPlayers) {
                currentIndex = 0;
            }
        }
        onePlayerLeft();
        createSidePot(activePlayer);

    }

    // TODO: ADD THIS TO AFTER SHOWDOWN
    // Takes all the words submitted by users as input and adds into each computer players dictionary if it is not in theirs
    private void updatePlayerDictionary(List<String> newWords) {
        for (String word : newWords) {
            for (int i = 1; i < roundPlayers.size(); i++) {
                ComputerScramblePlayer csp = (ComputerScramblePlayer) roundPlayers.get(i);

                if (csp.knowsWord(word)) csp.learnWord(word);
            }
        }
    }

    @Override
    protected void dealCommunityElements(int numCardsToBeDealt) {
        for (int i = 0; i < numCardsToBeDealt; i++) {
            communityTiles.add((Tile) deck.dealNext());
        }
    }

    public String[] combineToString(List<Tile> communityTiles,Tile[] hand){
        String[] str = new String[7];
        int i=0;
        for(Tile tile : communityTiles){
            str[i]=tile.name();
            i++;
        }
        for(Tile handTile : hand){
            str[i]= handTile.name();
            i++;
        }
        return str;
    }
}
