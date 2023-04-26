package texas_scramble.Controller;

import poker.Card;
import poker.DeckOfCards;
import poker.Player;
import poker.PotOfMoney;
import texas.RoundController;
import texas.Rounds;
import texas.TexasPlayer;
import texas_hold_em.ComputerTexasPlayer;
import texas_scramble.Deck.*;
import texas_scramble.Player.ComputerScramblePlayer;
import texas_scramble.PrintGame.PrintScramble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class RoundOfScramble extends RoundController {
    private ArrayList<TexasPlayer> roundPlayers;
    protected List<Tile> communityTiles;
    protected PrintScramble printScramble;

    public RoundOfScramble(DeckOfTiles deck, ArrayList<TexasPlayer> texasPlayers, int dealerIndex) {
        super(deck, texasPlayers, dealerIndex);
        this.roundPlayers = texasPlayers;
        this.communityTiles=new ArrayList<>();
        this.printScramble = new PrintScramble(texasPlayers,pots,communityTiles);

        initComputerPlayerWithCommunityTiles(communityTiles);
    }

    private void initComputerPlayerWithCommunityTiles(List<Tile> communityTiles) {
        for (TexasPlayer player : roundPlayers) {
            if (player instanceof ComputerScramblePlayer) {
                ((ComputerScramblePlayer) player).setCommunityTiles(communityTiles);

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
                    Card[] communityCardsArr = new Card[communityCards.size()];
                    player.findBestHand(communityCards.toArray(communityCardsArr), (DeckOfCards) deck);
                    System.out.println(player.getCurrentBestHand());
                    int handValue = player.getCurrentBestHand().getValue();
                    valueRank.put(i, handValue);
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
    public void roundCounter(int counter) {
        int roundCounter = counter;

        while (!onePlayerLeft() && roundCounter != 5) {

            switch (roundCounter) {
                case 1 -> {
                    preFlopRound();
                    roundCounter++;
                    dealCommunityTiles(3);
                    System.out.println("\n\nThree Public Tiles are released\n");
                }
                case 2 -> {
                    printScramble.table(Rounds.FLOP);
                    flopRound();
                    roundCounter++;
                    dealCommunityTiles(1);
                    System.out.println("\n\nTurn Tile is released\n");
                }
                case 3 -> {
                    printScramble.table(Rounds.TURN);
                    turnRound();
                    roundCounter++;
                    dealCommunityTiles(1);
                    System.out.println("\n\nRiver Tile is released\n");
                }
                default -> {
                    printScramble.table(Rounds.RIVER);
                    riverRound();
                    roundCounter++;

                    printScramble.table(Rounds.SHOWDOWN);
                }
            }
            resetStakes();
        }
    }
    public void dealCommunityTiles(int numCardsToBeDealt) {
        for (int i = 0; i < numCardsToBeDealt; i++) {
            communityTiles.add((Tile) deck.dealNext());
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
                currentPlayer.nextAction(getActivePot());
                printScramble.table(currentRound);
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
    public void preFlopRound() {
        blindBet();
        //after small blind and big blind, deal two cards to each player
        for (Player player : roundPlayers) {

            player.dealTo(deck);
            System.out.println(player);
        }
        //print the table before the game starts
        System.out.println();
        printScramble.table(Rounds.PRE_FLOP);
        roundMove(Rounds.PRE_FLOP);
    }
}
