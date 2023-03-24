public class GameOfBlackJack {
    private Player[] players;

    private DeckOfCards deck;

    private int numPlayers;
    public GameOfBlackJack(String[] names, int bank)
    {
        numPlayers = names.length+1;

        players = new Player[numPlayers];
        players[numPlayers-1] = new DealerPlayer("Dealer", bank);
        for (int i = 0; i < numPlayers-1; i++)
            if (i == 0)
                players[i] = new HumanPlayer(names[i].trim(), bank);
            else
                players[i] = new ComputerPlayer(names[i].trim(), bank);


        deck  = new DeckOfCards();
    }

    public void play(){
        while (getNumSolventPlayers() > 1) {
            deck.reset();
            RoundOfBlackJack round = new RoundOfBlackJack(deck, players);

            round.play();

            try {
                System.out.print("\n\nPlay another round? Press 'c' to continue and q' to terminate this game ... ");

                byte[] input = new byte[100];

                System.in.read(input);

                for (int i = 0; i < input.length; i++)
                    if ((char)input[i] == 'q' || (char)input[i] == 'Q')
                        return;
            }
            catch (Exception e) {};
        }
    }
    public int getNumSolventPlayers() {
        // how many players still have money left?

        int count = 0;

        for (int i = 0; i < getNumPlayers(); i++)
            if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
                count++;

        return count;
    }
    public int getNumPlayers() {
        return numPlayers;
    }
    public Player getPlayer(int num) {
        if (num >= 0 && num <= numPlayers)
            return players[num];
        else
            return null;
    }
}