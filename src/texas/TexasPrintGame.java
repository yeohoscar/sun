package texas;

import poker.Card;

import java.util.List;

abstract public class TexasPrintGame {
    abstract public void cardPrinter(boolean showDown);
    public void table(Rounds currentRound) {
        switch (currentRound) {
            case PRE_FLOP, FLOP, RIVER, TURN -> cardPrinter(false);
            //after river round, if there are more than two players left, they need to showDown
            default -> cardPrinter(true);
        }
    }
}
