package Texas_Hold_Em;


public class DealerPlayer extends Player {
    private static final int DEALER_STAND_LIMIT = 17;//Dealer must stand once dealer's card value is greater than or equal to 17

    /*--------------------Constructor--------------------------*/
    public DealerPlayer(String name, int bank)
    {
        super(name, bank);
    }

    public boolean shouldOpen(PotOfMoney pot){

    }

    public boolean shouldSee(PotOfMoney pot){

    }

    public boolean shouldRaise(PotOfMoney pot){

    }

    /*--------------------Dealer will decide if dealer want to HIT or STAND--------------------------*/
    // dealer never spilt, doubleDown
//    @Override
//    Action chooseAction(BlackjackHand hand){
//        try{
//            if(this.getHands().get(0).getValue()<DEALER_STAND_LIMIT)
//            {
//                return Action.HIT;
//            }
//            else {
//                return Action.STAND;
//            }
//        }
//        catch (Exception e){}
//        return Action.INVALID;
//    }

    /*--------------------Getter method: get the hand of dealer--------------------------*/
//    public BlackjackHand getHand() {
//        return hands.get(0);
//    }
}
