package texas_scramble.Test;

//import org.junit.Test;
import org.junit.jupiter.api.Test;
import texas_scramble.Dictionary.DictionaryTrie;
import texas_scramble.Dictionary.FullDictionary;
import texas_scramble.Player.ComputerScramblePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.exit;

public class TestScrambleComputerPlayer {

    @Test
    public void testFindAllWords() {
        FullDictionary dict = FullDictionary.getFullDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters1 = {"e", "n", "o", "p", "g", "i", "a"};
        String[] letters2 = {"A", "S", "S"};

        //test letters3 and letters4 first, these two should have worked
        String[] letters3 = {"U", "N", "O", "S"};
        String[] letters6 = {"A", "B", "C", "D"};
        String[] letters4 = {"X", "Y", "E", "N", "O", "P", "G"};
        String[][] letters5 = {{"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}, {"X", "Y", "E", "N", "O", "P", "G"}};
        String[] letters7 = {"E", "N", "O", "P", "G", "X", "Y"};

        List<String> allWords = dict.findAllWords(letters7);
        for(String word: allWords){
            System.out.println(word);
        }

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
        FullDictionary dict = FullDictionary.getFullDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters2 = {"A", "S", "S"};
        String[] letters3 = {"X", "Y", "E", "N", "O", "P", "G"};
        String[] letters5 = {"A", "A", "E"};
        String[] letters4 = {"XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG"};
        String letters7 = "ZGHQJ";//these letters can not form any words
//        HashMap<String, Integer> maxScoreWord = player1.findHighestScoreWord(letters5, dict);
        String maxScoreWord = player1.findHighestScoreWord(letters5, dict);
        System.out.println("word with highest score : " + maxScoreWord);
//        for(String letter: letters4){
//            HashMap<String, Integer> maxScoreWord2 = player1.findHighestScoreWord(letter);
//            System.out.println("max = "+maxScoreWord2);
//        }
    }

//    @Test
//    public void testFindAllCombination() {
//        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
//        //test when there is only one unknown letter
//        String[] playerLetters = {"A", "B"};
//        //ArrayList<String> result = player1.findAllCombination(playerLetters, 1);
////        for(String re: result){
////            System.out.println(re);
////        }
//
//
//        //test when there are two unknown letters
//        String[] playerLetters1 = {"A", "B"};
//        //ArrayList<String> result1 = player1.findAllCombination(playerLetters1, 2);
//        /*for(String re: result1){
//            System.out.println(re);
//        }*/
//
//        //test when there are two unknown letters which are blanks
//        String[] playerLetters3 = {"Z", "G", "H", " ", " "};
//        String[] playerLetters2 = {"Z", "G", "H"};
//        ArrayList<String> result2 = player1.findAllCombination(playerLetters3, 2);
//        for (String re : result2) {
//            if (re.length() == 7) {
//                System.out.println("length = " + re.length());
//                System.out.println(re);
//            } else {
//                exit(0);
//            }
//        }
//        System.out.println("Size = " + result2.size());
//
//    }

    @Test
    public void testFlopRound() {
        FullDictionary dict = FullDictionary.getFullDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        HashMap<String, Integer> wordsScore = new HashMap<>();
        //test when there are two unknown letters which are blanks
        String[] flopLetters3 = {"Z", "A", "J", "H", "H"};
        String[] flopCommunityLetters3 = {"J", "A", "Z"};
        String[] turnLetters2 = {"E", "A", "I", " ", " ", "E"};
        String[] turnCommunityLetters2 = {"I", " ", "A", "E"};
        String[] turnLetters3 = {"Z", "G", "H", "B", "C", "A"};
        //ArrayList<String> allCombination = player1.findAllCombination(flopLetters2, 2);
        List<String> allWords = dict.findAllWords(turnLetters2);
        float averageScore = 0;
        for (String word : allWords) {
            System.out.println("word = "+word);
            averageScore += player1.calculateWordScore(word);
        }
        System.out.println("allWords size = "+allWords.size());
        System.out.println("total score = "+averageScore);
        averageScore = averageScore/allWords.size();




        float averageCommunityLettersScore=0;
        int totalNumber = 0;
        List<String> allCommunityWords = dict.findAllWords(turnCommunityLetters2);
        for(String word: allCommunityWords){
            averageCommunityLettersScore+=player1.calculateWordScore(word);
            totalNumber++;
        }
        System.out.println("\n");
        System.out.println("allWords size = "+allCommunityWords.size());
        System.out.println("total score = "+averageCommunityLettersScore);
        averageCommunityLettersScore =  averageCommunityLettersScore/totalNumber;
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
        FullDictionary dict = FullDictionary.getFullDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        HashMap<String, Integer> wordsScore = new HashMap<>();
        //test when there are two unknown letters which are blanks
        String[] flopLetters2 = {"E", "A", "J", "Q", "Z"};
        String[] flopLetters3 = {"Q", "Z", "J", "X", "K"};
        String[] communityLetters2 = {"Q", "A", "J"};
        String[] communityLetters3 = {"Q", "Z", "J"};
        String[] turnLetters2 = {"Z", "G", "H", " ", " ", "A"};
        List<String> allWords = dict.findAllWords(flopLetters2);
        float averageScore = 0;
        for (String word : allWords) {
            System.out.println("word = "+word);
            averageScore += player1.calculateWordScore(word);
        }
        System.out.println("allWords size = "+allWords.size());
        System.out.println("total score = "+averageScore);
        averageScore =  averageScore/allWords.size();



        float averageCommunityLettersScore=0;
        int totalNumber = 0;
        for(String word: dict.findAllWords(communityLetters2)){
            averageCommunityLettersScore+=player1.calculateWordScore(word);
            totalNumber++;
        }
        averageCommunityLettersScore = averageCommunityLettersScore/totalNumber;
        if(averageScore>averageCommunityLettersScore){
            System.out.println("averageScore = " + averageScore);
            System.out.println("averageCommunityLettersScore = "+averageCommunityLettersScore);
        }else {
            System.out.println("averageScore = " + averageScore);
            System.out.println("averageCommunityLettersScore = "+averageCommunityLettersScore);
        }
    }

    @Test
    public void calculateMidValue() {
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        int result = player1.average();
        System.out.println("result = " + result);
    }
    @Test
    public void testFindAllWordsFormedByLetters(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        DictionaryTrie dict = FullDictionary.getFullDictionary();
        String[] letters1 = {"A", "A", "E", "E"};
        String[] letters2 = {"E", "N", "O", "P", "G", "X", "Y"};
        List<String> result = dict.findAllWordsFormedByLetters(letters2);
        for(String word: result){
            System.out.println(word);
        }

    }
    @Test
    public void testCalculateAverageScoreOfAllWordsContainCommunityLetters() {
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] communityLetters1 = {"A", "B", "C"};
        String[] communityLetters2 = {"Z", "G", "H"};
        FullDictionary dict = FullDictionary.getFullDictionary();
        int averageCommunityLettersScore=0;
        int totalNumber = 0;
        for(String word: dict.findAllWords(communityLetters2)){
            averageCommunityLettersScore+=player1.calculateWordScore(word);
            totalNumber++;
        }
        averageCommunityLettersScore = averageCommunityLettersScore/totalNumber;
        System.out.println("average score = "+averageCommunityLettersScore);
    }
}
    /*@Test
    public void testFindAllWordsWithBlank() {
        FullDictionary dict = FullDictionary.getFullDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters7 = {"Z", "G", "H", " ", " "};
        String[] letters8 = {"Z", "G", "H", "Q", "J"};//these letters can not form any words
        String[] letters6 = {"A", "B", "C", "D", " ", " "};

        String[] temp = letters6;
        //if letters contain blank, we must substitute blank with other letters
        if (player1.containBlank(temp)) {
            player1.removeBlank(temp);
        }
        List<String> maxScoreWord2 = dict.findAllWords(temp);
        System.out.println(maxScoreWord2);
    }*/
    /*@Test
    public void testRemoveWordsWithZeroValue(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        HashMap<String, Integer> highestWords = new HashMap<>();
        highestWords.put("^", 0);
        highestWords.put("HELLO", 10);
        highestWords.put("ABC", 20);
        player1.removeWordsWithZeroValue(highestWords);
        System.out.println(highestWords);
    }*/

