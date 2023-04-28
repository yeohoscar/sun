package texas.scramble.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import texas.scramble.player.ScrambleComputerPlayer;

public class TestDictionary {
    @Test
    public void testLearnWord() {
        ScrambleComputerPlayer com = new ScrambleComputerPlayer("TEST", 100, 0);

        Assertions.assertTrue(com.knowsWord("APPLE"));
        Assertions.assertFalse(com.knowsWord("AB"));

        com.learnWord("AB");

        Assertions.assertTrue(com.knowsWord("AB"));
    }
}
