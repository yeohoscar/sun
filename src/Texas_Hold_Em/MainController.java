package Texas_Hold_Em;

import java.util.HashMap;

public abstract class MainController {

    protected Player[] players;

    protected Deck deck;

    protected int numPlayers;

    protected HashMap<String,Integer> Pots;

    public int getNumPlayers() {
        return numPlayers;

    }


    public Player getPlayer(int num) {
        if (num >= 0 && num <= numPlayers)
            return players[num];
        else
            return null;
    }


    public int getNumSolventPlayers() {
        // how many players still have money left?

        int count = 0;

        for (int i = 0; i < getNumPlayers(); i++)
            if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
                count++;

        return count;
    }

    public void play()	{
        int dealerIndex=numPlayers-1;
        int numSolventPlayers = players.length;
        while (getNumSolventPlayers() > 1) {
            deck.reset();//before each game starts, shuffle the deck
            numSolventPlayers=getNumSolventPlayers();

            //every player can act as dealer, dealer also needs to place bet, but dealer is the last one to place bet
            players[dealerIndex] = new DealerPlayer(players[dealerIndex].getName(), players[dealerIndex].getBank());
            players[dealerIndex].setDealer(true);

            //start a game, there are four rounds within a game: Pre-flop, Turn, River and the one after River.
            RoundsOfTexas round = new RoundsOfTexas(deck, players, dealerIndex, numSolventPlayers);
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
    }
}
