package texas_scramble;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DictionaryTrie {
    Node root;

    public DictionaryTrie() {
        root = new Node('^', false, new ArrayList<Node>());
    }

    private void createDictionary() {
        try (Stream<String> stream = Files.lines(Paths.get("C:\\Users\\User\\OneDrive\\Documents\\UCD\\UCD\\CompSci\\Stage 3\\Semester 2\\COMP30880 Software Engineering Project 3\\sun_emoji_comp30880_project (1)\\src\\texas_scramble\\wordList.txt"))) {
            stream.forEach(this::add);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DictionaryTrie d = new DictionaryTrie();
        d.createDictionary();
        char[] str = new char[20];
        d.display(d.root, str, 0);
    }

    public Node getRoot() {
        return root;
    }

    private void add(String word) {
        Node curr = root;
        char[] letters = word.toCharArray();

        for (int i = 0; i < letters.length; i++) {
            if (!curr.children.contains(letters[i])) {
                if (i == letters.length - 1) {
                    Node newNode = new Node(letters[i], true, new ArrayList<Node>());
                    curr.addChild(newNode);
                } else {
                    Node newNode = new Node(letters[i], false, new ArrayList<Node>());
                    curr.addChild(newNode);
                    curr = newNode;
                }
            } else {
                curr = curr.children.get(curr.children.indexOf(letters[i]));
            }
        }
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

    private class Node {
        char letter;
        boolean isEndOfWord;
        List<Node> children;

        public Node(char letter, boolean isEndOfWord, List<Node> children) {
            this.letter = letter;
            this.isEndOfWord = isEndOfWord;
            this.children = children;
        }

        public char getLetter() {
            return letter;
        }

        public boolean isEndOfWord() {
            return isEndOfWord;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void addChild(Node newNode) {
            children.add(newNode);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Character) {
                return letter == (char) obj;
            }
            return false;
        }
    }
}
