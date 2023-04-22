package texas_scramble.Deck;

import poker.Player;
import texas.TexasPlayer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DictionaryTrie {
    private static DictionaryTrie cache = null;

    Node root;

    private DictionaryTrie() {
        root = new Node('^', false, new ArrayList<>());
        createDictionary();
    }
    /************* find all words contain community letters **************/
    public int calculateAverageScoreOfAllWordsContainCommunityLetters(String[] letters, TexasPlayer player){
        Node current;
        HashMap<String, Integer> score = new HashMap<>();
        score.put("|", 0);
        List<List<String>> allWords = new ArrayList<>();
        for(String letter: letters){
            current = root;
//            Node n = current.children.stream()
//                    .filter(node -> letter.charAt(0) == node.getLetter())
//                    .findFirst()
//                    .orElse(null);
//            if(n==null){
//                return new ArrayList<>();
//            }
            //current = n;
            List<String> words = allWordsContainCommunityLettersDfsHelper(current, letter, letters, new ArrayList<>(), score, player);
            allWords.add(words);
        }
        ArrayList<String> result = new ArrayList<>();
        for(List<String> words: allWords){
            result.addAll(words);
        }
        return score.get("|")/result.size();
    }
    private boolean wordContainsCommunityLetters(String prefix, String[] communityLetters){
//        String[] strArray = prefix.split("");
//        boolean contained = true;
//        HashMap<String, Integer> prefixHash = new HashMap<>();
//        for(String letter: strArray){
//            if(prefixHash.containsKey(letter)){
//                prefixHash.put(letter, prefixHash.get(letter)+1);
//            }else {
//                prefixHash.put(letter, 1);
//            }
//        }
        //compare if both key and value of prefixHash are subset of lettersContained
        int countNumberOfLettersContained = 0;
        for(String letter: communityLetters){
            if(prefix.contains(letter)){
                countNumberOfLettersContained++;
            }
        }
        if(communityLetters.length==3){
            if(countNumberOfLettersContained==1 && prefix.length()<=5){
                return true;
            }else if(countNumberOfLettersContained==2 && prefix.length()<=6){
                return true;
            }else return countNumberOfLettersContained == 3 && prefix.length() <= 7;
        }else if(communityLetters.length==4){
            if(countNumberOfLettersContained==1 && prefix.length()<=4){
                return true;
            }else if(countNumberOfLettersContained==2 && prefix.length()<=5){
                return true;
            }else return countNumberOfLettersContained == 3 && prefix.length() <= 6;
        }
        return false;
    }
    private List<String> allWordsContainCommunityLettersDfsHelper(Node node, String prefix, String[] lettersContained, List<String> results, HashMap<String,Integer> score, TexasPlayer player) {
        if (node.isEndOfWord() && wordContainsCommunityLetters(prefix, lettersContained)) {
            results.add(prefix);
            score.put("|", score.get("|")+player.calculateWordScore(prefix));
            //System.out.println("results = "+results);
        }
        for (int i = 0; i < node.children.size(); i++) {
            Node child = node.children.get(i);
            if (child != null) {
                //System.out.println("prefix + child.letter = "+(prefix+child.letter));
                allWordsContainCommunityLettersDfsHelper(child, prefix + child.letter, lettersContained, results, score, player);
            }
        }
        return results;
    }

    /************* find all words formed by letters on player's hand **************/
    public List<String> findAllWords(String[] letters){
        Node current;
        HashMap<String, Integer> lettersContained = new HashMap<>();
        for(String letter: letters){
            if(lettersContained.containsKey(letter)){
                lettersContained.put(letter, lettersContained.get(letter)+1);
            }else {
                lettersContained.put(letter, 1);
            }
        }
        List<List<String>> allWords = new ArrayList<>();
        for(String letter: letters){
            current = root;
            Node n = current.children.stream()
                    .filter(node -> letter.charAt(0) == node.getLetter())
                    .findFirst()
                    .orElse(null);
            if(n==null){
                return new ArrayList<>();
            }
            current = n;
            allWords.add(dfs(current, letter, lettersContained, new ArrayList<>()));
        }

        List<String> result = new ArrayList<>();
        for(List<String> words: allWords){
            result.addAll(words);
        }
        return result;
    }
    private boolean wordIsFormedByLettersContained(String prefix, HashMap<String, Integer> lettersContained){
        //System.out.println("PPrefix = "+prefix);
        String[] strArray = prefix.split("");
        boolean contained = true;
        HashMap<String, Integer> prefixHash = new HashMap<>();
        for(String letter: strArray){
            if(prefixHash.containsKey(letter)){
                prefixHash.put(letter, prefixHash.get(letter)+1);
            }else {
                prefixHash.put(letter, 1);
            }
        }
        //compare if both key and value of prefixHash are subset of lettersContained
        for(Map.Entry<String, Integer> entry: prefixHash.entrySet()){
            if(!(lettersContained.containsKey(entry.getKey()) && (entry.getValue()<=lettersContained.get(entry.getKey())))){
                contained=false;
            }
        }
        return contained;
    }
    private List<String> dfs(Node node, String prefix, HashMap<String, Integer> lettersContained, List<String> results) {
        if (node.isEndOfWord() && wordIsFormedByLettersContained(prefix, lettersContained)) {
            results.add(prefix);
            //System.out.println("results = "+results);
        }
        for (int i = 0; i < node.children.size(); i++) {
            Node child = node.children.get(i);
            if (child != null) {
                //System.out.println("prefix + child.letter = "+(prefix+child.letter));
                dfs(child, prefix + child.letter, lettersContained, results);
            }
        }
        return results;
    }
    public static synchronized DictionaryTrie getDictionary() {
        if (cache == null) {
            cache = new DictionaryTrie();
        }
        return cache;
    }

    private void createDictionary() {
        try (Stream<String> stream = Files.lines(Paths.get("Collins Scrabble Words (2019).txt"))) {
            stream.filter(word -> word.length() <= 7)
                    .forEach(this::add);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Node getRoot() {
        return root;
    }

    private void add(String word) {
        Node curr = root;

        for (char letter : word.toCharArray()) {
            Node n = curr.children.stream()
                    .filter(node -> letter == node.getLetter())
                    .findFirst()
                    .orElse(null);

            if (n == null) {
                n = new Node(letter, false, new ArrayList<>());
                curr.addChild(n);
            }

            curr = n;
        }
        curr.setEndOfWord(true);
    }

    public boolean isValidWord(String word) {
        Node curr = root;

        for (char letter : word.toCharArray()) {
            curr = curr.children.stream()
                    .filter(node -> letter == node.getLetter())
                    .findAny()
                    .orElse(null);

            if (curr == null) return false;
        }
        return curr.isEndOfWord();
    }

    void display(Node root, char[] str, int level) {
        // If node is leaf node, it indicates end
        // of string, so a null character is added
        // and string is displayed
        if (root.isEndOfWord()) {
            for (int k = level; k < str.length; k++)
                str[k] = 0;
            System.out.println(str);
        }

        int i;
        for (i = 0; i < root.children.size(); i++) {
            // if NON NULL child is found
            // add parent key to str and
            // call the display function recursively
            // for child node
            if (root.children.get(i) != null) {
                str[level] = root.children.get(i).letter;
                display(root.children.get(i), str, level + 1);
            }
        }
    }

    private static class Node {
        char letter;
        boolean endOfWord;
        List<Node> children;

        public Node(char letter, boolean isEndOfWord, List<Node> children) {
            this.letter = letter;
            this.endOfWord = isEndOfWord;
            this.children = children;
        }

        public char getLetter() {
            return letter;
        }

        public boolean isEndOfWord() {
            return endOfWord;
        }

        public void setEndOfWord(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }

        public void addChild(Node newNode) {
            children.add(newNode);
        }
    }
}
