
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


import poker.Card;
import poker.DeckOfCards;
import poker.PotOfMoney;

import java.util.*;


public class ComputerTexasPlayer extends TexasPlayer {
    public static final int VARIABILITY		= 50;

    private int riskTolerance				= 0;  // willingness of a player to take risks and bluff
    public int HANDANDPUBLIC      		= 0;  // number of cards in a hand of poker plus public cards

    private Random dice						= new Random(System.currentTimeMillis());

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public ComputerTexasPlayer(String name, int money){
        super(name, money);

        riskTolerance = Math.abs(dice.nextInt())%VARIABILITY
                - VARIABILITY/2;

        // this gives a range of tolerance between -VARIABILITY/2 to +VARIABILITY/2
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Accessors
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    // a negative risk tolerance means the player is averse to risk (nervous)
    // a positive risk tolerance means the player is open to risk   (adventurous)

    public int getRiskTolerance() {
        return riskTolerance - getStake(); // tolerance drops as stake increases
    }
    public Card getCard(int num, Card[] hand) {
        if (num >= 0 && num < HANDANDPUBLIC)
            return hand[num];
        else
            return null;
    }
    private void sortCards(Card[] allCards) {
        int minPosition = 0;
        Card[] hand = allCards;
        Card palm = null;

        // consider every position in allCards

        for (int i = 0; i < allCards.length-1; i++) {
            minPosition = i;
            //maxValue    = getCard(i).getValue();

            // consider every other position to the left of this position

            for (int j = i+1; j < allCards.length; j++)  { // is there a higher card to the left?
                if (getCard(minPosition, hand).getValue() > getCard(j, hand).getValue()) {
                    minPosition = j;					// yes, so remember where
                   // maxValue    = getCard(j).getValue();
                }
            }
            // swap card i with the highest value card to the right of it
            palm = allCards[i];
            allCards[i] = allCards[minPosition];
            allCards[minPosition] = palm;
        }

        if (allCards.length == 5 && getCard(4, allCards).getName() == "Ace") reorderStraight(allCards);
        else if(allCards.length == 6 && getCard(5, allCards).getName() == "Ace") reorderStraight(allCards);
    }

    private boolean isContained(Card[] allCards, String name){
        for(Card c: allCards){
            if(c.getName()==name){
                return true;
            }
        }
        return false;
    }
    private void reorderStraight(Card[] allCards) {
        // Check to see if ace should be sorted as a low value (1) rather than a high value (14)
        if (!(allCards.length==5 && allCards[4].isAce() && (isContained(allCards, "Jack") || isContained(allCards, "Queen") || isContained(allCards, "King")) && isContained(allCards, "Ten"))) {

            Card ace = allCards[4];

            allCards[4] = allCards[3];
            allCards[3] = allCards[2];
            allCards[2] = allCards[1];
            allCards[1] = allCards[0];
            allCards[0] = ace;
        }
        if (!(allCards.length==6 && allCards[5].isAce() && (isContained(allCards, "Jack") || isContained(allCards, "Queen") || isContained(allCards, "King")) && isContained(allCards, "Ten"))) {

            Card ace = allCards[5];

            allCards[5] = allCards[4];
            allCards[4] = allCards[3];
            allCards[3] = allCards[2];
            allCards[2] = allCards[1];
            allCards[1] = allCards[0];
            allCards[0] = ace;
        }
    }

    private void predicateBestHandType(Card[] publicCards, DeckOfCards deck, Rounds currentRound){
        int length = publicCards.length+2;
        Card[] allCards = new Card[length];
        HANDANDPUBLIC = allCards.length;
        System.arraycopy(publicCards, 0, allCards, 0, publicCards.length);
        System.arraycopy(super.getHand().getHand(), 0, allCards, publicCards.length, super.getHand().getHand().length);
        //we sort all cards from left to right, card with highest value is on right
        sortCards(allCards);
        if(currentRound==Rounds.FLOP || currentRound==Rounds.TURN){
            //TODO:1-when in Turn or River round, see if public cards can direct form some card type, this will impact the actions of computer player
            //     2-I think we do not need check pre-flop round and river round in each card type prediction
            //     3-if odds of all card types are zero, then it must be High card type
        }
    }
    //this method returns the quantity of cards that appear n times in allCards(hand card +  public card)
    private int countContains(Map<String, Integer> names, int n){
        int count = 0;
        for(int value: names.values()){
            if(value==n){
                count ++;
            }
        }
        return count;
    }
    public int combination(int n, int k) {
        if (k == 0 || n == k) {
            return 1;
        }
        return combination(n - 1, k - 1) + combination(n - 1, k);
    }
    /*--------------------predicate the odds of Royal Straight Flush--------------------------*/
    private Integer oddsOfRoyalStraightFlush(Card[] allCards, Rounds currentRound){
        int odds=0;
        String[] cards = {"Ace", "King", "Queen", "Jack", "Ten"};
        //hashMap stores the number of occurrence of each suit, key is the suit, and Integer is the number of occurrence of the suit
        Map<String, Integer> suits = new HashMap<>();
        //if one of the "Ace", "King", "Queen", "Jack", "Ten" appears in public cards and player's hand
        //we store the suit and the number of occurrence of this suit increases by one
        for(Card card: allCards){
            if(card.isAce() || card.isTen() || card.isJack() || card.isQueen() || card.isKing()){
                String suit = card.getSuit();
                suits.put(suit, suits.getOrDefault(suit, 0)+1);
            }
        }
        //if current round is flop round, there at least needs 3 cards which are in "Ace", "King", "Queen", "Jack", "Ten",
        //and also have same suit.
        if(currentRound==Rounds.FLOP){
            int outs;
            for (int count : suits.values()) {
                if (count == 3) {
                    outs=2;
                    odds=outs*4;
                    break;
                }else if(count==4){
                    outs=1;
                    odds=outs*4;
                    break;
                }else if(count==5){
                //if there are five cards from "Ace", "King", "Queen", "Jack", "Ten" and have same suit,
                //then this player has Royal Straight Flush
                    odds=100;
                    break;
                }
            }
        }
        //if current round is turn round, there at least needs 4 cards which are in "Ace", "King", "Queen", "Jack", "Ten",
        //and also have same suit.
        if(currentRound==Rounds.TURN){
            int outs;
            for (int count : suits.values()) {
                if(count==4){
                    outs=1;
                    odds=outs*2;
                    break;
                }else if(count==5){
                    odds=100;
                    break;
                }
            }
        }
        return odds;
    }

    /*--------------------predicate the odds of Straight Flush--------------------------*/
    private Integer oddsOfStraightFlush(Card[] allCards, Rounds currentRound){
        double oddsOfStraight = oddsOfStraight(allCards, currentRound)*0.01;
        double oddsOfFlush = oddsOfFlush(allCards, currentRound)*0.01;
        double oddsOfStraightFlush = 0.0;
        if(oddsOfStraight!=0 && oddsOfFlush!=0){
            oddsOfStraightFlush = (oddsOfStraight*oddsOfFlush)*100;
        }
        return (int) oddsOfStraightFlush;
    }

    /*--------------------predicate the odds of Four of a kind--------------------------*/
    private Integer oddsOfFourOfAKind(Card[] allCards, Rounds currentRound){
        //if there are more than 2 identical cards, then it is possible to form four of a kind in flop round
        int odds=0;
        Map<String, Integer> names = new HashMap<>();
        for(Card card: allCards){
            String name = card.getName();
            names.put(name, names.getOrDefault(name, 0)+1);
        }
        if(currentRound==Rounds.FLOP){
            int outs = 0;
            if(countContains(names, 2)==1 && countContains(names, 1)==3){
                outs = 1;
                odds = outs*4;
            }
            if(countContains(names, 2)==2){
                outs=2;
                odds=outs*4;
            }
            if(countContains(names, 3)==1 && countContains(names, 1)==2){
                outs=1;
                odds=outs*4;
            }
            if(countContains(names, 3)==1 && countContains(names, 2)==1){
                outs=2;
                odds=outs*4;
            }
            if(countContains(names, 4)==1){
                odds=100;
            }
        }
        //if there are more than 3 identical cards, then it is possible to form four of a kind in turn round
        if(currentRound==Rounds.TURN){
            int outs = 0;
            if(countContains(names, 3)==1 && countContains(names, 1)==3){
                outs=1;
                odds=outs*2;
            }
            if(countContains(names, 3)==2){
                outs=2;
                odds=outs*2;
            }
            if(countContains(names, 4)==1){
                odds=100;
            }
        }
        return odds;
    }

    /*--------------------predicate the odds of Full House--------------------------*/
    private Integer oddsOfFullHouse(Card[] allCards, Rounds currentRound){
        //if there are more than 2 identical cards, then it is possible to form full house in flop round
        int odds=0;
        Map<String, Integer> names = new HashMap<>();
        for(Card card: allCards){
            String name = card.getName();
            names.put(name, names.getOrDefault(name, 0)+1);
        }
        if(currentRound==Rounds.FLOP){
            int outs = 0;
            if(countContains(names, 2)==1 && countContains(names, 1)==3){
                outs = 11;
                odds = outs*4-(outs-8);
            }
            if(countContains(names, 2)==2){
                outs=4;
                odds = outs*4;
            }
            if(countContains(names, 3)==1 && countContains(names, 1)==2){
                outs = 6;
                odds = outs*4;
            }
            if(countContains(names, 3)==1 && countContains(names, 2)==1){
                odds = 100;
            }
        }
        //if there are more than 3 identical cards, then it is possible to form four of a kind in turn round
        if(currentRound==Rounds.TURN){
            int outs = 0;
            if(countContains(names, 3)==1 && countContains(names, 1)==3){
                outs=9;
                odds = outs*2-(outs-8);
            }
            if(countContains(names, 2)==2){
                outs=4;
                odds = outs*2;
            }
            if(countContains(names, 2)==3){
                outs=6;
                odds = outs*2;
            }
            if(countContains(names, 3)==1 && countContains(names, 2)==1){
                odds=100;
            }
        }
        return odds;
    }

    /*--------------------predicate the odds of Flush--------------------------*/
    private Integer oddsOfFlush(Card[] allCards, Rounds currentRound){
        //if there are more than 3 cards with same suits, then it is possible to form flush in flop round
        int odds=0;
        Map<String, Integer> suits = new HashMap<>();
        for(Card card: allCards){
            String suit = card.getSuit();
            suits.put(suit, suits.getOrDefault(suit, 0)+1);
        }
        if(currentRound==Rounds.FLOP){
            int outs = 0;
            for(int count: suits.values()){
                if(count>=3 && count<5){
                    outs = combination(13-count, 5-count);
                    if(outs>8){
                        odds = outs*4-(outs-8);
                    }
                    else {
                        odds = outs*4;
                    }
                    break;
                }
                if(count==5){
                    odds=100;
                    break;
                }
            }
        }
        //if there are more than 4 cards with same suits, then it is possible to form flush in turn round

        if(currentRound==Rounds.TURN){
            int outs = 0;
            for(int count: suits.values()){
                if(count==4){
                    outs = combination(13-count, 5-count);
                    if(outs>8){
                        odds = outs*2-(outs-8);
                    }
                    else {
                        odds = outs*2;
                    }
                    break;
                }
                if(count==5){
                    odds=100;
                    break;
                }
            }
        }
        return odds;
    }

    /*--------------------predicate the odds of Straight--------------------------*/
    private Integer oddsOfStraight(Card[] allCards, Rounds currentRound){
        ArrayList<Integer> difference = new ArrayList<>();
        ArrayList<Integer> allCardsValue = new ArrayList<>();
        int outs;
        int odds = 0;
        Integer[] oneSuitOfCards;
        int fastIndex = 0;
        int slowIndex = 0;
        if(allCards[allCards.length-1].isAce()){
            oneSuitOfCards = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
            difference = findCardsThatNotInOneSuitOfCards(allCards, fastIndex, slowIndex, oneSuitOfCards);
        }else if(allCards[0].isAce()){
            fastIndex=1;
            slowIndex=1;
            oneSuitOfCards = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
            difference = findCardsThatNotInOneSuitOfCards(allCards, fastIndex, slowIndex, oneSuitOfCards);
        }else {
            oneSuitOfCards = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
            difference = findCardsThatNotInOneSuitOfCards(allCards, fastIndex, slowIndex, oneSuitOfCards);
        }
        for(int i=0; i<allCards.length; i++){
            allCardsValue.add(allCards[i].getValue());
        }

        //int[] difference = diff.stream().mapToInt(Integer::intValue).toArray();
        ArrayList<Integer> record = new ArrayList<>();//this will record those combinations that can form a straight with allCards(hand cards + public cards)
        for(ArrayList<Integer> integer: generateCombinations(difference, currentRound)){
            ArrayList<Integer> temp = new ArrayList<>();
            temp = allCardsValue;
            temp.addAll(integer);
            Collections.sort(temp);//after we add the combination value to allCardsValue, we need to sort it again
            if(canFormAStraight(temp)){//if the combination can form a straight with allCards(hand cards + public cards)
                record.addAll(integer);
            }
        }
        if(record.size()==0){//if all combinations can not form a straight, then the odds of straight is zero
            return 0;
        }else {
            //we delete repeated elements in record so that we can calculate outs
            Set<Integer> set = new HashSet<>(record);
            record.clear();
            record.addAll(set);
            outs = record.size()*4;//because there are four different suits
            if(currentRound==Rounds.FLOP){
                if(outs>8){
                    odds = (outs*4)-(outs-8);
                }else {
                    odds = outs*4;
                }
            }else if(currentRound==Rounds.TURN){
                if(outs>8){
                    odds = (outs*2)-(outs-8);
                }else {
                    odds = outs*2;
                }
            }
            return odds;
        }

    }
    //this helper method is used to check if there exists a straight in ArrayList temp
    private boolean canFormAStraight(ArrayList<Integer> temp){
        int count = 0;
        for(int i=0; i<temp.size()-1; i++){
            if(temp.get(i).equals(temp.get(i)+1)){
                count++;
            }else {
                count=0;
            }
            if (count==4){
                break;
            }
        }
        return count==4;
    }
    public ArrayList<ArrayList<Integer>> generateCombinations(ArrayList<Integer>  difference, Rounds currentRound) {
        int n = difference.size();
        ArrayList<ArrayList<Integer>> combinations1 = new ArrayList<>();
        if(currentRound==Rounds.FLOP){
            // C(1, n)
            for (int i = 0; i < n; i++) {
                ArrayList<Integer> combination = new ArrayList<>(Arrays.asList(difference.get(i)));
                combinations1.add(combination);
            }
            // C(2, n)
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    ArrayList<Integer> combination = new ArrayList<>(Arrays.asList(difference.get(i), difference.get(j)));
                    combinations1.add(combination);
                }
            }
        }
        else if(currentRound==Rounds.TURN){
            for (int i = 0; i < n; i++) {
                ArrayList<Integer> combination = new ArrayList<>(Arrays.asList(difference.get(i)));
                combinations1.add(combination);
            }
        }
        return combinations1;
    }
    /*this method finds cards that are not in one suit of cards:
             for example: allCards = {3, 6, 7, 9, A},
                 one suit of cards = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}
                 cards that not in one suit of cards: difference = {2, 4, 5, 8, 10, 11, 12, 13}*/
    private ArrayList<Integer> findCardsThatNotInOneSuitOfCards(Card[] allCards, int fastIndex, int slowIndex, Integer[] oneSuitOfCards){
        ArrayList<Integer> difference = new ArrayList<>();
        for(;fastIndex<oneSuitOfCards.length; fastIndex++){
            if(oneSuitOfCards[fastIndex]==allCards[slowIndex].getValue()){
                while (slowIndex<allCards.length && oneSuitOfCards[fastIndex]==allCards[slowIndex].getValue()){
                    slowIndex++;
                }
            }
            else {
                difference.add(oneSuitOfCards[fastIndex]);
            }
        }
        return difference;
    }


    /*--------------------predicate the odds of Three of a kind--------------------------*/
    private Integer oddsOfThreeOfAKind(Card[] allCards, Rounds currentRound){
        int odds=0;
        Map<String, Integer> names = new HashMap<>();
        for(Card card: allCards){
            String name = card.getName();
            names.put(name, names.getOrDefault(name, 0)+1);
        }
        if(currentRound==Rounds.FLOP){
            int outs = 0;
            //if there are only two cards are same, no three cards are same
            if(countContains(names, 2)==1 && countContains(names, 3)==0){
                outs=2;
                odds = outs*4;
            }
            //must be Three of a kind
            if(countContains(names, 3)==1 && countContains(names, 2)==0){
                odds=100;
            }
/*            //player must select Full House instead of Three of a kind
//            if(countContains(names, 3) && countContains(names, 2)){
//                odds = 0;
//            }
            //it must be Four of a kind instead of Three of a kind
//            if(countContains(names, 4)){
//                odds = 0;
//            }*/
            if(countContains(names, 1)==5){
                outs = 15;
                odds = outs*4-(outs-8);
            }
        }
        //if there are more than 3 identical cards, then it is possible to form four of a kind in turn round
        if(currentRound==Rounds.TURN){
            int outs = 0;
            if(countContains(names, 2)==1 && countContains(names, 1)==4){
                outs = 2;
                odds = outs*2;
            }
            if(countContains(names, 3)==1 && countContains(names, 1)==3){
                outs = 1;
                odds = outs*2;
            }
        }
        return odds;
    }

    /*--------------------predicate the odds of Two Pair--------------------------*/
    private Integer oddsOfTwoPair(Card[] allCards, Rounds currentRound){
        int odds=0;
        Map<String, Integer> names = new HashMap<>();
        for(Card card: allCards){
            String name = card.getName();
            names.put(name, names.getOrDefault(name, 0)+1);
        }
        if(currentRound==Rounds.FLOP) {
            int outs = 0;
            //if there are only two cards are same, no three cards are same
            if(countContains(names, 1)==5){
                outs=15;
                odds = outs*4-(outs-8);
            }
            if(countContains(names, 2)==1 && countContains(names, 1)==3){
                outs=9;
                odds=outs*4-(outs-8);
            }
            if(countContains(names, 2)==2){
                odds=100;
            }
        }
        if(currentRound==Rounds.TURN) {
            int outs = 0;
            if(countContains(names, 2)==1 && countContains(names, 1)==4){
                outs=12;
                odds=outs*2-(outs-8);
            }
            if(countContains(names, 2)==2 || countContains(names, 2)==3){
                odds=100;
            }
        }
        return odds;
    }

    /*--------------------predicate the odds of Pair--------------------------*/
    private Integer oddsOfPair(Card[] allCards, Rounds currentRound){
        int odds=0;
        Map<String, Integer> names = new HashMap<>();
        for(Card card: allCards){
            String name = card.getName();
            names.put(name, names.getOrDefault(name, 0)+1);
        }
        if(currentRound==Rounds.FLOP) {
            int outs = 0;
            if(countContains(names, 1)==5){
                outs=15;
                odds = outs*4-(outs-8);
            }
            if(countContains(names, 2)==1 && countContains(names, 1)==3){
                odds=100;
            }
        }
        if(currentRound==Rounds.TURN){
            int outs = 0;
            if(countContains(names, 1)==6){
                outs=18;
                odds = outs*2-(outs-8);
            }
            if(countContains(names, 2)==1 && countContains(names, 1)==4){
                odds=100;
            }
            if(countContains(names, 3)==1 && countContains(names, 1)==3){
                odds=100;
            }
        }
        return odds;
    }




    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Key decisions a player must make
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    public boolean shouldSee(PotOfMoney pot) {
        if (getStake() == 0)
            return true;
        else
            return Math.abs(dice.nextInt())%100 < getHand().getRiskWorthiness() +
                    getRiskTolerance();
    }

    public boolean shouldRaise(PotOfMoney pot) {
        return Math.abs(dice.nextInt())%80 < getHand().getRiskWorthiness() +
                getRiskTolerance();
    }

    public boolean shouldCheck(PotOfMoney pot) {
        //TODO: FIGURE OUT HOW THE RISK THING WORKS
        return Math.abs(dice.nextInt())%80 < getHand().getRiskWorthiness() +
                getRiskTolerance();
    }

    public boolean shouldAllIn(PotOfMoney pot) {
        return Math.abs(dice.nextInt())%50 < getHand().getRiskWorthiness() +
                getRiskTolerance();
    }
}
