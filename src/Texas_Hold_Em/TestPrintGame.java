package Texas_Hold_Em;

import org.junit.Test;
import poker.*;

import java.util.ArrayList;

public class TestPrintGame {
    @Test
    public void testPrintPublicCard() {
        DeckOfCards deck = new DeckOfCards();
        deck.reset();
        Hand communityCards;
        ArrayList<TexasPlayer> texasPlayers = new ArrayList<>();
        PotOfMoney pot = new PotOfMoney();
        communityCards=deck.dealHand(3);
//        Hand community = new PokerHand();
////        DeckOfCards deck;
//        ArrayList<Card> communityCards = new ArrayList<>();
//        communityCards.add(new NumberCard("Ace", "hearts", 1, 14));
//        communityCards.add(new NumberCard("Deuce", "diamonds", 2));
//        communityCards.add(new NumberCard("Three", "clubs", 3));
        PrintGame printPublic = new PrintGame(texasPlayers, deck, pot, communityCards);
        printPublic.printPublicCard(communityCards);

    }
    @Test
    public void testCardPrinter() {
        DeckOfCards deck = new DeckOfCards();
        deck.reset();
        String[] showCard1 = {"╭────╮","|    |", "|    |","╰────╯"};
        String[] showCard2 = {"╭────╮","|    |", "|    |","╰────╯"};
        String[] names = {"Human", "Tom", "Dick", "Harry"};
        TexasController controller = new TexasController();
        controller.setUp(names, 0);
        ArrayList<TexasPlayer> texasPlayers = controller.texasPlayers;
        for(Player player : texasPlayers){
            player.dealTo(deck);
        }
        Hand communityCards;
        PotOfMoney pot = new PotOfMoney();
        communityCards=deck.dealHand(0);
        ComputerTexasPlayer player=  new ComputerTexasPlayer("ad", 0);

        PrintGame printPublic = new PrintGame(texasPlayers, deck, pot, communityCards);
        printPublic.cardPrinter(false);

    }
}
