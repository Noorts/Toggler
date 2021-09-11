package utils;

import java.util.*;

/**
 * A utility written to handle parsing and composing between a String and the plugin its toggles data structure.
 *
 * @author Noorts
 */
public class JsonParser {
    /**
     * Check the provided text for errors that indicate that the text doesn't adhere to the toggles JSON format.
     * See the defaultToggles defined in the AppSettingsState class for more information about the format.
     * @param jsonText that contains the toggles in the JSON format defined by this plugin.
     * @throws TogglesFormatException when the toggles in the provided String are malformed.
     */
    private static void checkJsonForErrors(String jsonText) throws TogglesFormatException {
        long numberOfParenthesesInJson = jsonText.chars().filter(ch -> ch == '"').count();
        if (numberOfParenthesesInJson % 2 == 1) {
            throw new TogglesFormatException("The toggles are malformed (check parentheses).");
        }

        if (jsonText.length() == 0) {
            throw new TogglesFormatException("No toggles could be found.");
        }
    }

    /**
     * Parse JSON-formatted text to the toggles data structure defined by this plugin.
     * @param jsonText that contains the toggles in the JSON format defined by this plugin.
     *                 See the defaultToggles defined in the AppSettingsState class
     *                 for more information about the format.
     * @return the toggles data structure.
     * @throws TogglesFormatException when the toggles in the provided String are malformed.
     */
    public static List<List<String>> parseJsonToToggles(String jsonText) throws TogglesFormatException {
        List<List<String>> togglesStructure = new ArrayList<>();
        jsonText = jsonText.replaceAll("\\t|\\n|\\s{4}", "");

        // Will check the JSON for errors and throw an error if required.
        checkJsonForErrors(jsonText);

        String[] toggles = jsonText.substring(1, jsonText.length() - 1).split("], *\\[");
        for (String toggle : toggles) {
            List<String> listOfToggles = new ArrayList<>();
            String[] words = toggle.split(", *");
            for (String word : words) {
                word = word.replaceAll("[\\[\\]\"]", "");
                // Throw an error if the word contains any forbidden boundary characters.
                checkIfWordContainsABoundaryCharacter(word);
                listOfToggles.add(word);
            }
            togglesStructure.add(listOfToggles);
        }

        /* Will check if the togglesStructure contains any duplicate words/symbols.
           An error will be thrown if a duplicate is found. */
        checkTogglesForDuplicates(togglesStructure);

        return togglesStructure;
    }

    /**
     * Compose a String in JSON format of the provided toggles.
     * @param togglesStructure the toggles in the plugin its toggles data structure.
     * @return a String in JSON format.
     */
    public static String toJson(List<List<String>> togglesStructure) {
        StringBuilder stringBuilder = new StringBuilder();

        if (togglesStructure == null) {
            return "[]";
        }

        stringBuilder.append("[\n");

        for (int i = 0; i < togglesStructure.size(); i++) {
            stringBuilder.append("\t[");
            for (int j = 0; j < togglesStructure.get(i).size(); j++) {
                stringBuilder.append('"');
                stringBuilder.append(togglesStructure.get(i).get(j));
                stringBuilder.append('"');
                if (j != togglesStructure.get(i).size() - 1) stringBuilder.append(", ");
            }
            stringBuilder.append("]");
            if (i != togglesStructure.size() - 1) stringBuilder.append(",\n");
        }

        stringBuilder.append("\n]");

        return stringBuilder.toString();
    }

    /**
     * Check if the provided toggles contain any duplicate words/symbols.
     * I.e. if a duplicate word/symbol is found in two different toggles
     * or if a duplicate is found inside of the same toggle.
     * @param toggles the data structure in which the toggles are defined.
     * @throws TogglesFormatException when a duplicate word/symbol was found.
     */
    private static void checkTogglesForDuplicates(List<List<String>> toggles) throws TogglesFormatException {
        HashSet<String> set = new HashSet<>();
        for (List<String> toggle : toggles) {
            for (String string : toggle) {
                if (!set.add(string.toLowerCase())){
                    throw new TogglesFormatException("Duplicate word/symbol was found.");
                }
            }
        }
    }

    /**
     * Check if the provided word contains a boundary character.
     * The boundary characters are used in the toggle action to detect
     * a word/symbol by expanding the selection from a caret.
     * @param word the word/symbol to be checked.
     * @throws TogglesFormatException when a word/symbol was found that contains a boundary character.
     */
    private static void checkIfWordContainsABoundaryCharacter(String word) throws TogglesFormatException {
        /* Boundary characters should be declared somewhere else to improve
         * maintainability as these characters are also used elsewhere. */
        Character[] boundaryChars = {' ', ';', ':', '.', ',', '`', '"', '\'', '(', ')', '[', ']', '{', '}'};

        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < boundaryChars.length; j++) {
                if (word.charAt(i) == boundaryChars[j]) {
                    throw new TogglesFormatException("A toggle contains an invalid character.");
                }
            }
        }
    }

    /**
     * A custom exception thrown when toggles parsed from a String are malformed.
     * @see JsonParser#checkJsonForErrors
     * @see JsonParser#parseJsonToToggles
     */
    public static class TogglesFormatException extends Exception {
        public TogglesFormatException(String errorMessage) {
            super(errorMessage);
        }
    }
}
