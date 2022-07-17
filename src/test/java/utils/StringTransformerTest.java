package utils;

import org.junit.Test;
import utils.StringTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringTransformerTest {
    @Test
    public void transferFullCapsCorrectly(){
        String wordToAdjust = "PIZZA";
        String replacementWord = "Rice";
        String correctWord = "RICE";

        String resultWord = StringTransformer.transferCapitalisation(wordToAdjust, replacementWord);
        assertEquals(correctWord, resultWord, "Full Caps is not being transferred correctly.");
    }

    @Test
    public void transferFirstLetterCorrectly(){
        String wordToAdjust = "Pizza";
        String replacementWord = "rice";
        String correctWord = "Rice";

        String resultWord = StringTransformer.transferCapitalisation(wordToAdjust, replacementWord);
        assertEquals(correctWord, resultWord, "Capitalised first letter is not being transferred correctly.");
    }

    @Test
    public void transferUsingDefaultCorrectly(){
        String wordToAdjust = "PiZzA";
        String replacementWord = "rice";
        String correctWord = "rice";

        String resultWord = StringTransformer.transferCapitalisation(wordToAdjust, replacementWord);
        assertEquals(correctWord, resultWord,
                "Defaulting to standard capitalisation on unknown is not working correctly.");
    }
}
