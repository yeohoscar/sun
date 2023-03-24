import poker.Card;

public class DealerPlayer extends Player {
    private static final int DEALER_STAND_LIMIT = 17;

    public DealerPlayer(String name, int bank)
    {
        super(name, bank);
    }
    public Card showOneCard()
    {
        return this.getHands().get(0).getCard(0);
    }
    //dealer always hit until the handValue is greater or equal to 17
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
        }
        catch (Exception e){}
        return Action.INVALID;
    }
    // dealer never spilt
    public BlackjackHand getHand() {
        return hands.get(0);
    }
}
