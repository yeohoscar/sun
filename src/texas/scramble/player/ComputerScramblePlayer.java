package texas.scramble.player;

import poker.Card;
import poker.PotOfMoney;
import texas.Action;
import texas.RoundController;
import texas.Rounds;
import texas.TexasComputerPlayer;
import texas.scramble.deck.DeckOfTiles;
import texas.scramble.dictionary.DictionaryTrie;
import texas.scramble.dictionary.FullDictionary;
import texas.scramble.deck.Tile;
import texas.scramble.hand.HandElement;

import java.util.*;

import static texas.Action.*;
import static texas.Action.FOLD;

public class ComputerScramblePlayer extends TexasComputerPlayer {
    public static final int VARIABILITY = 100;
    public final int averageHandValue = 4;
    private int riskTolerance;  // willingness of a player to take risks and bluff
    private Random dice = new Random(System.currentTimeMillis());
    private DeckOfTiles deckOfTiles = new DeckOfTiles();

    private DictionaryTrie dict;

    public ComputerScramblePlayer(String name, int money, int id) {
        super(name, money, id);

        dict = new DictionaryTrie("resources/hard.txt");

        riskTolerance = Math.abs(dice.nextInt()) % VARIABILITY
                - VARIABILITY / 2;
        // this gives a range of tolerance between -VARIABILITY/2 to +VARIABILITY/2
    }

    public ComputerScramblePlayer(String name, int money, int id, String pathToDictionary) {
        super(name, money, id);

        dict = new DictionaryTrie(pathToDictionary);

        riskTolerance = Math.abs(dice.nextInt()) % VARIABILITY
                - VARIABILITY / 2;
        // this gives a range of tolerance between -VARIABILITY/2 to +VARIABILITY/2
    }

    public ComputerScramblePlayer(String name, int money, int id, String pathToDictionary, int riskTolerance) {
        super(name, money, id);
        dict = new DictionaryTrie(pathToDictionary);
        this.riskTolerance = riskTolerance;
    }

    public boolean knowsWord(String word) {
        return dict.isValidWord(word);
    }

    public void learnWord(String word) {
        dict.add(word);
    }

    public int getRiskTolerance() {
        int risk = 0;
        while (riskTolerance<=5){
            riskTolerance = Math.abs(dice.nextInt()) % VARIABILITY
                    - VARIABILITY / 2;
        }
        System.out.println("riskTolerance = "+riskTolerance);
        System.out.println("getStake() = "+getStake());
        risk = riskTolerance - getStake() + predicateRiskTolerance();
//        risk = riskTolerance + predicateRiskTolerance();

        return risk; // tolerance drops as stake increases
    }


    public Card getCard(int num, Card[] hand) {
        if (num >= 0 && num < hand.length) {
            return hand[num];
        } else {
            return null;
        }
    }

    public float averageScoreCalculator(List<String> allWords) {
        int averageScore = 0;
        for (String word : allWords) {
            averageScore += calculateWordScore(word);
        }
        return (float) averageScore / allWords.size();
    }

    /******************** just ignore this part ********************/
    public ArrayList<String> findAll(ArrayList<String> average) {
        ArrayList<String> combinations = new ArrayList<>();
        for (int i = 0; i < average.size(); i++) {
            for (int j = i + 1; j < average.size(); j++) {
                combinations.add(average.get(i) + " " + average.get(j));
            }
        }
        return combinations;
    }

