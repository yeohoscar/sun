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
        String[] letters3 = {"U", "N", "O", "S"};
        String[] letters4 = {"u", "n", "o", "s"};

        player1.findAllWords(letters3);
    }
}
