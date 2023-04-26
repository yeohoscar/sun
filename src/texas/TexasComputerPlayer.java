package texas;

import poker.Card;
import texas.scramble.Deck.Tile;

import java.util.List;

abstract public class TexasComputerPlayer extends TexasPlayer{
    private List<Card> communityCards;
    private List<Tile> communityTiles;

    public TexasComputerPlayer(String name, int money, int id) {
        super(name, money, id);
    }
//    public void setCommunityCards(List<Tile> communityCards) {
//        this.communityCards = communityCards;
//    }
    public List<Card> getCommunityCards(){
        return this.communityCards;
    }
    public void setCommunityCards(List<Card> communityCards){this.communityCards=communityCards;}
    public List<Tile> getCommunityTiles(){
        return this.communityTiles;
    }
    public void setCommunityTiles(List<Tile> communityTiles){this.communityTiles=communityTiles;}


    public Rounds getCurrentRound() {
        int size;
        if(communityCards.isEmpty()){
            size=communityTiles.size();
        }else {
            size=communityCards.size();
        }
        switch (size) {
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
