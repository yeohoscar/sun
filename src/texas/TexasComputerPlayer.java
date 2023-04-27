package texas;

import texas.scramble.hand.HandElement;

import java.util.List;

abstract public class TexasComputerPlayer extends TexasPlayer{

    public TexasComputerPlayer(String name, int money, int id) {
        super(name, money, id);
    }

//    public void setCommunityCards(List<Tile> communityCards) {
//        this.communityCards = communityCards;
//    }



    public abstract int getRiskTolerance();
//    abstract int preFlopRiskToleranceHelper();
//    abstract int riverRoundRiskToleranceHelper();
    protected abstract int predicateRiskTolerance();

}
