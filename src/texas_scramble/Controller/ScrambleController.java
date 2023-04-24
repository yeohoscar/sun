package texas_scramble.Controller;

import poker.Card;
import texas.MainController;
import texas_hold_em.ComputerTexasPlayer;
import texas_hold_em.HumanTexasPlayer;
import texas_hold_em.RoundsOfTexas;
import texas_hold_em.TexasController;
import texas_scramble.Deck.*;
import texas_scramble.Player.ComputerScramblePlayer;
import texas_scramble.Player.HumanScramblePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ScrambleController extends MainController {
    protected DeckOfTiles deck;
    @Override
    public void setUp(String[] names, int bank) {
        texasPlayers = new ArrayList<>();
        numPlayers = names.length;


        for (int i = 0; i < numPlayers; i++)
            if (i == 0)
                texasPlayers.add(new HumanScramblePlayer(names[i].trim(), bank,i));
            else
                texasPlayers.add(new ComputerScramblePlayer(names[i].trim(), bank,i));

        deck=new DeckOfTiles();
    }
    @Override
    public void play()	{
        int dealerIndex=texasPlayers.size();
        while (texasPlayers.size() > 1) {
            deck.reset();//before each game starts, shuffle the deck
            if(dealerIndex>=texasPlayers.size()){dealerIndex=0;}
            List<Tile> communityTiles = new ArrayList<>();

            //start a game, there are four rounds within a game: Pre-flop, Turn, River and the one after River.
            RoundOfScramble round = new RoundOfScramble(deck, texasPlayers, communityTiles, dealerIndex);
            round.play();
            if(texasPlayers.size()==1){
                break;
            }
            try {
                System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");

                byte[] input = new byte[100];

                System.in.read(input);

                for (int i = 0; i < input.length; i++)
                    if ((char)input[i] == 'q' || (char)input[i] == 'Q')
                        return;
                //next player become a dealer
                dealerIndex ++;

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            updatePlayerIDs();
        }

        System.out.println("Congratulations!");
        System.out.println(texasPlayers.get(0).getName()+" is the Winner of the Game!");
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
