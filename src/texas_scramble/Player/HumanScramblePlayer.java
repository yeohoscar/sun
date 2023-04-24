package texas_scramble.Player;

import texas_hold_em.HumanTexasPlayer;

import java.util.Scanner;

public class HumanScramblePlayer extends HumanTexasPlayer {
    public HumanScramblePlayer(String name, int money, int id) {
        super(name, money, id);
    }

    public String submitWord() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your word (maximum 7 letters): ");
        String word = input.nextLine().trim();

        while (word.length() > 7 ) {
            System.out.println("Word too long! Please enter a word (maximum 7 letters): ");
            word = input.nextLine().trim();
        }

        return word;
    }
}
