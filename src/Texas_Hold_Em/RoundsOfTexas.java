
package Texas_Hold_Em;

import poker.*;

import java.util.ArrayList;
// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's

public class RoundsOfTexas extends RoundController {
    private PrintGame printGame;
    private int BigBlindAmount = 10;

    public RoundsOfTexas(DeckOfCards deck, ArrayList<TexasPlayer> texasPlayers, int dealerIndex) {
        super(deck, texasPlayers, dealerIndex);
        this.printGame = new PrintGame(texasPlayers, deck, pot);

    }


    @Override
    public void blindBet() {
        super.blindBet();
        printGame.table("deal");
    }



    @Override
    public void roundCounter(int counter) {
        int roundCounter = counter;
        while (noWinnerProduced() && roundCounter != 5) {
            switch (roundCounter) {
                case 1 -> {
                    preFlopRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    printGame.table("pre-flop");
                    //TODO: three public cards should be displayed on the table.
                    break;
                }
                case 2 -> {
                    flopRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    printGame.table("flop");
                    //TODO: turn card should be displayed on the table
                    break;
                }
                case 3 -> {
                    turnRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    printGame.table("turn");
                    //TODO: river card should be displayed on the table
                    break;
                }
                default -> {
                    riverRound(firstMovePlayerIndex(), pot);
                    roundCounter++;
                    printGame.table("river");
                    break;
                }




            }
        }
    }
    @Override
    public void removePlayer() {
        for(int i=0;i<numPlayers;i++){
            if(roundPlayers.get(i).getBank()<BigBlindAmount){
                roundPlayers.remove(i);
            }
        }
    }
}