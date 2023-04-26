package texas;

import poker.Card;
import texas.scramble.deck.Tile;
import texas.scramble.hand.HandElement;

import java.util.List;

abstract public class TexasComputerPlayer extends TexasPlayer{
    private List<? extends HandElement> communityElements;

    public TexasComputerPlayer(String name, int money, int id) {
        super(name, money, id);
    }
//    public void setCommunityCards(List<Tile> communityCards) {
//        this.communityCards = communityCards;
//    }

    public List<? extends HandElement> getCommunityElements(){
        return communityElements;
    }

    public void setCommunityElements(List<? extends HandElement> communityElements){
        this.communityElements = communityElements;
    }


    public Rounds getCurrentRound() {
        int size = communityElements.size();
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
