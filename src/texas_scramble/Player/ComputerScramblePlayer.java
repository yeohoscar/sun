package texas_scramble.Player;

import org.w3c.dom.Node;
import poker.Card;
import poker.DeckOfCards;
import poker.PotOfMoney;
import texas.Action;
import texas.RoundController;
import texas.Rounds;
import texas.TexasComputerPlayer;
import texas_scramble.Deck.DeckOfTiles;
import texas_scramble.Deck.DictionaryTrie;
import texas_scramble.Deck.Tile;

import java.util.*;
import java.util.stream.Collectors;

import static texas.Action.*;
import static texas.Action.FOLD;

public class ComputerScramblePlayer extends TexasComputerPlayer {
    public static final int VARIABILITY = 100;
    public final int averageHandValue = 14;

    //TODO: this average score needs to calculate, current value is estimated value
    public int averageCommunityLettersScore = 0;
    private int riskTolerance;  // willingness of a player to take risks and bluff
    private Random dice	= new Random(System.currentTimeMillis());
    private DeckOfTiles deckOfTiles = new DeckOfTiles();
    public ComputerScramblePlayer(String name, int money,int id) {
        super(name, money,id);

        riskTolerance = Math.abs(dice.nextInt())%VARIABILITY
                - VARIABILITY/2;
        // this gives a range of tolerance between -VARIABILITY/2 to +VARIABILITY/2
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

    /******************** just ignore this part ********************/
    public ArrayList<String> findAll(ArrayList<String> average){
        ArrayList<String> combinations = new ArrayList<>();
        for (int i = 0; i < average.size(); i++) {
            for (int j = i+1; j < average.size(); j++) {
                combinations.add(average.get(i) + " " + average.get(j));
            }
        }
        return combinations;
    }
    public int average(){
        int total = 0;
        ArrayList<String> average = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: deckOfTiles.getAllTiles().entrySet()){
            for(int i=0; i<entry.getValue(); i++){
                average.add(entry.getKey());
            }
        }
        System.out.println("average.size = "+average.size());
        ArrayList<String> all = findAll(average);
        for(String hand: all){
            total += calculateHandScore1(hand);
        }
        System.out.println("total value = "+total);
        System.out.println("all.size = "+all.size());
        return total/all.size();
    }
    private int calculateHandScore1(String hand){
        int score = 0;
        for(char tile: hand.toCharArray()){
            switch (tile) {
                case 'E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U' -> score += 1;
                case 'D', 'G' -> score+=2;
                case 'B', 'C', 'M', 'P' -> score+=3;
                case 'F', 'H', 'V', 'W', 'Y' -> score+=4;
                case 'K' -> score+=5;
                case 'J', 'X' -> score+=8;
                case 'Q', 'Z' -> score+=9;
                default -> score+=10;
            }
        }
        return score;
    }
    /******************** |||||||||||||| ********************/







    /**********************|||||||||||||************************/
    private int calculateHandScore(Tile[] hand){
        int score = 0;
        for(Tile tile: hand){
            switch (tile.name()) {
                case "E", "A", "I", "O", "N", "R", "T", "L", "S", "U" -> score += 1;
                case "D", "G" -> score+=2;
                case "B", "C", "M", "P" -> score+=3;
                case "F", "H", "V", "W", "Y" -> score+=4;
                case "K" -> score+=5;
                case "J", "X" -> score+=8;
                case "Q", "Z" -> score+=9;
                default -> score+=10;
            }
        }
        return score;
    }
    public int preFlopRiskToleranceHelper(Tile[] hand) {
        if(calculateHandScore(hand)>=averageHandValue){
            //TODO: the probability of taking raise action is high, just determine the risk value
        }
        else {
            //TODO: the probability of taking raise action is low, just determine the risk value
        }
        return 0;
    }

    //TODO: not done
    public int riverRoundRiskToleranceHelper(Tile[] publicCards, DeckOfCards deck) {

        return 0;
    }

