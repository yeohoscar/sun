package texas;

import texas.scramble.Deck.Tile;

import java.util.List;

abstract public class TexasComputerPlayer extends TexasPlayer{
    private List<Tile> communityCards;

    public TexasComputerPlayer(String name, int money, int id) {
        super(name, money, id);
    }
//    public void setCommunityCards(List<Tile> communityCards) {
//        this.communityCards = communityCards;
//    }
    public List<Tile> getCommunityCards(){
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
    public abstract int getRiskTolerance();
//    abstract int preFlopRiskToleranceHelper();
//    abstract int riverRoundRiskToleranceHelper();
    protected abstract int predicateRiskTolerance();
}
