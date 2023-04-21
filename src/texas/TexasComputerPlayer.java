package texas;

import poker.Card;
import poker.DeckOfCards;
import poker.PotOfMoney;

import java.util.List;

abstract public class TexasComputerPlayer extends TexasPlayer{

    public TexasComputerPlayer(String name, int money, int id) {
        super(name, money, id);
    }

    protected abstract int getRiskTolerance();
    protected abstract Rounds getCurrentRound();
    protected abstract void setCommunityCards(List<Card> communityCards);
    protected abstract int preFlopRiskToleranceHelper(Card[] hand);
    protected abstract int riverRoundRiskToleranceHelper(Card[] publicCards, DeckOfCards deck);
    protected abstract int predicateRiskTolerance();
}
