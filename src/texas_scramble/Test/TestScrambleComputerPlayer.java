package texas_scramble.Test;

//import org.junit.Test;
import org.junit.jupiter.api.Test;
import poker.*;
import texas_hold_em.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class TestScrambleComputerPlayer {
    @Test
    public void testFindAllWordsWithBlank(){
        DictionaryTrie dict = DictionaryTrie.getDictionary();
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters7 = {"Z", "G", "H", " ", " "};
        String[] letters8 = {"Z", "G", "H", "Q", "J"};//these letters can not form any words
        String[] letters6 = {"A", "B", "C", "D", " ", " "};

        String[] temp = letters6;
        //if letters contain blank, we must substitute blank with other letters
        if(player1.containBlank(temp)){
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
        String[][] letters5 = {{"X","Y", "E", "N", "O", "P", "G"}, {"X","Y", "E", "N", "O", "P", "G"}, {"X","Y", "E", "N", "O", "P", "G"}, {"X","Y", "E", "N", "O", "P", "G"}, {"X","Y", "E", "N", "O", "P", "G"}, {"X","Y", "E", "N", "O", "P", "G"}, {"X","Y", "E", "N", "O", "P", "G"}};
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
    public void testFindHighestScoreWord(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters2 = {"A", "S", "S"};
        String letters3 = "XYENOPG";
        String[] letters4 = {"XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG", "XYENOPG"};
        String letters7 = "ZGHQJ";//these letters can not form any words
        HashMap<String, Integer> maxScoreWord = player1.findHighestScoreWord(letters3);
        System.out.println("word with highest score : "+maxScoreWord);
//        for(String letter: letters4){
//            HashMap<String, Integer> maxScoreWord2 = player1.findHighestScoreWord(letter);
//            System.out.println("max = "+maxScoreWord2);
//        }
    }
    @Test
    public void testFindAllCombination(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        //test when there is only one unknown letter
        String[] playerLetters = {"A", "B"};
        ArrayList<String> result = player1.findAllCombination(playerLetters, 1);
        /*for(String re: result){
            System.out.println(re);
        }*/

        //test when there are two unknown letters
        String[] playerLetters1 = {"A", "B"};
        ArrayList<String> result1 = player1.findAllCombination(playerLetters1, 2);
        /*for(String re: result1){
            System.out.println(re);
        }*/

        //test when there are two unknown letters which are blanks
        String[] playerLetters2 = {"Z", "G", "H", " ", " "};
        ArrayList<String> result2 = player1.findAllCombination(playerLetters2, 2);
        for(String re: result2){
            if(re.length()==7){
                System.out.println("length = "+re.length());
                System.out.println(re);
            }else {
                exit(0);
            }
        }
        System.out.println("Size = "+result2.size());
    }
    @Test
    public void testFlopRound(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        //test when there are two unknown letters which are blanks
        String[] playerLetters2 = {"Z", "G", "H", " ", " "};
        ArrayList<String> result2 = player1.findAllCombination(playerLetters2, 2);
//        System.out.println(player1.findHighestScoreWord(result2.get(0)));
//        for(String re: result2){
//            System.out.println(re);
//        }
        for(String re: result2){
            System.out.println(player1.findHighestScoreWord(re));
        }
        System.out.println("Size = "+result2.size());
    }
    @Test
    public void testTurnRound(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        //test when there are two unknown letters which are blanks
        String[] playerLetters2 = {"Z", "G", "H", " ", " ", "A"};
        ArrayList<String> result2 = player1.findAllCombination(playerLetters2, 1);
//        System.out.println(player1.findHighestScoreWord(result2.get(0)));
//        for(String re: result2){
//            System.out.println(re);
//        }
        for(String re: result2){
            System.out.println(player1.findHighestScoreWord(re));
        }
        System.out.println("Size = "+result2.size());
    }
}
