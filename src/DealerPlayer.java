import poker.Card;

public class DealerPlayer extends Player {
    private static final int DEALER_STAND_LIMIT = 17;

    public DealerPlayer(String name, int bank)
    {
        super(name, bank);
    }
    public int getFaceUp()
    {
        return this.getHands().get(0).getCard(0).getValue();
    }
    public Card showOneCard()
    {
        return this.getHands().get(0).getCard(0);
    }
    @Override
    Action chooseAction(BlackjackHand hand){
        try{
            if(this.getHands().get(0).getValue()<DEALER_STAND_LIMIT)
            {
                return Action.HIT;
            }
            else {
                return Action.STAND;
            }
//            if(this.getHand()[0].getValue()>21)
//            {
//                return Action.BUST;
//            }
//            if(this.getHand()[0].getValue()>=17 && this.getHand()[0].getValue()<=21)
//            if(this.getHand()[0].getValue()>=17)
//            {
//                return Action.STAND;
//            }
        }
        catch (Exception e){}
        //TODO:
        // everytime dealer selects hit:
        //      1-always take HIT action until all cards' value is greater than or eq to 17
        //      2-see if all cards' value in hand is greater than 21, if yes, BUST
        //      3-see if all cards' value is greater than 17 and lt eq to 21, compare with other players,
        //        if dealer's cards' value is equal to player's value, then push, otherwise, compare values to decide the winner.
        // 然后按照输赢决定赌注去向, 开始下一轮
        return null;
    }
    //TODO: we need to add an action BUST?
//    @Override
//    boolean bust(BlackjackHand hand)
//    {
//            return true;
//    }
}
