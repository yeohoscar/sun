package texas_scramble.Controller;

import poker.DeckOfCards;
import texas.RoundController;
import texas.Rounds;
import texas.TexasPlayer;
import texas_hold_em.ComputerTexasPlayer;
import texas_scramble.Deck.*;
import texas_scramble.Player.ComputerScramblePlayer;

import java.util.ArrayList;
import java.util.List;
public class RoundOfScramble extends RoundController {
    private ArrayList<TexasPlayer> roundPlayers;
    protected List<Tile> communityTiles;

    public RoundOfScramble(DeckOfTiles deck, ArrayList<TexasPlayer> texasPlayers, int dealerIndex) {
        super(deck, texasPlayers, dealerIndex);
        this.roundPlayers = texasPlayers;
        this.communityTiles=new ArrayList<>();
        //this.printGame = new PrintGame(texasPlayers, deck, pot);

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

    }
    public void roundCounter(int counter) {
        int roundCounter = counter;

        while (!onePlayerLeft() && roundCounter != 5) {

            switch (roundCounter) {
                case 1 -> {
                    preFlopRound();
                    roundCounter++;
                    dealCommunityTiles(3);
                    System.out.println("\n\nThree Public Cards are released\n");
                }
                case 2 -> {
                    //printGame.table(Rounds.FLOP);
                    flopRound();
                    roundCounter++;
                    dealCommunityTiles(1);
                    System.out.println("\n\nTurn Card is released\n");
                }
                case 3 -> {
                    //printGame.table(Rounds.TURN);
                    turnRound();
                    roundCounter++;
                    dealCommunityTiles(1);
                    System.out.println("\n\nRiver Card is released\n");
                }
                default -> {
                    //printGame.table(Rounds.RIVER);
                    riverRound();
                    roundCounter++;

                    //printGame.table(Rounds.SHOWDOWN);
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
        roundPlayers.get(currentIndex).setDeck((DeckOfCards) deck);
        //loop until every one called or folded
        while (!onePlayerLeft() && !ActionClosed()) {
            TexasPlayer currentPlayer = roundPlayers.get(currentIndex);
            if (!currentPlayer.hasFolded() && !currentPlayer.isAllIn()) {
                delay(DELAY_BETWEEN_ACTIONS);
                currentPlayer.setOnTurn(true);
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
}
