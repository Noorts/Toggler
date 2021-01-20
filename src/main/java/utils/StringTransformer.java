package utils;

public class StringTransformer {
    /**
     * Transfer the capitalisation from the currentWord to the replacementWord.
     * Beware: the current implementation is very limited and only supports:
     *      FULL CAPS,
     *      First letter capitalised,
     *      All other scenarios will default to the provided form of the replacementWord.
     * @param currentWord The word from which the capitalisation scheme is determined.
     * @param replacementWord The word to transform and return.
     * @return The replacementWord after being transformed.
     */
    public static String transferCapitalisation(String currentWord, String replacementWord) {
        char[] symbolsInCurrentWord = currentWord.toCharArray();
        int amountOfUpperCaseCharactersInCurrentWord = 0;
        int lengthOfCurrentWord = symbolsInCurrentWord.length;

        for (int i = 0; i < lengthOfCurrentWord; i++) {
            if (Character.isUpperCase(symbolsInCurrentWord[i])) {
                amountOfUpperCaseCharactersInCurrentWord++;
            }
        }

        /* All characters are uppercase. */
        if (amountOfUpperCaseCharactersInCurrentWord == lengthOfCurrentWord) {
            return replacementWord.toUpperCase();
        }

        /* Only first character is uppercase. */
        if (amountOfUpperCaseCharactersInCurrentWord == 1 && Character.isUpperCase(symbolsInCurrentWord[0])) {
            return replacementWord.substring(0, 1).toUpperCase() + replacementWord.substring(1);
        }

        /* Default (likely a fully lowercase) replacement word is returned. */
        return replacementWord;
    }
}
