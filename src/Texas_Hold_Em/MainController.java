package Texas_Hold_Em;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MainController {

    protected ArrayList<Player> players;

    protected DeckOfTexasCards deck;

    protected int numPlayers;


    public Player getPlayer(int num) {
        if (num >= 0 && num <= numPlayers)
            return players.get(num);
        else
            return null;
    }


    public void setUp(String[] names, int bank) {
        players = new ArrayList<>();
        numPlayers = names.length;


        for (int i = 0; i < numPlayers; i++)
            if (i == 0)
                players.add(new HumanPlayer(names[i].trim(), bank));
            else
                players.add(new ComputerPlayer(names[i].trim(), bank));
    }


    public void play()	{
        int dealerIndex=numPlayers;
        while (players.size() > 1) {
            deck.reset();//before each game starts, shuffle the deck
            //next player become a dealer
            dealerIndex++;
            if(dealerIndex==numPlayers){dealerIndex=0;}

            //start a game, there are four rounds within a game: Pre-flop, Turn, River and the one after River.
            RoundsOfTexas round = new RoundsOfTexas(deck, players, dealerIndex);
            round.play();

            try {
                System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");

                byte[] input = new byte[100];

                System.in.read(input);

                for (int i = 0; i < input.length; i++)
                    if ((char)input[i] == 'q' || (char)input[i] == 'Q')
                        return;
                dealerIndex += 1;
            }
            catch (Exception e) {};

        }

        System.out.println("Congratulations!");
        System.out.println(players.get(0).getName()+" is the Winner of the Game!");
    }
}