    public int average() {
        int total = 0;
        ArrayList<String> average = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : deckOfTiles.getAllTiles().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                average.add(entry.getKey());
            }
        }
        System.out.println("average.size = " + average.size());
        ArrayList<String> all = findAll(average);
        for (String hand : all) {
            total += calculateHandScore1(hand);
        }
        System.out.println("total value = " + total);
        System.out.println("all.size = " + all.size());
        return total / all.size();
    }

    private int calculateHandScore1(String hand) {
        int score = 0;
        for (char tile : hand.toCharArray()) {
            switch (tile) {
                case 'E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U' -> score += 1;
                case 'D', 'G' -> score += 2;
                case 'B', 'C', 'M', 'P' -> score += 3;
                case 'F', 'H', 'V', 'W', 'Y' -> score += 4;
                case 'K' -> score += 5;
                case 'J', 'X' -> score += 6;
                case 'Q', 'Z' -> score += 7;
                default -> score += 5;
            }
        }
        return score;
    }

    /********************** predicate riskTolerance of different rounds ************************/
    public int predicateRiskTolerance() {
//        DeckOfCards deck = getDeckOfCards();
//        ArrayList<Tile> tileArray = new ArrayList<>();
//        if(!getCommunityElements().isEmpty()){
//            Tile[] publicCards = tileArray.toArray(new Tile[tileArray.size()]);
//        }
        Tile[] publicCards = getCommunityElements().toArray(new Tile[getCommunityElements().size()]);
        Rounds currentRound = getCurrentRound();
        int risk = 0;
        if (currentRound == Rounds.PRE_FLOP) {
            risk += preFlopRiskToleranceHelper(super.getHand().getHand());
        }

        if (currentRound == Rounds.FLOP || currentRound == Rounds.TURN) {
            risk += predicateBestWordAndRisk(publicCards, currentRound);
        }

        if (currentRound == Rounds.RIVER) {
            risk += riverRoundRiskToleranceHelper(publicCards, deckOfTiles);
        }
        return risk;
    }
    public String[] combineCommunityAndLettersOnHand(Tile[] publicCards, HandElement[] lettersOnNHand){
        ArrayList<String> letters = new ArrayList<>();//store letters both from community letters and letters on hand
        for(Tile communityLetter: publicCards){
            letters.add(communityLetter.name());
        }
        for(HandElement letterOnHand: lettersOnNHand){
            letters.add(letterOnHand.toString());
        }
        return letters.toArray(new String[0]);
    }


    /******************** predicate pre-flop round ********************/
    private int calculateHandScore(HandElement[] hand) {
        int score = 0;
        for (HandElement handElement : hand) {
            switch (handElement.toString()) {
                case "E", "A", "I", "O", "N", "R", "T", "L", "S", "U" -> score += 1;
                case "D", "G" -> score += 2;
                case "B", "C", "M", "P" -> score += 3;
                case "F", "H", "V", "W", "Y" -> score += 4;
                case "K" -> score += 5;
                case "J", "X" -> score += 6;
                case "Q", "Z" -> score += 7;
                default -> score += 5;
            }
        }
        return score;
    }

    public int preFlopRiskToleranceHelper(HandElement[] hand) {
        int risk = 0;
        if (calculateHandScore(hand) >= averageHandValue) {
            //TODO: the probability of taking raise action is high, just determine the risk value
            risk = 15;
        } else {
            //TODO: the probability of taking raise action is low, just determine the risk value
            risk = 6;
        }
        System.out.println("risk = "+risk);
        return risk;
    }

    /********************** predicate flop and turn round ************************/
    public int predicateBestWordAndRisk(Tile[] publicCards, Rounds currentRound) {
        int risk = 0;
        FullDictionary dict = FullDictionary.getFullDictionary();
        //combine community letters and letters on hand
        String[] lettersOnHand = combineCommunityAndLettersOnHand(publicCards, this.getHand().getHand());
        //obtain community letters
        ArrayList<String> community = new ArrayList<>();
        for(Tile publicCard: publicCards){
            community.add(publicCard.name());
        }
        String[] communityLetters = community.toArray(new String[0]);
        /***************** average score of community letters score should be calculated before flop/turn round start *******************/
        //calculate average score of current community letters score
        float averageCommunityLettersScore = averageScoreCalculator(dict.findAllWords(communityLetters));
        /*************************************************************/
        //calculate average score of words that can be formed by players' current letters
        float averageScore = averageScoreCalculator(dict.findAllWords(lettersOnHand));
//        averageScore = Math.round((float) averageScore/totalWordNumber);
        if (averageScore >= averageCommunityLettersScore) {
            //TODO: determine the risk
            risk = 30;
        } else {
            //TODO: determine the risk
            risk = 15;
        }
        return risk;
        /*//3-find the highest score word of all combination, then compare it with a value(not calculated yet)
        HashMap<String, Integer> theFinalWord = new HashMap<>(findHighestScoreWordHelper(highestWords));
        int theFinalWordScore = 0;
        String theFinalWordSpelling = "^";
        for(Map.Entry<String, Integer> entry: theFinalWord.entrySet()){
            theFinalWordSpelling = entry.getKey();
            theFinalWordScore = entry.getValue();
        }
        if(theFinalWordScore>averageCommunityLettersScore){
            //TODO: determine the risk
        }
        else {
            //TODO: determine the risk
        }
        return 0;*/
    }

    /********************** predicate river round ************************/
    public int riverRoundRiskToleranceHelper(Tile[] publicCards, DeckOfTiles deck) {
        int risk = 0;
        DictionaryTrie dict = FullDictionary.getFullDictionary();
        //combine community letters and letters on hand
        String[] lettersOnHand = combineCommunityAndLettersOnHand(publicCards, this.getHand().getHand());
        //obtain community letters
        ArrayList<String> community = new ArrayList<>();
        for(Tile publicCard: publicCards){
            community.add(publicCard.name());
        }
        String[] communityLetters = community.toArray(new String[0]);

        /***************** average score of community letters score should be calculated before flop/turn round start *******************/
        //calculate average score of current community letters score
        float averageCommunityLettersScore = averageScoreCalculator(dict.findAllWords(communityLetters));
        /*************************************************************/
        //calculate highest score word that can be formed by players' current letters
        String highestScoreWord = findHighestScoreWord(lettersOnHand, dict);
        float wordScore = calculateWordScore(highestScoreWord);
//        averageScore = Math.round((float) averageScore/totalWordNumber);
        if (wordScore >= averageCommunityLettersScore) {
            //TODO: determine the risk
            risk = 35;
        } else {
            //TODO: determine the risk
            risk = 16;
        }
        return risk;
    }

    /**********************|||||||||||||************************/
    public HashMap<String, Integer> submitWords(String[] lettersOnHand){
//        String[] lettersOnHand = combineCommunityAndLettersOnHand(publicCards, this.getHand().getHand());
        HashMap<String, Integer> submitWords = new HashMap<>();
        boolean lettersFinished = false;
        HashMap<String, Integer> lettersContained = new HashMap<>();
        for(String letter: lettersOnHand){
            if(lettersContained.containsKey(letter)){
                lettersContained.put(letter, lettersContained.get(letter)+1);
            }else {
                lettersContained.put(letter, 1);
            }
        }
        String word = findHighestScoreWord(lettersOnHand, dict);
        String[] word1 = word.split("");
        submitWords.put(word, calculateWordScore(word));
        while (!lettersFinished){
            if(word.length()==7){
                lettersFinished = true;
            }else {
//                System.out.println("word = "+word);
                //filter out remaining letters
                for(String letter: word1){
                    if(lettersContained.containsKey(letter)){
                        lettersContained.put(letter, lettersContained.get(letter)-1);
                    }else {
                        lettersContained.put(" ", lettersContained.get(" ")-1);
                    }
                }
//                for(Map.Entry<String, Integer> entry: lettersContained.entrySet()){
//                    if(entry.getValue()!=0){
//                        System.out.println("remaining letters = "+entry.getKey());
//                        System.out.println("number of this letter = "+entry.getValue());
//                    }
//                }
                if(noMoreRemainingLetters(lettersContained)){
                    return submitWords;
                }
                //put remaining letters to a String[] array
                ArrayList<String> remainingLetters = new ArrayList<>();
                for(Map.Entry<String, Integer> entry: lettersContained.entrySet()){
                    if(entry.getValue()>0){
                        for(int i=0; i<entry.getValue(); i++){
                            remainingLetters.add(entry.getKey());
                        }
                    }
                }
                String[] remainingLetters1 = new String[remainingLetters.size()];
                remainingLetters.toArray(remainingLetters1);
                //find highest score word with remaining letters
                word = findHighestScoreWord(remainingLetters1, dict);
                word1 = word.split("");
                if(!word.equals("^")){
                    submitWords.put(word, calculateWordScore(word));
                }else {
                    return submitWords;
                }
            }
        }
        return submitWords;
    }
    public boolean noMoreRemainingLetters(HashMap<String, Integer> remainingLetters){
        boolean empty = true;
        for(Map.Entry<String, Integer> entry: remainingLetters.entrySet()){
            if(entry.getValue()!=0){
                empty=false;
            }
        }
        return empty;
    }
    /**********************|||||||||||||************************/

    //TODO: not done
    public Action chooseAction(PotOfMoney pot) {
//        if (shouldRaise(pot)) return RAISE;
//        if (shouldSee(pot)) return SEE;
//        if (shouldAllIn(pot)) return ALL_IN;
//        return FOLD;
        ArrayList<Action> action = new ArrayList<>();
        if (shouldRaise(pot)){
             action.add(RAISE);
        }
        if (shouldSee(pot)){
            action.add(SEE);
        }
        if (shouldAllIn(pot) && action.size()==0){
             action.add(ALL_IN);
        }
        if(action.size()==1){
            return action.get(0);
        }else if(action.size()==0){
            return FOLD;
        }else {
            Random random = new Random();
            int index = random.nextInt(action.size());
            return action.get(index);
        }
    }

    @Override
    public boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    protected boolean shouldSee(PotOfMoney pot) {
        if (pot.getCurrentStake() - stake > bank) {
            return false;
//            return -1;
        } else {
            int risk = getRiskTolerance();
            int random = Math.abs(dice.nextInt()) % 60;
            System.out.println("shouldSee getRiskTolerance() = "+risk);
            System.out.println("getCurrentBestHand().getRiskWorthiness()+getRiskTolerance() = "+getCurrentBestHand().getRiskWorthiness() + risk);
            System.out.println("Math.abs(dice.nextInt()) % 60 = "+random);
            System.out.println();
//            return getCurrentBestHand().getRiskWorthiness()+risk-random;
            return random <= getCurrentBestHand().getRiskWorthiness() + risk;
//            return Math.abs(dice.nextInt()) % 120 < getCurrentBestHand().getRiskWorthiness() +
//                    getRiskTolerance();
        }
    }

    @Override
    protected boolean shouldRaise(PotOfMoney pot) {
        if (bank < pot.getCurrentStake() * 2 - stake || bank < RoundController.BIG_BLIND_AMOUNT) {
            return false;
//            return -1;
        }
        int risk = getRiskTolerance();
        int random = Math.abs(dice.nextInt()) % 60;
        System.out.println("shouldRaise getRiskTolerance() = "+risk);
        System.out.println("getCurrentBestHand().getRiskWorthiness()+getRiskTolerance() = "+getCurrentBestHand().getRiskWorthiness() + risk);
        System.out.println("Math.abs(dice.nextInt()) % 60 = "+random);
        System.out.println();
//        return getCurrentBestHand().getRiskWorthiness()+risk-random;
        return random <= getCurrentBestHand().getRiskWorthiness() + risk;
//        return Math.abs(dice.nextInt()) % 120 < getCurrentBestHand().getRiskWorthiness() +
//                getRiskTolerance();
    }

    protected boolean shouldAllIn(PotOfMoney pot) {
        int risk = getRiskTolerance();
        int random = Math.abs(dice.nextInt()) % 75;
        System.out.println("shouldAllIn getRiskTolerance() = "+risk);
        System.out.println("getCurrentBestHand().getRiskWorthiness()+getRiskTolerance() = "+getCurrentBestHand().getRiskWorthiness() + risk);
        System.out.println("Math.abs(dice.nextInt()) % 75 = "+random);
        System.out.println();
//        return getCurrentBestHand().getRiskWorthiness()+risk-random;
        return random<=getCurrentBestHand().getRiskWorthiness()+risk;
//        return Math.abs(dice.nextInt()) % 150 < getCurrentBestHand().getRiskWorthiness() +
//                getRiskTolerance();
    }


}