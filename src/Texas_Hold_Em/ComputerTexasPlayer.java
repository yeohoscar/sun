
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


import poker.*;

import java.lang.reflect.Method;
import java.util.*;


public class ComputerTexasPlayer extends TexasPlayer {
    public static final int VARIABILITY = 50;

    private int riskTolerance;  // willingness of a player to take risks and bluff

    private Random dice	= new Random(System.currentTimeMillis());

    private List<Card> communityCards;

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public ComputerTexasPlayer(String name, int money,int id){
        super(name, money,id);

//        riskTolerance = Math.abs(dice.nextInt())%VARIABILITY
//                - VARIABILITY/2;
        riskTolerance = 50;
        //System.out.println("riskToleranceeeeeeeeeeeeee = "+riskTolerance);
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
        System.out.println("Original RiskTolerance = "+riskTolerance);
//        System.out.println("\n\nriskTolerance = "+riskTolerance);
//        System.out.println("getStake() = "+getStake() );
//        System.out.println("predicateRiskTolerance() = "+predicateRiskTolerance());
//        System.out.println("Updated riskTolerance = "+(riskTolerance - getStake() - predicateRiskTolerance()));
        return riskTolerance - getStake() - predicateRiskTolerance(); // tolerance drops as stake increases
    }

    public List<Card> getCommunityCards(){
        return communityCards;
    }

