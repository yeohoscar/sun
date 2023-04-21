package texas;

import poker.Card;
import poker.DeckOfCards;
import poker.PotOfMoney;

import java.util.List;

abstract public class TexasComputerPlayer extends TexasPlayer{
    private List<Card> communityCards;

    public TexasComputerPlayer(String name, int money, int id) {
        super(name, money, id);
    }
    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }
    public List<Card> getCommunityCards(){
        return this.communityCards;
    }
    public Rounds getCurrentRound() {
        switch (communityCards.size()) {
            case 3 -> {
                return Rounds.FLOP;
            }
            case 4 -> {
                return Rounds.TURN;
            }
            case 5 -> {
                return Rounds.RIVER;
            }
            default -> {
                return Rounds.PRE_FLOP;
            }
        }
    }
    protected abstract int getRiskTolerance();
    protected abstract int preFlopRiskToleranceHelper(Card[] hand);
    protected abstract int riverRoundRiskToleranceHelper(Card[] publicCards, DeckOfCards deck);
    protected abstract int predicateRiskTolerance();
}
