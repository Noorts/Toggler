package utils;

import java.util.ArrayList;
import java.util.List;

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
        jsonText = jsonText.replaceAll("[\\t\\n {4}]", "");

        // Will check the JSON for errors and throw an error if required.
        checkJsonForErrors(jsonText);

        String[] toggles = jsonText.substring(1, jsonText.length() - 1).split("], *\\[");
        for (String toggle : toggles) {
            List<String> listOfToggles = new ArrayList<>();
            String[] words = toggle.split(", *");
            for (String word : words) {
                word = word.replaceAll("[\\[\\]\"]", "");
                listOfToggles.add(word);
            }
            togglesStructure.add(listOfToggles);
        }

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
            stringBuilder.append("    [");
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
