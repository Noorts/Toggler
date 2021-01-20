package utils;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<List<String>> parseJson(String jsonText) {
        long numberOfParenthesesInJson = jsonText.chars().filter(ch -> ch == '"').count();

        // Crude error handling
        if (numberOfParenthesesInJson % 2 == 1) {
            System.out.println("The settings file is corrupt.");
            return null;
        }

        List<List<String>> togglesStructure = new ArrayList<>();

        jsonText = jsonText.replaceAll("[\\t\\n {4}]", "");

        if (jsonText.length() == 0) {
            System.out.println("The settings file is empty.");
            return null;
        }

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

    public static String toJson(List<List<String>> toggleWordsStructure) {
        StringBuilder stringBuilder = new StringBuilder();

        if (toggleWordsStructure == null) {
            return "[]";
        }

        stringBuilder.append("[\n");

        for (int i = 0; i < toggleWordsStructure.size(); i++) {
            stringBuilder.append("    [");
            for (int j = 0; j < toggleWordsStructure.get(i).size(); j++) {
                stringBuilder.append('"');
                stringBuilder.append(toggleWordsStructure.get(i).get(j));
                stringBuilder.append('"');
                if (j != toggleWordsStructure.get(i).size() - 1) stringBuilder.append(", ");
            }
            stringBuilder.append("]");
            if (i != toggleWordsStructure.size() - 1) stringBuilder.append(",\n");
        }

        stringBuilder.append("\n]");

        return stringBuilder.toString();
    }
}
