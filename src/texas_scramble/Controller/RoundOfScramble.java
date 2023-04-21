package texas_scramble.Controller;

import poker.Card;
import poker.DeckOfCards;
import texas.RoundController;
import texas.TexasPlayer;

import java.util.ArrayList;
import java.util.List;

public class RoundOfScramble extends RoundController {
    public RoundOfScramble(DeckOfCards deck, ArrayList<TexasPlayer> players, List<Card> communityCards, int dealerIndex) {
        super(deck, players, communityCards, dealerIndex);
    }

    @Override
    public void removePlayer() {
        
    }

    @Override
    public void showDown() {

    }

    @Override
    public void createSidePot(int activePlayer) {

    }
}
