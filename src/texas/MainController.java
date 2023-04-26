package texas;

import poker.Card;
import poker.DeckOfCards;
import texas.hold_em.ComputerTexasPlayer;
import texas.hold_em.HumanTexasPlayer;
import texas.hold_em.RoundsOfTexas;

import java.util.ArrayList;
import java.util.List;

public abstract class MainController {
    public ArrayList<TexasPlayer> texasPlayers;

    protected DeckOfCards deck;

    protected int numPlayers;

    public void setUp(String[] names, int bank) {
        texasPlayers = new ArrayList<>();
        numPlayers = names.length;


        for (int i = 0; i < numPlayers; i++)
            if (i == 0)
                texasPlayers.add(new HumanTexasPlayer(names[i].trim(), bank,i));
            else
                texasPlayers.add(new ComputerTexasPlayer(names[i].trim(), bank,i));
    }

    public void play()	{
        int dealerIndex=texasPlayers.size();
        while (texasPlayers.size() > 1) {
            deck.reset();//before each game starts, shuffle the deck
            if(dealerIndex>=texasPlayers.size()){dealerIndex=0;}

            //start a game, there are four rounds within a game: Pre-flop, Turn, River and the one after River.
            RoundsOfTexas round = new RoundsOfTexas(deck, texasPlayers, dealerIndex);
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

    protected void updatePlayerIDs() {
        for (int i = 0; i < texasPlayers.size(); i++) {
            texasPlayers.get(i).setId(i);
        }
    }
}
