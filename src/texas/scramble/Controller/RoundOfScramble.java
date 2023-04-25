package texas.scramble.Controller;

import texas.RoundController;
import texas.TexasPlayer;
import texas.hold_em.ComputerTexasPlayer;
import texas.scramble.Deck.*;
import texas.scramble.Player.ComputerScramblePlayer;

import java.util.ArrayList;
import java.util.List;
public class RoundOfScramble extends RoundController {
    private ArrayList<TexasPlayer> roundPlayers;
    private DeckOfTiles deck;

    private List<Tile> communityTiles;

    public RoundOfScramble(DeckOfTiles deck, ArrayList<TexasPlayer> texasPlayers, List<Tile> communityTiles, int dealerIndex) {
        super(null, texasPlayers, null, dealerIndex);
        this.roundPlayers = texasPlayers;
        this.deck=deck;
        this.communityTiles=communityTiles;
        //this.printGame = new PrintGame(texasPlayers, deck, pot);

        initComputerPlayerWithCommunityCards(communityTiles);
    }

    private void initComputerPlayerWithCommunityCards(List<Tile> communityTiles) {
        for (TexasPlayer player : roundPlayers) {
            if (player instanceof ComputerTexasPlayer) {
                ((ComputerTexasPlayer) player).setCommunityElements(communityTiles);
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
            communityTiles.add(deck.dealNext());
        }
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
