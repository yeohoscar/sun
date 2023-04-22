package texas_scramble.Test;

//import org.junit.Test;
import org.junit.jupiter.api.Test;
import poker.*;
import texas_hold_em.*;
import texas_scramble.Deck.DeckOfTiles;
import texas_scramble.Deck.DictionaryTrie;
import texas_scramble.Player.ComputerScramblePlayer;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class TestScrambleComputerPlayer {
    @Test
    public void testFindAllWordsWithBlank() {
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters7 = {"Z", "G", "H", " ", " "};
        String[] letters8 = {"Z", "G", "H", "Q", "J"};//these letters can not form any words
        String[] letters6 = {"A", "B", "C", "D", " ", " "};

        String[] temp = letters6;
        //if letters contain blank, we must substitute blank with other letters
        if (player1.containBlank(temp)) {
            player1.substituteBlank(temp);
        }
        List<String> maxScoreWord2 = dict.findAllWords(temp);
        System.out.println(maxScoreWord2);
    }

    @Test
    public void testFindAllWords() {
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters1 = {"e", "n", "o", "p", "g", "i", "a"};
        String[] letters2 = {"A", "S", "S"};

        //test letters3 and letters4 first, these two should have worked
        String[] letters3 = {"U", "N", "O", "S"};
        String[] letters6 = {"A", "B", "C", "D"};
        String[] letters4 = {"X", "Y", "E", "N", "O", "P", "G"};
        String[][] letters5 = {{"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}};
        String[] letters7 = {"Z", "G", "H", "P", "A"};

        List<String> maxScoreWord2 = dict.findAllWords(letters7);
        System.out.println(maxScoreWord2);

//        List<String> maxScoreWord3 = player1.findAllWords(letters4);
//        System.out.println(maxScoreWord3);

//        for(String[] letter: letters5){
//            List<String> maxScoreWord2 = player1.findAllWords(letter);
//        }
//        for(String[] letter: letters5){
//            List<String> maxScoreWord2 = player1.findAllWords(letter);
//            System.out.println(maxScoreWord2);
//        }
    }

    @Test
    public void testFindHighestScoreWord() {
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters2 = {"A", "S", "S"};
        String letters3 = "XYENOPG";
        String[] letters4 = {"XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG"};
        String letters7 = "ZGHQJ";//these letters can not form any words
        HashMap<String, Integer> maxScoreWord = player1.findHighestScoreWord(letters3, dict);
        System.out.println("word with highest score : " + maxScoreWord);
//        for(String letter: letters4){
//            HashMap<String, Integer> maxScoreWord2 = player1.findHighestScoreWord(letter);
//            System.out.println("max = "+maxScoreWord2);
//        }
    }

    @Test
    public void testFindAllCombination() {
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        //test when there is only one unknown letter
        String[] playerLetters = {"A", "B"};
        //ArrayList<String> result = player1.findAllCombination(playerLetters, 1);
//        for(String re: result){
//            System.out.println(re);
//        }


        //test when there are two unknown letters
        String[] playerLetters1 = {"A", "B"};
        //ArrayList<String> result1 = player1.findAllCombination(playerLetters1, 2);
        /*for(String re: result1){
            System.out.println(re);
        }*/

        //test when there are two unknown letters which are blanks
        String[] playerLetters3 = {"Z", "G", "H", " ", " "};
        String[] playerLetters2 = {"Z", "G", "H"};
        ArrayList<String> result2 = player1.findAllCombination(playerLetters3, 2);
        for (String re : result2) {
            if (re.length() == 7) {
                System.out.println("length = " + re.length());
                System.out.println(re);
            } else {
                exit(0);
            }
        }
        System.out.println("Size = " + result2.size());

    }

    @Test
    public void testFlopRound() {
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        int averageCommunityLettersScore = 0;
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        HashMap<String, Integer> highestWords = new HashMap<>();
        //test when there are two unknown letters which are blanks
        String[] flopLetters2 = {"Z", "G", "H", "A", "B"};
        String[] communityLetters2 = {"Z", "G", "H"};
        String[] turnLetters2 = {"Z", "G", "H", "B", "C", "A"};
        ArrayList<String> allCombination = player1.findAllCombination(turnLetters2, 1);
        for (String combination : allCombination) {
            highestWords.putAll(player1.findHighestScoreWord(combination, dict));
        }
        player1.removeWordsWithZeroValue(highestWords);
        int averageScore = 0;
        for(Map.Entry<String, Integer> entry: highestWords.entrySet()){
            averageScore += entry.getValue();
        }
        averageScore = averageScore/highestWords.size();

        averageCommunityLettersScore = dict.calculateAverageScoreOfAllWordsContainCommunityLetters(communityLetters2, player1);

        if(averageScore>averageCommunityLettersScore){
            System.out.println("averageScore = " + averageScore);
            System.out.println("averageCommunityLettersScore = "+averageCommunityLettersScore);
        }else {
            System.out.println("averageScore = " + averageScore);
            System.out.println("averageCommunityLettersScore = "+averageCommunityLettersScore);
        }
        /*ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        HashMap<String, Integer> highestWords = new HashMap<>();
        //test when there are two unknown letters which are blanks
        String[] playerLetters3 = {"Z", "G", "H"};
        String[] playerLetters2 = {"Z", "G", "H", " ", " "};
        ArrayList<String> result2 = player1.findAllCombination(playerLetters3, 4);
        for (String re : result2) {
            //System.out.println(player1.findHighestScoreWord(re));
            highestWords.putAll(player1.findHighestScoreWord(re));
        }
        int finalScore = 0;
        for (Map.Entry<String, Integer> entry : highestWords.entrySet()) {
            finalScore += entry.getValue();
        }
        System.out.println("final score = " + finalScore);
        System.out.println("Size = " + result2.size());*/
    }

    @Test
    public void testTurnRound() {
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        int averageCommunityLettersScore = 0;
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        HashMap<String, Integer> highestWords = new HashMap<>();
        //test when there are two unknown letters which are blanks
        String[] flopLetters2 = {"Z", "G", "H", "A", "B"};
        String[] communityLetters2 = {"Z", "G", "H"};
        String[] turnLetters2 = {"Z", "G", "H", " ", " ", "A"};
        ArrayList<String> allCombination = player1.findAllCombination(turnLetters2, 1);
        System.out.println("size of combination = "+allCombination.size());
        for (String combination : allCombination) {
            highestWords.putAll(player1.findHighestScoreWord(combination, dict));
        }
        player1.removeWordsWithZeroValue(highestWords);
        int averageScore = 0;
        for(Map.Entry<String, Integer> entry: highestWords.entrySet()){
            averageScore += entry.getValue();
        }
        averageScore = averageScore/highestWords.size();

        System.out.println("averageScore = " + averageScore);

//        averageCommunityLettersScore = dict.calculateAverageScoreOfAllWordsContainCommunityLetters(communityLetters2, player1);
//
//        if(averageScore>averageCommunityLettersScore){
//            System.out.println("averageScore = " + averageScore);
//            System.out.println("averageCommunityLettersScore = "+averageCommunityLettersScore);
//        }else {
//            System.out.println("averageScore = " + averageScore);
//            System.out.println("averageCommunityLettersScore = "+averageCommunityLettersScore);
//        }
    }

    @Test
    public void calculateMidValue() {
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        int result = player1.average();
        System.out.println("result = " + result);
    }

    @Test
    public void testCalculateAverageScoreOfAllWordsContainCommunityLetters() {
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] communityLetters1 = {"A", "B", "C"};
        String[] communityLetters2 = {"Z", "G", "H"};
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        int score = 0;
        score = dict.calculateAverageScoreOfAllWordsContainCommunityLetters(communityLetters2, player1);

        System.out.println("average score = "+score);
    }
    @Test
    public void testRemoveWordsWithZeroValue(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        HashMap<String, Integer> highestWords = new HashMap<>();
        highestWords.put("^", 0);
        highestWords.put("HELLO", 10);
        highestWords.put("ABC", 20);
        player1.removeWordsWithZeroValue(highestWords);
        System.out.println(highestWords);
    }
}