    public Rounds getCurrentRound(){
            switch (communityCards.size()){
                case 3->{
                    return Rounds.FLOP;
                }
                case 4->{
                    return Rounds.TURN;
                }
                case 5->{
                    return Rounds.RIVER;
                }
                default -> {
                    return Rounds.PRE_FLOP;
                }
            }
    }
    public Card getCard(int num, Card[] hand) {
        if (num >= 0 && num < hand.length) {
            return hand[num];
        }
        else{
            return null;
        }
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Mutators
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public void sortCards(Card[] allCards) {
        int minPosition = 0;
        Card palm = null;

        // consider every position in allCards

        for (int i = 0; i < allCards.length-1; i++) {
            minPosition = i;
            //maxValue    = getCard(i).getValue();

            // consider every other position to the left of this position

            for (int j = i+1; j < allCards.length; j++)  { // is there a higher card to the left?
                if (getCard(minPosition, allCards).getValue() > getCard(j, allCards).getValue()) {
                    minPosition = j;					// yes, so remember where
                   // maxValue    = getCard(j).getValue();
                }
            }
            // swap card i with the highest value card to the right of it
            palm = allCards[i];
            allCards[i] = allCards[minPosition];
            allCards[minPosition] = palm;
        }

        if (allCards.length == 5 && Objects.equals(getCard(4, allCards).getName(), "Ace")) reorderStraight(allCards);
        else if(allCards.length == 6 && Objects.equals(getCard(5, allCards).getName(), "Ace")) reorderStraight(allCards);
    }

    private boolean isContained(Card[] allCards, String name){
        for(Card c: allCards){
            if(Objects.equals(c.getName(), name)){
                return true;
            }
        }
        return false;
    }
    private void reorderStraight(Card[] allCards) {
        // Check to see if ace should be sorted as a low value (1) rather than a high value (14)
        if (allCards.length==5 && !(allCards[4].isAce() && (isContained(allCards, "Jack") || isContained(allCards, "Queen") || isContained(allCards, "King")) && isContained(allCards, "Ten"))) {

            Card ace = allCards[4];

            allCards[4] = allCards[3];
            allCards[3] = allCards[2];
            allCards[2] = allCards[1];
            allCards[1] = allCards[0];
            allCards[0] = ace;
        }
        if (allCards.length==6 && !(allCards[5].isAce() && (isContained(allCards, "Jack") || isContained(allCards, "Queen") || isContained(allCards, "King")) && isContained(allCards, "Ten"))) {
            Card ace = allCards[5];

            allCards[5] = allCards[4];
            allCards[4] = allCards[3];
            allCards[3] = allCards[2];
            allCards[2] = allCards[1];
            allCards[1] = allCards[0];
            allCards[0] = ace;
        }
    }
    public int preFlopRiskToleranceHelper(Card[] hand){
        //the most advantage hand card
        if((hand[0].isAce() && hand[1].isAce()) || (hand[0].isKing() && hand[1].isKing()) || (hand[0].isQueen() && hand[1].isQueen()) || (hand[0].isJack() && hand[1].isJack())){
            //TODO: affect the riskTolerance
            return 2;
        }
        //these hand cards maybe can form strong straight
        else if((isContained(hand, "Ace") && isContained(hand, "King")) ||
                (isContained(hand, "Queen") && isContained(hand, "King")) ||
                (isContained(hand, "Queen") && isContained(hand, "Ace")) ||
                (isContained(hand, "Jack") && isContained(hand, "Queen")) ||
                (isContained(hand, "Jack") && isContained(hand, "King")) ||
                (isContained(hand, "Jack") && isContained(hand, "Ace"))){
            //TODO: affect the riskTolerance
            return 12;
        }
        //these hand cards maybe can form strong straightFlush
        else if((isContained(hand, "Ace") && isContained(hand, "King") && suitsOnHandAreSame(hand)) ||
                (isContained(hand, "Queen") && isContained(hand, "King") && suitsOnHandAreSame(hand)) ||
                (isContained(hand, "Queen") && isContained(hand, "Ace") && suitsOnHandAreSame(hand)) ||
                (isContained(hand, "Jack") && isContained(hand, "Queen") && suitsOnHandAreSame(hand)) ||
                (isContained(hand, "Jack") && isContained(hand, "King") && suitsOnHandAreSame(hand)) ||
                (isContained(hand, "Jack") && isContained(hand, "Ace")) && suitsOnHandAreSame(hand)){
            //TODO: affect the riskTolerance
            return 7;
        }
        //TODO: may need to do some modify
        else{
            return 14;
        }
    }
    public int riverRoundRiskToleranceHelper(Card[] publicCards, Card[] handCards, DeckOfCards deck){
        PokerHand publicHand = new PokerHand(publicCards, deck);
        PokerHand playerHand = new PokerHand(handCards, deck);
        String[] nameOrder = new String[]{"Deuce", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};

        int length = publicCards.length+2;
        Card[] allCards = new Card[length];
        System.arraycopy(publicCards, 0, allCards, 0, publicCards.length);
        System.arraycopy(super.getHand().getHand(), 0, allCards, publicCards.length, super.getHand().getHand().length);
        //we sort all cards from left to right, card with highest value is on right
        sortCards(allCards);
        sortCards(publicCards);
        Method[] preds = PokerHand.class.getDeclaredMethods();
//        int bestOdd = 0;
        for (Method pred : preds) {
            try {
                if (pred.getName().startsWith("is") && pred.getParameterTypes().length == 0 &&
                        pred.getReturnType() == Boolean.class) {
                    if((Boolean) pred.invoke(publicHand, new Object[0])){
                        String handType = pred.getName().substring(2);
                        if(handType.equals("RoyalStraightFlush") || handType.equals("StraightFlush") || handType.equals("FullHouse") || handType.equals("Flush") || handType.equals("Straight")){
                            return getHandValue(handType);
                        }else if(handType.equals("FourOfAKind")){
                            int count=0;
                            //cardNames store those cards that are not FourOfAKind
                            ArrayList<String> cardNames = new ArrayList<>();
                            String tem = allCards[0].getName();
                            for(int i=1; i<allCards.length; i++){
                                if(Objects.equals(allCards[i].getName(), tem)){
                                    count++;
                                }else if(count!=3){
                                    cardNames.add(tem);
                                    tem = allCards[i].getName();
                                    count=0;
                                }
                                if(count==3 && !Objects.equals(allCards[i].getName(), tem)){
                                    cardNames.add(allCards[i].getName());
                                    tem = allCards[i].getName();
                                }
                            }
                            //TODO: 这个riskTolerance也许需要修改
                            int risk=0;
                            String cardName = cardNames.get(cardNames.size()-1);
                            for(int i=0; i<nameOrder.length; i++){
                                if(nameOrder[i].equals(cardName)){
                                    if(i>=10){
                                        risk=5;
                                    }
                                    else if(i>6){
                                        risk=10;
                                    }else {
                                        risk=20;
                                    }
                                }
                            }
                            return risk;
                        }else {
                            return getHandValue(handType);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    public int predicateRiskTolerance(){
        DeckOfCards deck = getDeckOfCards();
        Card[] publicCards = communityCards.toArray(new Card[communityCards.size()]);
        Rounds currentRound = getCurrentRound();
        int risk = 0;
        if(currentRound==Rounds.PRE_FLOP){
            risk += preFlopRiskToleranceHelper(super.getHand().getHand());
        }
        if(currentRound==Rounds.FLOP || currentRound==Rounds.TURN){
            //TODO: make returned best hand type to affect the riskTolerance
            risk += predicateBestHandTypeAndRisk(publicCards, deck, currentRound);
        }
        if(currentRound==Rounds.RIVER){
            //TODO: 1-see if public cards can form some hand type,
            //      2-compare the hand type A from public cards with the best hand type of all cards B,
            //      3-if A is higher than B, we need to consider the cards on hand, it will affect the riskTolerance
            //      4-if B is higher than A, we do not ned to consider the cards on hand, and change the riskTolerance
            risk += riverRoundRiskToleranceHelper(publicCards, super.getHand().getHand(), deck);
        }
        return risk;
    }

    //this will return the finally returned hand type(with use of random value)
    private String findHandHigherThanCurrentHand(Map<String, Integer> predictedHandType, String currentHandType){
        boolean isHigh = true;
        Map<String, Integer> higherHandType = new HashMap<>();
        String[] handTypeMap = new String[]{"RoyalStraightFlush", "StraightFlush", "FourOfAKind", "FullHouse", "Flush", "Straight", "ThreeOfAKind", "TwoPair", "Pair"};
        //see if hand type should be High or not
        for(Integer odds: predictedHandType.values()){
            if(odds!=0){
                isHigh=  false;
            }
        }
        if(isHigh){
            return "High";
        }

        //find those hand type higher than currentHandType in predictedHandType, and add those hand type with its odds to a hashMap
        for(String hand: handTypeMap){
            if(!hand.equals(currentHandType)){
                for(Map.Entry<String, Integer> entry1: predictedHandType.entrySet()){
                    if(Objects.equals(entry1.getKey(), hand)){
                        higherHandType.put(entry1.getKey(), entry1.getValue());
                    }
                }
            }else {
                break;
            }
        }
        //find those hand types that odds are greater than 30
        ArrayList<String> hands = new ArrayList<>();
        for(Map.Entry<String, Integer> entry1: higherHandType.entrySet()){
            if(entry1.getValue()>30){
                hands.add(entry1.getKey());
            }
        }
        //if there are no hands that odds are greater than 30, just return current hand type
        if(hands.isEmpty()) {
            return currentHandType;
        }
        //if yes, randomly find one and return it
        else {
            Random random = new Random();
            int index = random.nextInt(hands.size());
            return hands.get(index);
        }
    }

    //this method will predict the best hand type and return the risk affected by this best hand type
    public int predicateBestHandTypeAndRisk(Card[] publicCards, DeckOfCards deck, Rounds currentRound){
        int length = publicCards.length+2;
        Card[] allCards = new Card[length];
        System.arraycopy(publicCards, 0, allCards, 0, publicCards.length);
        System.arraycopy(super.getHand().getHand(), 0, allCards, publicCards.length, super.getHand().getHand().length);
        //we sort all cards from left to right, card with highest value is on right
        sortCards(allCards);

        String handType = "";
        this.findBestHand(publicCards, deck);

        //TODO: this one may have errors, because dont know if "this.getCurrentBestHand().getClass().getSimpleName()"
        //      and "handType = pred.getName().substring(6)" has same String format
        Map<String, Integer> handsHigherThanCurrentHand = new HashMap<>();
        String currentHandType = this.getCurrentBestHand().getClass().getSimpleName();
        Map<String, Integer> predictedHandType = new HashMap<>();
        Method[] preds = ComputerTexasPlayer.class.getDeclaredMethods();
//        int bestOdd = 0;
        for (Method pred : preds) {
            try {
                if (pred.getName().startsWith("odd") && pred.getParameterTypes().length != 0 &&
                        pred.getReturnType() == Integer.class) {
                    Integer test = (Integer) pred.invoke(this, publicCards, currentRound);
                    handType = pred.getName().substring(6);
                    predictedHandType.put(handType, test);
//                    if (bestOdd <= test) {
//                        bestOdd = test;
//                        handType = pred.getName().substring(6);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //find those predicted hand type that are higher than current hand type
        //if the odds of predicted hand(higher than current hand) is greater than 40,
        return getHandValue(findHandHigherThanCurrentHand(predictedHandType, currentHandType));
//        return getHandValue(handType);
    }

    private int getHandValue(String handType) {
        switch (handType) {
            case "RoyalStraightFlush" -> {
                return PokerHand.ROYALFLUSH_RISK;
            }
            case "StraightFlush" -> {
                return PokerHand.STRAIGHTFLUSH_RISK;
            }
            case "Straight" -> {
                return PokerHand.STRAIGHT_RISK;
            }
            case "FourOfAKind" -> {
                return PokerHand.FOURS_RISK;
            }
            case "Flush" -> {
                return PokerHand.FLUSH_RISK;
            }
            case "FullHouse" -> {
                return PokerHand.FULLHOUSE_RISK;
            }
            case "ThreeOfAKind" -> {
                return PokerHand.THREES_RISK;
            }
            case "TwoPair" -> {
                return PokerHand.TWOPAIR_RISK;
            }
            case "Pair" -> {
                return PokerHand.PAIR_RISK;
            }
            default ->{
                return PokerHand.HIGHCARD_RISK;
            }
//            case "High" -> {
//            }
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
    public Integer oddsOfRoyalFlush(Card[] allCards, Rounds currentRound){
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
                    odds=outs*2;
                    break;
                }else if(count==4){
                    outs=1;
                    odds=outs*2;
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
    public Integer oddsOfStraightFlush(Card[] allCards, Rounds currentRound){
        double oddsOfStraight = oddsOfStraight(allCards, currentRound)*0.01;
        double oddsOfFlush = oddsOfFlush(allCards, currentRound)*0.01;
        double oddsOfStraightFlush = 0.0;
        if(oddsOfStraight!=0 && oddsOfFlush!=0){
            oddsOfStraightFlush = (oddsOfStraight*oddsOfFlush)*100;
        }
        return (int) oddsOfStraightFlush;
    }

    /*--------------------predicate the odds of Four of a kind--------------------------*/
    public Integer oddsOfFourOfAKind(Card[] allCards, Rounds currentRound){
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
                odds = outs*2;
            }
            if(countContains(names, 2)==2){
                outs=2;
                odds=outs*2;
            }
            if(countContains(names, 3)==1 && countContains(names, 1)==2){
                outs=1;
                odds=outs*2;
            }
            if(countContains(names, 3)==1 && countContains(names, 2)==1){
                outs=2;
                odds=outs*2;
            }
            if(countContains(names, 4)==1){
                odds=100;
            }
        }
        //if there are more than 3 identical cards, then it is possible to form four of a kind in turn round
        if(currentRound==Rounds.TURN){
            int outs = 0;
            if(countContains(names, 3)==1){
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
    public Integer oddsOfFullHouse(Card[] allCards, Rounds currentRound){
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
                outs = 20;
                odds = outs*2-(outs-8);
            }
            if(countContains(names, 2)==2){
                outs=4;
                odds = outs*2;
            }
            if(countContains(names, 3)==1 && countContains(names, 1)==2){
                outs = 6;
                odds = outs*2;
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
    public Integer oddsOfFlush(Card[] allCards, Rounds currentRound){
        //if there are more than 3 cards with same suits, then it is possible to form flush in flop round
        int odds=0;
        Map<String, Integer> suits = new HashMap<>();
        for(Card card: allCards){
            String suit = card.getSuit();
            suits.put(suit, suits.getOrDefault(suit, 0)+1);
        }
        //from flop to turn
        if(currentRound==Rounds.FLOP){
            int outs = 0;
            for(int count: suits.values()){
                if(count>=3 && count<5){
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
        //if there are more than 4 cards with same suits, then it is possible to form flush in turn round

        //from turn to river
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
                if(count>=5){
                    odds=100;
                    break;
                }
            }
        }
        return odds;
    }

    /*--------------------predicate the odds of Straight--------------------------*/
    public Integer oddsOfStraight(Card[] allCards, Rounds currentRound){
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
            //if the last card is Ace, we need to call getValue
            for(int i=0; i<allCards.length; i++){
                allCardsValue.add(allCards[i].getValue());
            }
        }else if(allCards[0].isAce()){
            fastIndex=1;
            slowIndex=1;
            oneSuitOfCards = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
            difference = findCardsThatNotInOneSuitOfCards(allCards, fastIndex, slowIndex, oneSuitOfCards);
            //if the last card is Ace, we need to call getRank
            for(int i=0; i<allCards.length; i++){
                allCardsValue.add(allCards[i].getRank());
            }
        }else {
            oneSuitOfCards = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
            difference = findCardsThatNotInOneSuitOfCards(allCards, fastIndex, slowIndex, oneSuitOfCards);
        }

        //int[] difference = diff.stream().mapToInt(Integer::intValue).toArray();

        //if allCards already contains straight, just return 100
        if(canFormAStraight(allCardsValue)){
            return 100;
        }
        ArrayList<Integer> record = new ArrayList<>();//this will record those combinations that can form a straight with allCards(hand cards + public cards)
        for(ArrayList<Integer> integer: generateCombinations(difference, currentRound)){
            ArrayList<Integer> temp = new ArrayList<>(allCardsValue);
            temp.addAll(integer);
            Collections.sort(temp);//after we add the combination value to allCardsValue, we need to sort it again
//            System.out.println("temp = "+temp);
            if(canFormAStraight(temp)){//if the combination can form a straight with allCards(hand cards + public cards)
//                System.out.println("temp = "+temp);
//                System.out.println("true");
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
//            System.out.println("recode size = "+record.size());
//            System.out.println(record);

            outs = record.size()*4;//because there are four different suits
            if(currentRound==Rounds.FLOP){
                if(outs>8){
                    odds = (outs*2)-(outs-8);
                }else {
                    odds = outs*2;
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
            if(temp.get(i)+1==temp.get(i+1)){
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
            if(slowIndex<allCards.length && oneSuitOfCards[fastIndex]==allCards[slowIndex].getValue()){
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
    public Integer oddsOfThreeOfAKind(Card[] allCards, Rounds currentRound){
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
                odds = outs*2;
            }
            //must be Three of a kind
            if(countContains(names, 3)==1 && countContains(names, 2)==0){
                odds=100;
//                outs=10;
//                odds=outs*2-(outs-8);
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
                odds = outs*2-(outs-8);
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
                odds=100;
//                outs = 9;
//                odds = outs*2-(outs-8);
            }

        }
        return odds;
    }

    /*--------------------predicate the odds of Two Pair--------------------------*/
    public Integer oddsOfTwoPair(Card[] allCards, Rounds currentRound){
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
                odds = outs*2-(outs-8);
            }
            if(countContains(names, 2)==1 && countContains(names, 1)==3){
                outs=9;
                odds=outs*2-(outs-8);
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
    public Integer oddsOfPair(Card[] allCards, Rounds currentRound){
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
                odds = outs*2-(outs-8);
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
//            if(countContains(names, 3)==1 && countContains(names, 1)==3){
//                odds=100;
//            }
        }
        return odds;
    }

    @Override
    public void raiseBet(PotOfMoney pot) {
        // pot current stake is either 0 or greater than big blind amount
        if (pot.getCurrentStake() > bank || RoundController.BIG_BLIND_AMOUNT > bank) return;

        int raiseAmount;

        if (pot.getCurrentStake() == 0) {
            raiseAmount = RoundController.BIG_BLIND_AMOUNT;
        } else {
            raiseAmount = pot.getCurrentStake();
        }

        stake += raiseAmount;
        bank -= raiseAmount;

        pot.raiseStake(raiseAmount);

        System.out.println("\n> " + getName() + " says: I raise by " + raiseAmount + " chips!\n");
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
        if (getStake() == 0) {
            System.out.println("getStake() in computer player = "+getStake());
            return true;
        }
        else {
//            System.out.println("Return: " + (Math.abs(dice.nextInt()) % 100 < getCurrentBestHand().getRiskWorthiness() +
//                    getRiskTolerance() + 10000000));
            return Math.abs(dice.nextInt()) % 100 < getCurrentBestHand().getRiskWorthiness() +
                    getRiskTolerance() + 10000000;
        }
    }

    public boolean shouldRaise(PotOfMoney pot) {
        return Math.abs(dice.nextInt())%80 < getCurrentBestHand().getRiskWorthiness() +
                getRiskTolerance();
    }

    public boolean shouldCheck(PotOfMoney pot) {
        int value = getCurrentBestHand().getRiskWorthiness() +
                getRiskTolerance();
//        System.out.println("getCurrentBestHand().getRiskWorthiness() = "+getCurrentBestHand().getRiskWorthiness());
//        System.out.println("getRiskTolerance() = "+getRiskTolerance());
//        System.out.println("value = "+value);
//        System.out.println("dice.nextInt())%80 = "+Math.abs(dice.nextInt())%80);
        return Math.abs(dice.nextInt())%100 < getCurrentBestHand().getRiskWorthiness() +
                getRiskTolerance();
        // + 100000000
    }

    public boolean shouldAllIn(PotOfMoney pot) {
        return Math.abs(dice.nextInt())%50 < getCurrentBestHand().getRiskWorthiness() +
                getRiskTolerance();
    }

     private boolean suitsOnHandAreSame(Card[] hand){
        return hand[0].getSuit().equals(hand[1].getSuit());
    }
}