    //TODO: not done
    public int predicateRiskTolerance() {
//        DeckOfCards deck = getDeckOfCards();
        Tile[] publicCards = getCommunityCards().toArray(new Tile[getCommunityCards().size()]);
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
    public void removeWordsWithZeroValue(HashMap<String, Integer> highestWords){
        String keyToRemove = "^";
        highestWords.entrySet().removeIf(entry -> entry.getKey().equals(keyToRemove));
    }
    //TODO: not done
    public int predicateBestWordAndRisk(Tile[] publicCards, Rounds currentRound){
        //TODO: have not combine community letters and letters on hand
        DictionaryTrie dict = DictionaryTrie.getDictionary();

        ArrayList<String> letters = new ArrayList<>();//store letters both from community letters and letters on hand
        String[] lettersOnHand = letters.toArray(new String[0]);

        ArrayList<String> community = new ArrayList<>();
        String[] communityLetters = community.toArray(new String[0]);

        ArrayList<String> allCombination = new ArrayList<>();
        HashMap<String, Integer> highestWords = new HashMap<>();
        if(currentRound==Rounds.FLOP){
            //calculate average score of current community letters score
            averageCommunityLettersScore = dict.calculateAverageScoreOfAllWordsContainCommunityLetters(communityLetters,this);
            //1-findAllCombination()
            allCombination = findAllCombination(lettersOnHand, 2);
        }else if(currentRound==Rounds.TURN){
            //calculate average score of current community letters score
            averageCommunityLettersScore = dict.calculateAverageScoreOfAllWordsContainCommunityLetters(communityLetters, this);
            //1-findAllCombination()
            allCombination = findAllCombination(lettersOnHand, 1);
        }
        //2-for each combination, call findHighestScoreWord(), find the highest score word of each combination
        for(String combination: allCombination){
            highestWords.putAll(findHighestScoreWord(combination, dict));
        }
        //remove those words like: ^, 0
        removeWordsWithZeroValue(highestWords);
        //3-find the average score of all combination and compare it with average score of current community letters
        int averageScore = 0;
        for(Map.Entry<String, Integer> entry: highestWords.entrySet()){
            averageScore += entry.getValue();
        }
        averageScore = averageScore/highestWords.size();
        if(averageScore>=averageCommunityLettersScore){
            //TODO: determine the risk
        }else {
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


    /**********************|||||||||||||************************/
    //this method is used to find all combinations that current community cards and cards on hand can combine with not arise cards
    //for example, there are 3 community cards and 2 cards on hand, to predicate river round, there are two cards space left,
    //different cards in the two space with community cards and cards on hand can have different combinations
    public ArrayList<String> findAllCombination(String[] lettersOnHand, int letterSpaceLeft){
        if(letterSpaceLeft==0){
            return new ArrayList<>(Arrays.asList(lettersOnHand));
        }else {
            ArrayList<String> availableLetters = findAvailableLetters(lettersOnHand);
            ArrayList<String> updateAvailableLetters = new ArrayList<>();
            ArrayList<String> letterCombination = availableLetters;
            HashMap<String, Integer> temp = new HashMap<>(deckOfTiles.getAllTiles());
//            System.out.println("before 1st level:");
//            for(Map.Entry<String, Integer> entry: temp.entrySet()){
//                System.out.print(entry+", ");
//            }
//            System.out.println();
            for(String letter: lettersOnHand){
                temp.put(letter, temp.get(letter)-1);
            }
//            System.out.println("1st level available letters:");
//            for(Map.Entry<String, Integer> entry: temp.entrySet()){
//                System.out.print(entry+", ");
//            }
//            System.out.println();
//            for(String letters: availableLetters){
//                System.out.print(letters);
//            }
//            System.out.println();
            while(letterSpaceLeft>1){
//                System.out.println("letterSpaceLet = "+letterSpaceLeft);
                //pre level: availableLetters
                for(String letter: availableLetters){
                    temp.put(letter, temp.get(letter)-1);
                }
                for(Map.Entry<String, Integer> entry: temp.entrySet()){
                    if(entry.getValue()>0){
                        //subs level
                        updateAvailableLetters.add(entry.getKey());
                    }
                }
//                System.out.println("updated:");
//                for(Map.Entry<String, Integer> entry: temp.entrySet()){
//                    System.out.print(entry+", ");
//                }
//                System.out.println();
//                for(String letters: updateAvailableLetters){
//                    System.out.print(letters);
//                }
//                System.out.println();
                //两层的结合
                letterCombination = combinationHelper(letterCombination, updateAvailableLetters);
                availableLetters.addAll(updateAvailableLetters);
                updateAvailableLetters.clear();
                letterSpaceLeft--;
            }
            //combine each of combination with letters on player's hand, each of the new combination will have the highest word
            String str = String.join("", lettersOnHand);
            for(int i=0; i<letterCombination.size(); i++){
                letterCombination.set(i, letterCombination.get(i)+str);
            }
            return letterCombination;
        }
    }
    private ArrayList<String> combinationHelper(ArrayList<String> pre, ArrayList<String> subs){
        ArrayList<String> letterCombination = new ArrayList<>();
        for (String s : pre) {
            for (String sub : subs) {
                letterCombination.add(s + sub);
            }
        }
        return letterCombination;
    }

////    *********************|||||||||||||***********************
//
//    //findHighestScoreWord will return the word with highest score among all words returned by findAllWords
//    public HashMap<String, Integer> findHighestScoreWord(String combination){
//        DictionaryTrie dict = DictionaryTrie.getDictionary();
//        char[] charArray = combination.toCharArray();
//        String[] letters = new String[charArray.length];
//
//        for (int i = 0; i < charArray.length; i++) {
//            letters[i] = Character.toString(charArray[i]);
//        }
//
//        if(containBlank(letters)){
//            substituteBlank(letters);
//        }
//        //find all words that these letters can form
//        List<String> allWords = dict.findAllWords(letters);
//        //System.out.println("words size = "+allWords.size());
//        //if all words is empty, this means current letters on player's hand cna not form any words
//        if(allWords.isEmpty()){
//            return new HashMap<>(Collections.singletonMap("^", 0));
//        }
//        //calculate score of each of these words
//        HashMap<String, Integer> recordWordsScore = new HashMap<>();
//        for(String word: allWords){
//            recordWordsScore.put(word, calculateWordScore(word));
//        }
//        HashMap<String, Integer> storeHighestScoreWords = new HashMap<>();
//
//        //find those words with highest score
//        int maxScore = Collections.max(recordWordsScore.values());
//        //System.out.println("maxScore = "+maxScore);
//        for (Map.Entry<String, Integer> entry : recordWordsScore.entrySet()) {
//            if (entry.getValue() == maxScore) {
//                storeHighestScoreWords.put(entry.getKey(), entry.getValue());
//            }
//        }
//        //if there is only one word with highest score, return this word,
//        //otherwise, randomly choose one from those words with highest score
//        if(storeHighestScoreWords.size()==1){
//            return storeHighestScoreWords;
//        }else {
//            List<String> keyList = new ArrayList<>(storeHighestScoreWords.keySet());
//            Random rand = new Random();
//            int randomIndex = rand.nextInt(keyList.size());
//            String randomKey = keyList.get(randomIndex);
//            return new HashMap<>(Collections.singletonMap(randomKey, storeHighestScoreWords.get(randomKey)));
//        }
//    }
//    private ArrayList<String> updateAvailableLetters(HashMap<String, Integer> temp){
//        ArrayList<String> availableLetters = new ArrayList<>();
//        for(Map.Entry<String, Integer> entry: temp.entrySet()){
//            if(entry.getValue()>0){
//                availableLetters.add(entry.getKey());
//            }
//        }
//        return availableLetters;
//    }
//    public void substituteBlank(String[] letters){
//        ArrayList<String> availableLetters = findAvailableLetters(letters);
//        HashMap<String, Integer> temp = new HashMap<>(deckOfTiles.getAllTiles());
//        for(int i=0; i<letters.length; i++){
//            if(letters[i].equals(" ")){
//                letters[i]=findHighestScoreLetter(availableLetters, temp);
//                availableLetters = updateAvailableLetters(temp);
//            }
//        }
//    }
//    private String findHighestScoreLetter(ArrayList<String> availableLetters, HashMap<String, Integer> temp){
//        int maxScore=0;
//        int score=0;
//        String maxScoreLetter = "A";
//        for(String letter: availableLetters){
//            switch (letter.charAt(0)) {
//                case 'E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U' -> score = 1;
//                case 'D', 'G' -> score=2;
//                case 'B', 'C', 'M', 'P' -> score=3;
//                case 'F', 'H', 'V', 'W', 'Y' -> score=4;
//                case 'K' -> score=5;
//                case 'J', 'X' -> score=8;
//                case 'Q', 'Z' -> score=10;
//                default -> score=0;
//            }
//            if(maxScore<score){
//                maxScore=score;
//                maxScoreLetter=letter;
//            }
//        }
//        temp.put(maxScoreLetter, temp.get(maxScoreLetter)-1);
//        return maxScoreLetter;
//    }
//    private int countBlank(String[] letters){
//        int count=0;
//        for(String letter: letters){
//            if(letter.equals(" ")){
//                count++;
//            }
//        }
//        return count;
//    }
//    public boolean containBlank(String[] letters){
//        for(String letter: letters){
//            if(letter.equals(" ")){
//                return true;
//            }
//        }
//        return false;
//    }
//    public int calculateWordScore(String word){
//        int score = 0;
//        for(int i=0; i<word.length(); i++) {
//            /*if (word.charAt(i) == 'E' || word.charAt(i) == 'A' || word.charAt(i) == 'I' || word.charAt(i) == 'O' || word.charAt(i) == 'N' || word.charAt(i) == 'R' || word.charAt(i) == 'T' || word.charAt(i) == 'L' || word.charAt(i) == 'S' || word.charAt(i) == 'U') {
//                score += 1;
//            }*/
//            switch (word.charAt(i)) {
//                case 'E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U' -> score += 1;
//                case 'D', 'G' -> score+=2;
//                case 'B', 'C', 'M', 'P' -> score+=3;
//                case 'F', 'H', 'V', 'W', 'Y' -> score+=4;
//                case 'K' -> score+=5;
//                case 'J', 'X' -> score+=8;
//                case 'Q', 'Z' -> score+=10;
//                default -> score+=0;
//            }
//        }
//        if(word.length()==7){
//            return score+50;
//        }
//        return score;
//    }

    /**********************|||||||||||||************************/

    //TODO: not done
    public Action chooseAction(PotOfMoney pot){
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

    protected boolean shouldAllIn(PotOfMoney pot){
        return Math.abs(dice.nextInt()) % 150 < getCurrentBestHand().getRiskWorthiness() +
                getRiskTolerance();
    }


//    private void findWordsHelper(DictionaryTrie.Node root, String[] letters, StringBuilder sb, List<String> res) {
//        // 如果当前节点表示一个单词，并且该单词由给定字符集合组成，则将其加入到结果列表中
//        if (curr.isEndOfWord() && isWordInChars(curr.getWord(), chars)) {
//            res.add(curr.getWord());
//        }
//        // 递归遍历当前节点的子节点
//        for (TrieNode node : curr.getChildren().values()) {
//            char letter = node.getLetter();
//            if (chars.contains(letter)) {
//                sb.append(letter);
//                findWordsHelper(node, chars, sb, res);
//                sb.deleteCharAt(sb.length() - 1);
//            }
//        }
//    }
//
//    private boolean isWordInChars(String word, Set<Character> chars) {
//        for (char c : word.toCharArray()) {
//            if (!chars.contains(c)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
    /*
    private void findAllWordsHelper(List<List<String>> temp, String[] letters, List<List<String>> combinations, List<String> words, HashMap<String, Integer> duplicatedLetters){
        for (List<String> combination : combinations) {
            for (String letter : letters) {
                List<String> newCombination = new ArrayList<>(combination);
                newCombination.add(letter);
                if((!temp.contains(newCombination)) && checkDuplicated(duplicatedLetters, newCombination)){ //
                    temp.add(newCombination);
                    String word = newCombination.stream().collect(Collectors.joining());
                    words.add(word);
                }
            }
        }
    }
    private boolean checkDuplicated(HashMap<String, Integer> duplicatedLetters, List<String> newCombination){
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
    //TODO: findAllWords 太慢了, 需要重写
    //findAllWords will return all words that can be formed by current letters on players hand,
    public List<String> findAllWords1(String[] letters){
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
        List<String> words = new ArrayList<>();
        List<String> result = new ArrayList<>();
        // C(1, n)
        for (String letter : letters) {
            List<String> combination = new ArrayList<>();
            combination.add(letter);
            combinations.add(combination);
            String word = combination.stream().collect(Collectors.joining());
            if(DictionaryTrie.getDictionary().isValidWord(word)){
                words.add(word);
            }
        }
        // C(2, n) to C(n-1, n)
        List<List<String>> temp = new ArrayList<>();
        for(int i=2; i<letters.length; i++){
            findAllWordsHelper(temp, letters, combinations, words, duplicatedLetters);
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
                        words.add(word);
                    }
                }
            }
        }
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        for(String word: words){
            if(dict.isValidWord(word)){
                result.add(word);
            }
//            System.out.println("word = "+word);
        }
//        System.out.println("*****************************************");
//        for(String re: result){
//            System.out.println(re);
//        }
//        System.out.println("result.size = "+result.size());
        return result;
    }*/
}