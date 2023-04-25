package texas_hold_em;

//import org.junit.Test;
import org.junit.jupiter.api.Test;
import poker.*;
import texas.TexasPlayer;

import java.util.ArrayList;
import java.util.List;

public class TestPrintGame {
    @Test
    public void testPrintPublicCard() {
        DeckOfCards deck = new DeckOfCards();
        deck.reset();
        Hand communityCards;
        ArrayList<TexasPlayer> texasPlayers = new ArrayList<>();
        PotOfMoney pot = new PotOfMoney();
        communityCards=deck.dealHand(3);
        List<Card> cards = List.of((Card[]) communityCards.getHand());
//        Hand community = new PokerHand();
////        DeckOfCards deck;
//        ArrayList<Card> communityCards = new ArrayList<>();
//        communityCards.add(new NumberCard("Ace", "hearts", 1, 14));
//        communityCards.add(new NumberCard("Deuce", "diamonds", 2));
//        communityCards.add(new NumberCard("Three", "clubs", 3));
//        PrintGame printPublic = new PrintGame(texasPlayers, deck, pot, cards);
//        printPublic.printPublicCard(cards);

    }
    @Test
    public void testCardPrinter() {
        DeckOfCards deck = new DeckOfCards();
        deck.reset();
        String[] names = {"Human", "Tom", "Dick", "Harry", "Yan", "wang", "Sun"};
        TexasController controller = new TexasController();
        controller.setUp(names, 5);
        ArrayList<TexasPlayer> texasPlayers = controller.texasPlayers;
        texasPlayers.get(1).setDealer(true);
        for(Player player : texasPlayers){
            player.dealTo(deck);
        }
//        texasPlayers.get(1).fold();
//        texasPlayers.get(2).fold();
//        for(Player player : texasPlayers){
//            System.out.println("Player name = "+player.getName());
//            for(Card card: player.getHand().getHand()){
//                System.out.println("Card name = "+card.getName());
//            }
//        }
        Hand communityCards;
        ArrayList<PotOfMoney> pots = new ArrayList<>();
//        PotOfMoney pot = new PotOfMoney();
        communityCards=deck.dealHand(4);
        List<Card> cards = List.of((Card[]) communityCards.getHand());

        ComputerTexasPlayer player=  new ComputerTexasPlayer("ad", 0, 0);

        PrintGame printPublic = new PrintGame(texasPlayers, pots, cards);
        printPublic.cardPrinter(false);

    }
}
