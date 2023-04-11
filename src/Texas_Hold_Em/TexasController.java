
package Texas_Hold_Em;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A GameOfPoker is a sequence of one or more RoundOfPoker's


import poker.DeckOfCards;

import java.util.Arrays;
import java.util.Scanner;

public class TexasController extends MainController {

	@Override
	public void setUp(String[] names, int bank) {
		super.setUp(names, bank);
		deck  = new DeckOfCards();
	}

	public static void main(String[] args) {
		String[] names = {"Human", "Tom", "Dick", "Harry", "Jim", "Dave", "Paul", "Bob", "John", "Bill"};

		System.out.println("\nWelcome to the Automated Texas Hold'Em Machine ...\n\n");

		System.out.print("\nWhat is your name?  ");

		byte[] input = new byte[100];

		try {
			int numBytesRead = System.in.read(input);
			String userInput = new String(input, 0, numBytesRead).trim();
			if (!userInput.isEmpty()) {
				names[0] = userInput;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.print("\nHow many players do you wish to play with?  ");

		Scanner scanner = new Scanner(System.in);
		String[] playerNames = names;

		try {
			int numPlayers = scanner.nextInt();

			if (numPlayers < 10 && numPlayers > 2) {
				playerNames = Arrays.copyOfRange(names, 0, numPlayers);
			}

		} catch (Exception e) {
			System.out.println("Invalid input.");
			scanner.nextLine(); // Clear the scanner buffer
		}

		int startingBank = 100;

		System.out.println("\nLet's play Texas Hold'Em ...\n\n");
		TexasController game = new TexasController();
		game.setUp(playerNames, startingBank);
		game.play();
	}
}