package texas_scramble;

import poker.Card;
import poker.DeckOfCards;
import poker.PokerHand;
import texas_hold_em.Rounds;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ComputerScramblePlayer {
    public static final int VARIABILITY = 100;
    /*public static int E, A, I, O, N, R, T, L, S, U= 1;
    public static int D, G = 2;
    public static int B, C, M, P = 3;
    public static int F, H, V, W, Y = 4;
    public static int K = 5;
    public static int J, X = 8;
    public static int Q, Z = 10;
    public static int BLANK = 0;*/

    private int riskTolerance;  // willingness of a player to take risks and bluff

    private Random dice	= new Random(System.currentTimeMillis());

    private List<Card> communityCards;

    public ComputerScramblePlayer(String name, int money,int id) {
        //super(name, money,id);

        riskTolerance = Math.abs(dice.nextInt())%VARIABILITY
                - VARIABILITY/2;

        // this gives a range of tolerance between -VARIABILITY/2 to +VARIABILITY/2
    }
    public int getRiskTolerance() {
        int risk = 0;
        //risk = riskTolerance - getStake() + predicateRiskTolerance();
        risk = riskTolerance + predicateRiskTolerance();

        return risk; // tolerance drops as stake increases
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
    public Card getCard(int num, Card[] hand) {
        if (num >= 0 && num < hand.length) {
            return hand[num];
        } else {
            return null;
        }
    }
    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    //TODO: may need sortCard method
    public int preFlopRiskToleranceHelper(Card[] hand) {
        //the most advantage hand card
        /*if ((hand[0].isAce() && hand[1].isAce())
                || (hand[0].isKing() && hand[1].isKing())
                || (hand[0].isQueen() && hand[1].isQueen())
                || (hand[0].isJack() && hand[1].isJack())) {
            return 2;
        }
        //these hand cards maybe can form strong straight
        else if ((isContained(hand, "Ace") && isContained(hand, "King")) ||
                (isContained(hand, "Queen") && isContained(hand, "King")) ||
                (isContained(hand, "Queen") && isContained(hand, "Ace")) ||
                (isContained(hand, "Jack") && isContained(hand, "Queen")) ||
                (isContained(hand, "Jack") && isContained(hand, "King")) ||
                (isContained(hand, "Jack") && isContained(hand, "Ace"))) {
            return 5;
        }
        //these hand cards maybe can form strong straightFlush
        else if ((isContained(hand, "Ace") && isContained(hand, "King") && suitsInHandAreSame(hand)) ||
                (isContained(hand, "Queen") && isContained(hand, "King") && suitsInHandAreSame(hand)) ||
                (isContained(hand, "Queen") && isContained(hand, "Ace") && suitsInHandAreSame(hand)) ||
                (isContained(hand, "Jack") && isContained(hand, "Queen") && suitsInHandAreSame(hand)) ||
                (isContained(hand, "Jack") && isContained(hand, "King") && suitsInHandAreSame(hand)) ||
                (isContained(hand, "Jack") && isContained(hand, "Ace")) && suitsInHandAreSame(hand)) {
            return 7;
        }else if(suitsInHandAreSame(hand)){
            return 9;
        }else if(isContained(hand, "Ace") || isContained(hand, "Jack") || isContained(hand, "Queen") || isContained(hand, "King") || isContained(hand, "Ten")){
            return 15;
        } else {
            return 22;
        }*/
        return 0;
    }
    public int riverRoundRiskToleranceHelper(Card[] publicCards, DeckOfCards deck) {
        /*PokerHand publicHand = new PokerHand(publicCards, deck);
        String[] nameOrder = new String[] {"Deuce", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};

        int length = publicCards.length + 2;
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
                    if ((Boolean) pred.invoke(publicHand, new Object[0])) {
                        String handType = pred.getName().substring(2);

                        if (handType.equals("RoyalStraightFlush") || handType.equals("StraightFlush") || handType.equals("FullHouse") || handType.equals("Flush") || handType.equals("Straight")) {
                            return getHandValue(handType);
                        } else if (handType.equals("FourOfAKind")) {
                            int count = 0;
                            //cardNames store those cards that are not FourOfAKind
                            ArrayList<String> cardNames = new ArrayList<>();
                            String tem = allCards[0].getName();

                            for (int i = 1; i < allCards.length; i++) {
                                if (Objects.equals(allCards[i].getName(), tem)) {
                                    count++;
                                } else if (count != 3) {
                                    cardNames.add(tem);
                                    tem = allCards[i].getName();
                                    count = 0;
                                }

                                if (count == 3 && !Objects.equals(allCards[i].getName(), tem)) {
                                    cardNames.add(allCards[i].getName());
                                    tem = allCards[i].getName();
                                }
                            }

                            int risk = 0;
                            String cardName = cardNames.get(cardNames.size() - 1);
                            for (int i = 0; i < nameOrder.length; i++) {
                                if (nameOrder[i].equals(cardName)) {
                                    if (i >= 10) {
                                        risk = 5;
                                    }
                                    else if (i > 6) {
                                        risk = 10;
                                    } else {
                                        risk = 20;
                                    }
                                }
                            }
                            return risk;
                        } else {
                            return getHandValue(handType);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        return 0;
    }

    public int predicateRiskTolerance() {
//        DeckOfCards deck = getDeckOfCards();
        Card[] publicCards = communityCards.toArray(new Card[communityCards.size()]);
        Rounds currentRound = getCurrentRound();
        int risk = 0;
        if (currentRound == Rounds.PRE_FLOP) {
//            risk += preFlopRiskToleranceHelper(super.getHand().getHand());
        }

        if (currentRound == Rounds.FLOP || currentRound == Rounds.TURN) {
            risk += predicateBestWordAndRisk(publicCards, currentRound);
        }

        if (currentRound == Rounds.RIVER) {
//            risk += riverRoundRiskToleranceHelper(publicCards, deck);
        }
        return risk;
    }
    public int predicateBestWordAndRisk(Card[] publicCards, Rounds currentRound){
        //TODO: 1-in flop round, predicate turn round, player has 5 cards(already known) and 1 card(should arise in turn round)
        //        predicate the best word with these 6 cards
        //      2-in turn round, predicate river round, player has 6 cards(already known) and 1 card(should arise in river round)
        //        predicate the best word with these 7 cards
        return 0;
    }
    public void findAllWordsHelper(List<List<String>> temp, String[] letters, List<List<String>> combinations, List<String> result, HashMap<String, Integer> duplicatedLetters){
        for (List<String> combination : combinations) {
            for (String letter : letters) {
                List<String> newCombination = new ArrayList<>(combination);
                newCombination.add(letter);
                if((!temp.contains(newCombination)) && checkDuplicated(duplicatedLetters, newCombination)){ //
                    temp.add(newCombination);
                    String word = newCombination.stream().collect(Collectors.joining());
                    if(DictionaryTrie.getDictionary().isValidWord(word)){
                        result.add(word);
                    }
                }
            }
        }
    }
    public boolean checkDuplicated(HashMap<String, Integer> duplicatedLetters, List<String> newCombination){
        int check=0;
        HashMap<String, Integer> newCombinationLetters = new HashMap<>();
        for(String letter: newCombination){
            if(newCombinationLetters.containsKey(letter)){
                newCombinationLetters.put(letter, newCombinationLetters.get(letter)+1);
            }else {
                newCombinationLetters.put(letter, 1);
            }
        }
//        System.out.println("*************** combination = "+newCombination);
//        for(Map.Entry<String, Integer> entry: newCombinationLetters.entrySet()){
//            System.out.println("newCombinationLetters   Char: "+entry.getKey()+" Count: "+entry.getValue());
//        }
//        System.out.println("***************");
        for(Map.Entry<String, Integer> entry1: newCombinationLetters.entrySet()){
            for(Map.Entry<String, Integer> entry2: duplicatedLetters.entrySet()){
                if(entry1.getKey().equals(entry2.getKey())){
                    if(entry1.getValue()>entry2.getValue()){
                        check=1;
                    }
                }
            }
        }
        return check == 0;
    }
    public List<String> findAllWords(String[] letters){
        HashMap<String, Integer> duplicatedLetters = new HashMap<>();

        //find those letters that arise multiple times in String[] letters
        for(String letter: letters){
            if(duplicatedLetters.containsKey(letter)){
                duplicatedLetters.put(letter, duplicatedLetters.get(letter)+1);
            }else {
                duplicatedLetters.put(letter, 1);
            }
        }
//        for(Map.Entry<String, Integer> entry: duplicatedLetters.entrySet()){
//            System.out.println("Char: "+entry.getKey()+" Count: "+entry.getValue());
//        }

        List<List<String>> combinations = new ArrayList<>();
        List<String> result = new ArrayList<>();
        // C(1, n)
        for (String letter : letters) {
            List<String> combination = new ArrayList<>();
            combination.add(letter);
            combinations.add(combination);
            String word = combination.stream().collect(Collectors.joining());
            if(DictionaryTrie.getDictionary().isValidWord(word)){
                result.add(word);
            }
        }
        // C(2, n) to C(n-1, n)
        List<List<String>> temp = new ArrayList<>();
        for(int i=2; i<letters.length; i++){
            findAllWordsHelper(temp, letters, combinations, result, duplicatedLetters);
            combinations = temp;
            temp = new ArrayList<>();
        }
        // C(n, n)
        temp = new ArrayList<>();
        for (List<String> combination : combinations) {
            for (String letter : letters) {
                List<String> newCombination = new ArrayList<>(combination);
                newCombination.add(letter);
                if(!temp.contains(newCombination) && checkDuplicated(duplicatedLetters, newCombination)){
                    temp.add(newCombination);
                    String word = newCombination.stream().collect(Collectors.joining());
                    if(DictionaryTrie.getDictionary().isValidWord(word)){
                        result.add(word);
                    }
                }
            }
        }
        System.out.println("*****************************************");
        for(String re: result){
            System.out.println(re);
        }
        System.out.println("result.size = "+result.size());
        return result;
    }


    //TODO: findAllWords will return all words that can be formed by current letters on players hand,
    //      and findHighestScoreWord will return the word with highest score among all words returned by findAllWords
    public String findHighestScoreWord(String[] letters){
        //find all words that these letters can form
        List<String> allWords = findAllWords(letters);

        //calculate score of each of these words
        HashMap<String, Integer> recordWordsScore = new HashMap<>();
        for(String word: allWords){
            recordWordsScore.put(word, calculateWordScore(word));
        }
        //find those words with highest score
        int maxScore = Collections.max(recordWordsScore.values());
        ArrayList<String> wordsWithMaxScore = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : recordWordsScore.entrySet()) {
            if (entry.getValue() == maxScore) {
                wordsWithMaxScore.add(entry.getKey());
            }
        }

        //if there is only one word with highest score, return this word,
        //otherwise, randomly choose one from those words with highest score
        if(wordsWithMaxScore.size()==1){
            return wordsWithMaxScore.get(0);
        }else {
            Random random = new Random();
            return wordsWithMaxScore.get(random.nextInt(wordsWithMaxScore.size()));
        }
    }
    public int calculateWordScore(String word){
        int score = 0;
        for(int i=0; i<word.length(); i++) {
            /*if (word.charAt(i) == 'E' || word.charAt(i) == 'A' || word.charAt(i) == 'I' || word.charAt(i) == 'O' || word.charAt(i) == 'N' || word.charAt(i) == 'R' || word.charAt(i) == 'T' || word.charAt(i) == 'L' || word.charAt(i) == 'S' || word.charAt(i) == 'U') {
                score += 1;
            }*/
            switch (word.charAt(i)) {
                case 'E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U' -> score += 1;
                case 'D', 'G' -> score+=2;
                case 'B', 'C', 'M', 'P' -> score+=3;
                case 'F', 'H', 'V', 'W', 'Y' -> score+=4;
                case 'K' -> score+=5;
                case 'J', 'X' -> score+=8;
                case 'Q', 'Z' -> score+=10;
                default -> score+=0;
            }
        }
        return score;
    }
}
