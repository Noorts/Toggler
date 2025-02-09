package core;

import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;
import utils.JsonParser;
import utils.NotificationHandler;

import java.util.List;

public class TogglesConfig {
    private List<List<String>> toggles;

    public TogglesConfig() {
        resetTogglesToDefault();
    }

    public void resetTogglesToDefault() {
        try {
            toggles = JsonParser.parseJsonToToggles(Constants.DEFAULT_TOGGLES);
        } catch (JsonParser.TogglesFormatException e) {
            NotificationHandler.notify("The defaultToggles provided by the creator of the " +
                    "plugin don't conform to the JSON format.",
                NotificationType.ERROR);
        }
    }

    /**
     * Find the next or previous word/symbol for the provided word/symbol in the
     * toggles. The provided word/symbol is searched for in the toggles
     * configured in the plugin settings and the next or previous one in the
     * sequence is returned. Whether the next or previous toggle in the sequence
     * is returned depends on the toggleForward parameter.
     *
     * @param word          The word/symbol to be replaced.
     * @param toggleForward Determines whether the next or previous toggle in the sequence is returned.
     * @return The next/previous word/symbol in the sequence that the provided
     * word/symbol is part of. Null is returned if the provided word couldn't be
     * found in the config.
     */
    public String findReplacementWord(String word, boolean toggleForward) {
        String wordInLowerCase = word.toLowerCase();

        /* O(n) search for the word/symbol to replace. */
        for (int i = 0; i < toggles.size(); i++) {
            for (int j = 0; j < toggles.get(i).size(); j++) {
                if (toggles.get(i).get(j).toLowerCase().equals(wordInLowerCase)) {
                    /* The next word/symbol in the sequence is retrieved. The
                       modulo is used to wrap around if the end of the sequence
                       is reached. */
                    int sequenceSize = toggles.get(i).size();
                    return toggles.get(i).get(
                        toggleForward
                            ? (j + 1) % sequenceSize // Next
                            : (j - 1 + sequenceSize) % sequenceSize // Previous
                    );
                }
            }
        }

        /* The word/symbol could not be found. */
        return null;
    }

    public static TogglesConfig deserialize(@NotNull String togglesString) {
        TogglesConfig config = new TogglesConfig();
        try {
            config.toggles = JsonParser.parseJsonToToggles(togglesString);
            return config;
        } catch (JsonParser.TogglesFormatException e) {
            NotificationHandler.notify("The toggles couldn't be parsed from the " +
                    "plugin setting storage successfully.",
                NotificationType.ERROR);
            return null;
        }
    }

    public String serialize() {
        return JsonParser.toJson(this.toggles);
    }

    public List<List<String>> getToggles() {
        return toggles;
    }

    public void setToggles(List<List<String>> toggles) {
        this.toggles = toggles;
    }
}
