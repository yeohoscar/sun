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
        player1.findAllWords(letters4);
    }
    @Test
    public void testFindHighestScoreWord(){
        ComputerScramblePlayer player1 = new ComputerScramblePlayer("Tom", 0, 0);
        String[] letters1 = {"e", "n", "o", "p", "g", "i", "a"};
        String[] letters2 = {"A", "S", "S"};

        //test letters3 and letters4 first, these two should have worked
        String[] letters3 = {"U", "N", "O", "S"};
        String[] letters4 = {"A", "G", "Z", "S", "M", "P", "Q"};
        //player1.findAllWords(letters4);
        String maxScoreWord = player1.findHighestScoreWord(letters4);
        System.out.println("max = "+maxScoreWord);
    }
}
