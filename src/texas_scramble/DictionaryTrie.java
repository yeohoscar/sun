package texas_scramble;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DictionaryTrie {
    Node root;

    public DictionaryTrie() {
        root = new Node('^', false, new ArrayList<>());
        createDictionary();
        char[] str = new char[20];
        display(root, str, 0);
    }

    private void createDictionary() {
        try (Stream<String> stream = Files.lines(Paths.get("Collins Scrabble Words (2019).txt"))) {
            stream
               .filter(word -> word.length() <= 7)
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
