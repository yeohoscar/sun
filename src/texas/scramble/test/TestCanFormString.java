package texas.scramble.test;

import org.junit.jupiter.api.Test;
import texas.scramble.deck.Tile;
import texas.scramble.dictionary.FullDictionary;
import texas.scramble.player.ScrambleHumanPlayer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class TestCanFormString {
    ScrambleHumanPlayer player1 = new ScrambleHumanPlayer("Tom", 0, 0);
    Tile[] newHand = {new Tile("P", 3),
            new Tile("Y", 4),
            new Tile("C", 3),
            new Tile("A", 1),
            new Tile("I", 1),
            new Tile("O", 1),
            new Tile("T", 1)};
    String[] words = {"p-Oo", "tyaipyo", "a", "a", "a", "A", "cat", "a"};
    String[] words2 = {"p-Oo", "tyaipyo", "a", "a", "a", "A", "CAT", "a"};
    String[] words1 = {"CAT"};
    @Test
    public void testCanFormString(){
        for(String word: words){
            if (!player1.canFormString(newHand,word )||!FullDictionary.getInstance().isValidWord(word)){
               System.out.println("InValid word! Please enter a word again (maximum "+newHand.length+" letters) or g to give up: ");
           }else {
               System.out.println("valid word: "+word);
           }
        }
        System.out.println("***************************");
        for(String word: words2){
            if (!player1.canFormString(newHand,word )||!FullDictionary.getInstance().isValidWord(word)){
                System.out.println("InValid word! Please enter a word again (maximum "+newHand.length+" letters) or g to give up: ");
            }else {
                System.out.println("valid word: "+word);
            }
        }
    }
    @Test
    public void testSimulateUserInput(){
        String input = "cat";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        String word = result.trim().toUpperCase();
        if (!player1.canFormString(newHand, word)||!FullDictionary.getInstance().isValidWord(word)){
            System.out.println("InValid word! Please enter a word again (maximum "+newHand.length+" letters) or g to give up: ");
        }else {
            System.out.println("valid word: "+word);
        }
    }
}
