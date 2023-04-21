package texas_scramble.Controller;

import texas.MainController;
import texas_hold_em.TexasController;
import texas_scramble.Deck.*;

import java.util.Arrays;
import java.util.Scanner;

public class ScrambleController extends MainController {
    protected DeckOfTiles deck;
    @Override
    public void setUp(String[] names, int bank) {
        super.setUp(names, bank);
        updatePlayerIDs();
        deck = new DeckOfTiles();
    }
    //distribute new ids to remain players
    private void updatePlayerIDs() {
        for (int i = 0; i < texasPlayers.size(); i++) {
            texasPlayers.get(i).setId(i);
        }
    }

    public static void main(String[] args) {
        String[] names = {"Human", "Tom", "Dick", "Harry", "Jim", "Dave", "Paul", "Bob", "John", "Bill"};

        System.out.println("\nWelcome to the Automated Texas Scramble Machine ...\n\n");

        System.out.print("\nWhat is your name?  ");

        byte[] input = new byte[100];

        try {
            int numBytesRead = System.in.read(input);
            String userInput = new String(input, 0, numBytesRead).trim();
            if (!userInput.isEmpty()) {
                names[0] = userInput;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        String[] playerNames = names;
        int numPlayers = 0;
        // ask how many players in the game
        while (!(numPlayers <= 10 && numPlayers >= 2)) {
            System.out.print("\nHow many players do you wish to play with? (Between 2 and 10 inclusive)  ");
            try {
                numPlayers = scanner.nextInt();

                if (numPlayers <= 10 && numPlayers >= 2) {
                    playerNames = Arrays.copyOfRange(names, 0, numPlayers);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("\nInvalid input.");
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
        // every player start with 100 chips
        int startingBank = 100;

        System.out.println("\nLet's play Texas Scramble ...\n\n");
        ScrambleController game = new ScrambleController();
        game.setUp(playerNames, startingBank);
        game.play();
    }
}
