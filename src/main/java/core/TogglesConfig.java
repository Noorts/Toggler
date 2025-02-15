package core;

import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;
import utils.ConfigParser;
import utils.NotificationHandler;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TogglesConfig {
    private List<List<String>> toggles;

    private Pattern regexPatternOfTogglesCache = null;

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
        invalidateCache();
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

    private void invalidateCache() {
        this.regexPatternOfTogglesCache = null;
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
     * Returns a regex pattern that matches any of the configured toggles.
     * The regex is case-insensitive.
     * <p>
     * The individual toggles have been escaped by wrapping them in \\Q and \\E.
     * This allows characters such as * to be included in the toggles.
     * These would normally be recognised as regex operators.
     * <p>
     * An example of what the pattern might represent:
     * "(\\Qremove\\E|\\Qadd\\E)"
     */
    public Pattern getRegexPatternOfToggles() {
        if (regexPatternOfTogglesCache == null) {
            this.regexPatternOfTogglesCache = buildRegexPatternOfToggles();
        }
        return regexPatternOfTogglesCache;
    }

    private Pattern buildRegexPatternOfToggles() {
        String regexStringOfToggles = this.toggles.stream()
            .flatMap(Collection::stream)
            // sort to prioritize large matches over smaller matches.
            .sorted(Comparator.comparingInt(String::length).reversed())
            .map(s -> "\\Q" + s + "\\E")
            .collect(Collectors.joining("|", "(", ")"));

        return Pattern.compile(regexStringOfToggles, Pattern.CASE_INSENSITIVE);
    }
}
