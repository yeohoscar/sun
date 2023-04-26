package texas.scramble.player;

import poker.Card;
import poker.DeckOfCards;
import poker.PotOfMoney;
import texas.Action;
import texas.RoundController;
import texas.Rounds;
import texas.TexasComputerPlayer;
import texas.scramble.deck.DeckOfTiles;
import texas.scramble.dictionary.DictionaryTrie;
import texas.scramble.dictionary.FullDictionary;
import texas.scramble.deck.Tile;

import java.util.*;

import static texas.Action.*;
import static texas.Action.FOLD;

public class ComputerScramblePlayer extends TexasComputerPlayer {
    public static final int VARIABILITY = 100;
    public final int averageHandValue = 14;

    //TODO: this average score needs to calculate, current value is estimated value
    public int averageCommunityLettersScore = 0;
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

    //TODO: not done
    public int getRiskTolerance() {
        int risk = 0;
        //risk = riskTolerance - getStake() + predicateRiskTolerance();
        risk = riskTolerance + predicateRiskTolerance();

        return risk; // tolerance drops as stake increases
    }


    //TODO: not done
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
                case 'J', 'X' -> score += 8;
                case 'Q', 'Z' -> score += 9;
                default -> score += 10;
            }
        }
        return score;
    }

    /********************** predicate riskTolerance of different round ************************/
    //TODO: not done
    public int predicateRiskTolerance() {
//        DeckOfCards deck = getDeckOfCards();
        Tile[] publicCards = getCommunityTiles().toArray(new Tile[getCommunityTiles().size()]);
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





    /******************** predicate pre-flop round ********************/
    private int calculateHandScore(Tile[] hand) {
        int score = 0;
        for (Tile tile : hand) {
            switch (tile.name()) {
                case "E", "A", "I", "O", "N", "R", "T", "L", "S", "U" -> score += 1;
                case "D", "G" -> score += 2;
                case "B", "C", "M", "P" -> score += 3;
                case "F", "H", "V", "W", "Y" -> score += 4;
                case "K" -> score += 5;
                case "J", "X" -> score += 8;
                case "Q", "Z" -> score += 9;
                default -> score += 10;
            }
        }
        return score;
    }

    public int preFlopRiskToleranceHelper(Tile[] hand) {
        if (calculateHandScore(hand) >= averageHandValue) {
            //TODO: the probability of taking raise action is high, just determine the risk value
        } else {
            //TODO: the probability of taking raise action is low, just determine the risk value
        }
        return 0;
    }

    /********************** predicate flop and turn round ************************/
    //TODO: not done
    public int predicateBestWordAndRisk(Tile[] publicCards, Rounds currentRound) {
        //TODO: have not combine community letters and letters on hand
        FullDictionary dict = FullDictionary.getFullDictionary();

        ArrayList<String> letters = new ArrayList<>();//store letters both from community letters and letters on hand
        String[] lettersOnHand = letters.toArray(new String[0]);

        ArrayList<String> community = new ArrayList<>();
        String[] communityLetters = community.toArray(new String[0]);

        //TODO:  average score of community letters score should be calculated before flop/turn round start
        /***************** average score of community letters score should be calculated before flop/turn round start *******************/
        //calculate average score of current community letters score
        float averageCommunityLettersScore = averageScoreCalculator(dict.findAllWords(communityLetters));
        /*************************************************************/
        //calculate average score of words that can be formed by players' current letters
        float averageScore = averageScoreCalculator(dict.findAllWords(lettersOnHand));
//        averageScore = Math.round((float) averageScore/totalWordNumber);
        if (averageScore >= averageCommunityLettersScore) {
            //TODO: determine the risk
        } else {
            //TODO: determine the risk
        }
        return 0;
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
    //TODO: not done
    public int riverRoundRiskToleranceHelper(Tile[] publicCards, DeckOfCards deck) {
        //TODO: have not combine community letters and letters on hand
        DictionaryTrie dict = FullDictionary.getFullDictionary();

        ArrayList<String> letters = new ArrayList<>();//store letters both from community letters and letters on hand
        String[] lettersOnHand = letters.toArray(new String[0]);

        ArrayList<String> community = new ArrayList<>();
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
        } else {
            //TODO: determine the risk
        }
        return 0;
    }

    /**********************|||||||||||||************************/

    //TODO: not done
    public Action chooseAction(PotOfMoney pot) {
        if (shouldAllIn(pot)) return ALL_IN;
        if (shouldRaise(pot)) return RAISE;
        if (shouldSee(pot)) return SEE;
        return FOLD;
    }

    @Override
    public boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    protected boolean shouldSee(PotOfMoney pot) {
        if (pot.getCurrentStake() - stake > bank) {
            return false;
        } else {
            return Math.abs(dice.nextInt()) % 120 < getCurrentBestHand().getRiskWorthiness() +
                    getRiskTolerance();
        }
    }

    @Override
    protected boolean shouldRaise(PotOfMoney pot) {
        if (bank < pot.getCurrentStake() * 2 - stake || bank < RoundController.BIG_BLIND_AMOUNT) {
            return false;
        }
        return Math.abs(dice.nextInt()) % 120 < getCurrentBestHand().getRiskWorthiness() +
                getRiskTolerance();
    }

    protected boolean shouldAllIn(PotOfMoney pot) {
        return Math.abs(dice.nextInt()) % 150 < getCurrentBestHand().getRiskWorthiness() +
                getRiskTolerance();
    }


}