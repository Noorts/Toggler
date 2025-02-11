package core;

import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;
import utils.ConfigParser;
import utils.NotificationHandler;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TogglesConfig {
    private List<List<String>> toggles;

    /**
     * Instantiated with default toggles.
     */
    public TogglesConfig() {
        resetTogglesToDefault();
    }

    /**
     * Parses and loads provided toggles.
     */
    public TogglesConfig(@NotNull String togglesString) throws ConfigParser.TogglesFormatException {
        this.overwriteToggles(togglesString);
    }

    @Override
    public String toString() {
        return ConfigParser.toJson(this.toggles);
    }

    public void overwriteToggles(@NotNull String togglesString) throws ConfigParser.TogglesFormatException {
        this.toggles = ConfigParser.parseJsonToToggles(togglesString);
    }

    public void resetTogglesToDefault() {
        try {
            toggles = ConfigParser.parseJsonToToggles(Constants.DEFAULT_TOGGLES);
        } catch (ConfigParser.TogglesFormatException e) {
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

    /**
     * Takes the provided toggles and creates a regex pattern out of it that
     * matches any of the toggles.
     * <p>
     * The individual toggles have been escaped by wrapping them in \\Q and \\E.
     * This allows characters such as * that would normally be recognised as
     * regex operators to be included in the toggles.
     * <p>
     * The following is an example of the output of the method:
     * "(\\Qremove\\E|\\Qadd\\E)"
     *
     * @return The regex pattern packaged inside a String.
     */
    public String getRegexPatternOfToggles() {
        List<String> names = this.toggles.stream().flatMap(Collection::stream)
            .sorted(Comparator.comparingInt(String::length).reversed()).toList();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(\\Q").append(names.get(0)).append("\\E");
        for (int i = 1; i < names.size(); i++) {
            stringBuilder.append("|\\Q").append(names.get(i)).append("\\E");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
