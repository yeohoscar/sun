package blackjack;

public class GameOfBlackJack {


    private Player[] players;//list that contains all players

    private DeckOfCards deck;//an instance of variable type of DeckOfCards

    private int numPlayers;//the number of players

    /*--------------------Constructor--------------------------*/
    public GameOfBlackJack(String[] names, int bank)
    {
        numPlayers = names.length+1;//the reason that adds extra one is for dealer

        players = new Player[numPlayers];
        players[numPlayers-1] = new DealerPlayer("Dealer", -1);//create an instance of Dealer

        //initialise HumanPlayer and all ComputerPlayer
        for (int i = 0; i < numPlayers-1; i++)
            if (i == 0)
                players[i] = new HumanPlayer(names[i].trim(), bank);
            else
                players[i] = new ComputerPlayer(names[i].trim(), bank);


        deck  = new DeckOfCards();//add 52 cards into deck of cards
    }

    /*--------------------Game starts to play, game ends until all players have no money in bank---------------------*/
    public void play(){
        while (getNumSolventPlayers() > 1) {
            deck.reset();//before each round starts, shuffle the deck
            RoundOfBlackJack round = new RoundOfBlackJack(deck, players);//create a new round

            round.play();//play with this round

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
        System.out.println("GAME OVER!");
    }

    /*--------------------return the number of players that still have money--------------------------*/
    public int getNumSolventPlayers() {
        int count = 0;

        for (int i = 0; i < getNumPlayers(); i++)
            if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
                count++;

        return count;
    }

    /*--------------------Getter Methods--------------------------*/
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
