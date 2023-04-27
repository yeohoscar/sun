package texas.scramble.player;


import texas.hold_em.TexasHumanPlayer;
import texas.scramble.deck.Tile;
import texas.scramble.dictionary.FullDictionary;
import texas.scramble.hand.ScrambleHand;


import java.util.*;


public class HumanScramblePlayer extends TexasHumanPlayer {

    private int finalValue=0;
    private int wordLength=7;

    private Tile[] newHand;
    public HumanScramblePlayer(String name, int money, int id) {
        super(name, money, id);
    }

    public int submitWord(List<Tile> communityTiles) {
        combineTiles(communityTiles);
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

        return finalValue;
    }


    public boolean canFormString(Tile[] newHand, String inputString) {
        Map<String, Integer> charFreq = new HashMap<>();
        for (Tile tile : newHand) {
            charFreq.put(tile.name(), charFreq.getOrDefault(tile.name(), 0) + 1);
        }

    // Check if the string can be formed from the characters in the list
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            String str = String.valueOf(c);
            if(!charFreq.containsKey(str)&&charFreq.containsKey(" ")){
                if(charFreq.get(" ")!=0){
                    charFreq.put(" ", charFreq.get(" ") - 1);
                    continue;
                }else{
                    return false;
                }
            }
            else if (!charFreq.containsKey(str) || charFreq.get(str) == 0) {
                // The character c is not in the list or has already been used up
                return false;
            }
            // Decrement the frequency of the character c in the list
            charFreq.put(str, charFreq.get(str) - 1);
        }

    // All characters in the string can be formed from the list
        return true;
    }

    public void combineTiles(List<Tile> communityTiles){
        Tile[] allTiles = (Tile[]) Arrays.copyOf(hand.getHand(), 7);
        int index = 2;
        for (Tile tile : communityTiles) {
            allTiles[index++] = tile;
        }
        this.newHand=allTiles;
        System.out.println("CommunityTiles"+communityTiles);
        System.out.print("newHand: ");
        for(Tile tile : newHand){
            System.out.print(tile.name());
        }
        System.out.println();

    }

    public String askQuestion(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your word (maximum "+wordLength+" letters): ");
        String word = input.nextLine().trim();
        while (!canFormString(newHand,word )||!FullDictionary.getFullDictionary().isValidWord(word)) {
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
        return findHighestScoreWord(letters, FullDictionary.getFullDictionary());
    }

    public void removeTileFromNewHand(String letter) {
        Tile[] newArray = new Tile[newHand.length - 1];
        int j = 0;
        for (Tile tile : newHand) {
            if(tile.name().equals(" ")&&!Arrays.asList(newHand).contains(letter)){
                continue;
            }
            else if (tile.name().equals(letter)) {
                continue;
            }
            newArray[j++] = tile;
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