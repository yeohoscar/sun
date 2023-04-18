package texas_scramble;

//import org.junit.Test;
import org.junit.jupiter.api.Test;
import poker.*;
import texas_hold_em.*;

import java.util.ArrayList;
import java.util.List;

public class TestScrambleComputerPlayer {
    @Test
    public void testFindAllWords() {
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters1 = {"e", "n", "o", "p", "g", "i", "a"};
        String[] letters2 = {"A", "S", "S"};

        //test letters3 and letters4 first, these two should have worked
        String[] letters3 = {"U", "N", "O", "S"};
        String[] letters4 = {"Z", "Z", "Z", "S"};
        char[] str = new char[20];
        player1.dictionaryTrie.display(player1.dictionaryTrie.getRoot(), str, 0);
        player1.findAllWords(letters4);
    }
}
