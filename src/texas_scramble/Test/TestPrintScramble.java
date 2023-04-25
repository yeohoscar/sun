package texas_scramble.Test;

import org.junit.jupiter.api.Test;
import poker.Card;
import poker.DeckOfCards;
import poker.Player;
import poker.PotOfMoney;
import texas.TexasPlayer;
import texas_hold_em.ComputerTexasPlayer;
import texas_hold_em.Hand;
import texas_hold_em.PrintGame;
import texas_hold_em.TexasController;
import texas_scramble.Deck.DeckOfTiles;
import texas_scramble.Deck.Tile;
import texas_scramble.PrintGame.PrintScramble;

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

        PrintScramble printPublic = new PrintScramble(texasPlayers, Tiles, pots, cards);
        printPublic.cardPrinter(false);
    }
}
