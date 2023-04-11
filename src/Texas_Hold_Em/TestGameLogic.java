package Texas_Hold_Em;

import org.junit.jupiter.api.Test;
import poker.*;

import java.util.ArrayList;
import java.util.List;
public class TestGameLogic {


    @Test
    public void testPreFlopRound() {
        DeckOfCards deck = new DeckOfCards();
        String[] names = {"Human", "Tom", "Dick", "Harry"};
        TexasController controller = new TexasController();
        controller.setUp(names, 100);
        ArrayList<TexasPlayer> texasPlayers = controller.texasPlayers;
//        texasPlayers.add(new HumanTexasPlayer("Human", 100, 0));
//        texasPlayers.add(new ComputerTexasPlayer("Tom", 95, 1));
//        texasPlayers.add(new ComputerTexasPlayer("Dick", 90, 2));
//        texasPlayers.add(new ComputerTexasPlayer("Harry", 100, 3));
//
//        Card[] card1 = new Card[2];
//        card1[0] = new NumberCard("Jack", "clubs", 11);
//        card1[1] = new NumberCard("Ten", "hearts", 10);
//        texasPlayers.get(0).getHand()=new PokerHand(card1, deck);
//        System.out.println(texasPlayers.get(0).getHand());
//        texasPlayers.get(0).getHand().getHand()[0] = new NumberCard("Jack", "clubs", 11);
//        texasPlayers.get(0).getHand().getHand()[1] = new NumberCard("Ten", "hearts", 10);
//        texasPlayers.get(1).getHand().getHand()[0] = new NumberCard("Queen", "hearts", 12);
//        texasPlayers.get(1).getHand().getHand()[1] = new NumberCard("Three", "spades", 3);
//        texasPlayers.get(2).getHand().getHand()[0] = new NumberCard("Eight", "hearts", 8);
//        texasPlayers.get(2).getHand().getHand()[1] = new NumberCard("Nine", "spades", 9);
//        texasPlayers.get(3).getHand().getHand()[0] = new NumberCard("Jack", "spades", 11);
//        texasPlayers.get(3).getHand().getHand()[1] = new NumberCard("Three", "hearts", 3);
        texasPlayers.get(1).setDealer(true);
        List<Card> communityCards = new ArrayList<>();
        communityCards.add(new NumberCard("Ace", "hearts", 1, 14));
        communityCards.add(new NumberCard("Deuce", "hearts", 2));
        communityCards.add(new NumberCard("Three", "hearts", 3));

        RoundsOfTexas texas = new RoundsOfTexas(deck, texasPlayers, communityCards, 0);
        texas.play();
    }

    @Test
    public void testFlopRound() {
        DeckOfCards deck = new DeckOfCards();
        String[] names = {"Human", "Tom", "Dick", "Harry", "Yan", "wang"};
        TexasController controller = new TexasController();
        controller.setUp(names, 100);
        ArrayList<TexasPlayer> texasPlayers = controller.texasPlayers;
        texasPlayers.get(1).setDealer(true);
        Card[] communityCards = new Card[]{
                new NumberCard("Ace", "hearts", 1, 14),
                new NumberCard("Deuce", "hearts", 2),
                new NumberCard("Three", "hearts", 3)};

    }
}
