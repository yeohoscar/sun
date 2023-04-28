package texas.scramble.test;

import org.junit.jupiter.api.Test;
import texas.scramble.deck.DeckOfTiles;
import texas.scramble.deck.Tile;
import texas.scramble.hand.ScrambleHand;
import texas.scramble.player.HumanScramblePlayer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class testHummanScramblePlayer {
    @Test
    public void testSubmitWord(){

        HumanScramblePlayer player = new HumanScramblePlayer("qwe",100,0);
        List<Tile> communityTiles = new ArrayList<>();
        DeckOfTiles deck = new DeckOfTiles();
        communityTiles.add(new Tile("T",1));
        communityTiles.add(new Tile("E",1));
        communityTiles.add(new Tile("E",1));
        communityTiles.add(new Tile("N",1));
        communityTiles.add(new Tile("A",1));
        player.dealTo(deck);
        System.out.println("CommunityTiles"+communityTiles);
// Set up the input for the user
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("TEA\nN\n".getBytes());
        System.setIn(in);

        // Call the submitWord method and get the result
        int result1 = player.submitWord(communityTiles);

        // Check if the result is correct
        assertEquals(3, result1);

        // Restore the original System.in
        System.setIn(sysInBackup);

    }


    public void negative(int i){

    }
}
