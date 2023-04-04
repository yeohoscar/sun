
package Texas_Hold_Em;

import poker.*;

import java.util.ArrayList;
// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's

public class RoundsOfTexas extends RoundController {
    private PrintGame printGame;
    private PotOfMoney pot;

    public RoundsOfTexas(DeckOfCards deck, ArrayList<TexasPlayer> texasPlayers, int dealerIndex) {
        super(deck, texasPlayers, dealerIndex);
        this.printGame = new PrintGame(texasPlayers, deck, getPot());
        this.pot = getPot();
    }


    @Override
    public void blindBet(int dealerIndex, int sizeOfPlayers) {
        super.blindBet(dealerIndex, sizeOfPlayers);
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
}