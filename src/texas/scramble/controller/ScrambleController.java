package texas.scramble.controller;

import texas.TexasController;
import texas.scramble.deck.*;
import texas.scramble.player.ScrambleComputerPlayer;
import texas.scramble.player.ScrambleHumanPlayer;

import java.util.*;

public class ScrambleController extends TexasController {
    protected DeckOfTiles deck;
    //set up the game with ScramblePlayer and Tiles for deck
    @Override
    public void setUp(String[] names, int bank, int numHumanPlayers) {
        texasPlayers = new ArrayList<>();
        numPlayers = names.length;

        for (int i = 0; i < numPlayers; i++) {
            if (i < numHumanPlayers) {
                texasPlayers.add(new ScrambleHumanPlayer(names[i].trim(), bank, i));
            } else {
                texasPlayers.add(getPresetComputerPlayer(names[i].trim(), bank, i));
            }
        }

        deck = new DeckOfTiles();
    }

    // Get computer players
    private ScrambleComputerPlayer getPresetComputerPlayer(String name, int bank, int id) {
        ScrambleComputerPlayer player;
        switch (name) {
            case "Tom" -> player = new ScrambleComputerPlayer(name, bank, id, "resources/easy.txt", 50);
            case "Dick" -> player = new ScrambleComputerPlayer(name, bank, id, "resources/medium.txt", 120);
            case "Harry" -> player = new ScrambleComputerPlayer(name, bank, id, "resources/harder.txt", 85);
            case "Jim" -> player = new ScrambleComputerPlayer(name, bank, id, "resources/hard.txt", 63);
            case "Dave" -> player = new ScrambleComputerPlayer(name, bank, id, "resources/medium.txt", 104);
            default -> player = new ScrambleComputerPlayer(name, bank, id, "resources/medium.txt");
        }
        return player;
    }

    //distribute new ids to remain players
    public void updatePlayerIDs() {
        for (int i = 0; i < texasPlayers.size(); i++) {
            texasPlayers.get(i).setId(i);
        }
    }

    @Override
    public void play()	{
        int dealerIndex=texasPlayers.size();
        while (texasPlayers.size() > 1) {
            deck.reset();//before each game starts, shuffle the deck
            if(dealerIndex>=texasPlayers.size()){dealerIndex=0;}


            //start a game, there are four rounds within a game: Pre-flop, Turn, River and the one after River.
            RoundOfScramble round = new RoundOfScramble(deck, texasPlayers, dealerIndex);
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

    public static void startGame() {
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
        while (!(numPlayers <= 9 && numPlayers >= 1)) {
            System.out.print("\nHow many players do you wish to play with? (Between 1 and 9 inclusive)  ");
            try {
                numPlayers = scanner.nextInt();

                if (numPlayers <= 9 && numPlayers >= 1) {
                    Random rand = new Random();
                    int j = 1;
                    for (int i = 0; i < numPlayers; i++) {
                        int idx = Math.abs(rand.nextInt()) % (numPlayers - i) + j;
                        String tmp = names[j];
                        names[j] = names[idx];
                        names[idx] = tmp;
                        j++;
                    }
                    playerNames = Arrays.copyOfRange(names, 0, numPlayers + 1);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("\nInvalid input.");
            }
            scanner.nextLine(); // Clear the scanner buffer
        }
        // every player start with 100 chips
        int startingBank = 100;

        System.out.println("\nLet's play Texas Scramble ...\n\n");
        ScrambleController game = new ScrambleController();
        game.setUp(playerNames, startingBank, 2);
        game.play();
    }
}
