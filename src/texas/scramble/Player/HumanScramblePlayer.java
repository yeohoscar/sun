package texas.scramble.Player;

import texas.hold_em.HumanTexasPlayer;
import texas.scramble.Deck.Tile;
import texas.scramble.Dictionary.DictionaryTrie;
import texas.scramble.Hand.ScrambleHand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class HumanScramblePlayer extends HumanTexasPlayer {

    private int finalValue=0;
    private int wordLength=7;
    private DictionaryTrie dict;

    private Tile[] newHand;
    public HumanScramblePlayer(String name, int money, int id) {
        super(name, money, id);
        this.dict = new DictionaryTrie("resources/hard.txt");

    }

    public void submitWord() {
        this.newHand = (Tile[]) hand.getHand();
        ArrayList<String> words = new ArrayList<>();
        words.add(askQuestion());
        while(wordLength>0){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Do you want to enter another word with rest of the Tiles? Y/N");
            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("y")||answer.equals("Y")) {
                words.add(askQuestion());
            }else {
                break;
            }
        }

        for(String word: words){
            finalValue+=calculateHandScore(word);
        }
        if (newHand.length==0){
            finalValue+=50;
        }

        System.out.println("The best word you can make is: "+bestWord((ScrambleHand) hand)+"!");
    }


    public boolean canFormString(Tile[] newHand, String inputString) {
        char[] stringArray = inputString.toCharArray();
        Arrays.sort(newHand);
        Arrays.sort(stringArray);
        int i = 0;
        int j = 0;
        while (i < newHand.length && j < stringArray.length) {
            if (newHand[i].name().charAt(0) == stringArray[j]) {
                j++;
            }
            i++;
        }
        return j == stringArray.length;
    }

    public String askQuestion(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your word (maximum "+wordLength+" letters): ");
        String word = input.nextLine().trim();
        while (!canFormString(newHand,word )||!dict.isValidWord(word)) {
            System.out.println("InValid word! Please enter a word again (maximum "+wordLength+" letters): ");
            word = input.nextLine().trim();
        }
        wordLength-=word.length();
        removeTiles(word);
        return word;
    }
    public void removeTiles(String word){
        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            removeTileFromNewHand(letter);
        }
    }
    public String bestWord(ScrambleHand hand) {
        String[] letters = new String[hand.getHand().length];
        for(int i=0;i<letters.length;i++){
            letters[i]=hand.getHand()[i].name();
        }
        return findHighestScoreWord(letters,dict);
    }

    public void removeTileFromNewHand(String letter) {
        Tile[] newArray = new Tile[newHand.length - 1];
        int j=0;
        for (int i = 0; i < newHand.length; i++) {
            if (newHand[i].name() != letter) {
                newArray[j] = newHand[i];
                j++;
            }
        }
        newHand = newArray;
    }

    private int calculateHandScore(String hand) {
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
                default -> score += 0;
            }
        }
        return score;
    }
}
