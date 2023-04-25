package texas.scramble.Test;

import org.junit.jupiter.api.Test;
import poker.PotOfMoney;
import texas.TexasPlayer;
import texas.hold_em.ComputerTexasPlayer;
import texas.Hand;
import texas.hold_em.TexasController;
import texas.scramble.Deck.DeckOfTiles;
import texas.scramble.Deck.Tile;
import texas.scramble.PrintGame.PrintScramble;

import java.util.ArrayList;
import java.util.List;

public class TestPrintScramble {
    @Test
    public void testCardPrinter(){
        DeckOfTiles Tiles = new DeckOfTiles();
        Tiles.reset();
        String[] names = {"Human", "Tom", "Dick", "Harry", "Yan", "wang", "Sun"};
        TexasController controller = new TexasController();
        controller.setUp(names, 5);
        ArrayList<TexasPlayer> texasPlayers = controller.texasPlayers;
        texasPlayers.get(1).setDealer(true);
        for(TexasPlayer player : texasPlayers){
            player.dealTo(Tiles);
//            player.setOnTurn(true);
        }
//        texasPlayers.get(1).setOnTurn(true);
//        texasPlayers.get(2).setOnTurn(false);
//        texasPlayers.get(3).setOnTurn(true);
        Hand communityCards;
        ArrayList<PotOfMoney> pots = new ArrayList<>();
//        PotOfMoney pot = new PotOfMoney();
        communityCards=Tiles.dealHand(4);
        List<Tile> cards = List.of((Tile[]) communityCards.getHand());

        ComputerTexasPlayer player=  new ComputerTexasPlayer("ad", 0, 0);

        PrintScramble printPublic = new PrintScramble(texasPlayers, pots, cards);
        printPublic.cardPrinter(false);
    }
}
