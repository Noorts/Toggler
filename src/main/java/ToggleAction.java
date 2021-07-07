import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import utils.StringTransformer;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Defines the logic used for the toggle action.
 *
 * @author Noorts
 */
public class ToggleAction extends AnAction {

    private static final NotificationGroup togglerNotificationGroup =
            new NotificationGroup("Toggler",
                    NotificationDisplayType.BALLOON, false);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();

        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        List<List<String>> toggleWordsStructure = appSettingsState.toggles;
        String regexPatternOfToggles = createRegexPatternOfToggles(toggleWordsStructure);

        /* Bandage (temporary fix) that might help remove the "ghost" caret that appears on load of the IDE. */
        editor.getCaretModel().setCaretsAndSelections(editor.getCaretModel().getCaretsAndSelections());

        List<Caret> carets = editor.getCaretModel().getAllCarets();

        /* Lock the file and perform the toggle on all carets in the editor. */
        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (Caret caret : carets) {
                performToggleOnSingleCaret(caret, document, editor,
                        regexPatternOfToggles, appSettingsState.partialMatchingIsEnabled);
            }
        });
    }

    /**
     * Will perform the toggle action on the provided caret.
     * Full selection of the word and a caret next to or inside the word are supported.
     *
     * @param caret    The caret to perform the toggle for.
     * @param document The document in which the caret(s) are present.
     * @param editor   The editor in which the caret(s) are present.
     */
    private void performToggleOnSingleCaret(Caret caret, Document document, Editor editor,
                String regexPatternOfToggles, boolean partialMatchingIsAllowed) {
        /* The validity of the carets are checked down below to prevent unexpected behavior.
         * E.g. when three carets are placed inside the same word/symbol and the toggle is pressed, the
         * first caret will be processed and the word/symbol will be toggled/replaced. The unintended consequence
         * is that the other two carets will be moved to the beginning of the document. I think they are moved
         * because the first caret has selected the entire word in which the other carets are located.
         * The loop in the actionPerformed method will move on to processing the next caret and will eventually
         * end up processing the carets that were moved to the beginning. Thus the caret will replace
         * the word/symbol on line 0 column 0.
         *
         * This is an unintended side effect for the user and to prevent this the carets are examined
         * and left unprocessed if the position of the caret was moved by the "bug". */
        if (!caret.isValid() || !caret.isUpToDate()) {
            return;
        }
        // Return if the caret is a multi-line selection as support for this hasn't been implemented yet.
        if (caret.getSelectionStartPosition().line != caret.getSelectionEndPosition().line) {
            Notifications.Bus.notify(new Notification(
                    togglerNotificationGroup.getDisplayId(), "Toggler",
                    "Toggling by finding keywords inside of a multi-line " +
                            "selection isn't supported by Toggler (yet).",
                    NotificationType.INFORMATION, null));
            return;
        }

        /* Positions of the beginning and end of the selection.
         * The position is held in two variables so that we can reselect it if necessary.
         * See the last comment block in this method for more information. */
        int oldPosition = caret.getOffset();

        /* If no word/symbol was selected, then a word/symbol will be selected automatically
         * next to or underneath the caret so that it can be replaced with the replacement word/symbol. */
        boolean caretHasASelection = caret.hasSelection();
        if (!caretHasASelection) {
            expandCaretSelection(caret, editor);
        }

        // Select the entire toggle/word from the caret so that it can be replaced by the replacement.
        String selectedToggleFromCaret = caret.getSelectedText();

        // Exit if the caret is in an empty file in which no toggle could possibly be selected.
        if (selectedToggleFromCaret == null) {
            Notifications.Bus.notify(new Notification(
                    togglerNotificationGroup.getDisplayId(), "Toggler",
                    "No text could be selected.",
                    NotificationType.INFORMATION, null));
            return;
        }

        /* This position is relative to the start of the expanded selection.
         * It is passed to the getPositionOfToggleMatch method to be able to determine if a partial match is
         * in contact with the caret. Being in contact is a requirement for a partial match. */
        int caretPositionInsideOfCurrentSelection = oldPosition - caret.getSelectionStart();

        List<Integer> positionOfMatch = getPositionOfToggleMatch(regexPatternOfToggles, selectedToggleFromCaret,
                (partialMatchingIsAllowed && !caretHasASelection), caretPositionInsideOfCurrentSelection);

        // If a match was found then toggle it, else display a notification.
        if (positionOfMatch != null) {
            String match = selectedToggleFromCaret.substring(positionOfMatch.get(0), positionOfMatch.get(1));
            String replacementToggle = findNextToggleInToggles(match);

            /* The replacementToggle should never be null in this case, because if no match was found then
             * the positionOfMatch would be null. There will always be a replacementToggle because even a single toggle
             * will toggle to itself, thus providing a replacementToggle. */
            assert replacementToggle != null;
            replacementToggle = StringTransformer.transferCapitalisation(match, replacementToggle);

            // Toggle the match to its replacement toggle. This is where the manipulation of the document is performed.
            int startPositionOfTextToReplace = caret.getSelectionStart() + positionOfMatch.get(0);
            int endPositionOfTextToReplace = caret.getSelectionStart() + positionOfMatch.get(1);
            document.replaceString(startPositionOfTextToReplace, endPositionOfTextToReplace, replacementToggle);
        } else {
            Notifications.Bus.notify(new Notification(
                    togglerNotificationGroup.getDisplayId(), "Toggler",
                    String.format("No match was found in: %s.<br>" +
                            "Add new words or symbols through the configuration menu.<br>" +
                            "Go to Settings/Preferences -> Tools -> Toggler.", selectedToggleFromCaret),
                    NotificationType.WARNING, null));
        }

        /* Reset the caret selection to the state before the action was performed.
         * E.g. - A caret with selection will turn into a selection of the replacement word.
         *      - No selection (just a caret) will not select the replacement word but will instead
         *        place the caret at the original position it was in. */
        if (!caretHasASelection) {
            /* We check if the position we want to reset the caret selection to exceeds the text length.
             * If it does, we set the caret to the end of the selection to prevent setting
             * it to an unavailable position. Else, the selection is set to the old position. */
            if (document.getTextLength() < oldPosition) {
                caret.setSelection(caret.getLeadSelectionOffset(), caret.getLeadSelectionOffset());
            } else {
                caret.setSelection(oldPosition, oldPosition);
            }
        }
    }

    /**
     * Expand the caret its selection to encompass a word/symbol. This is done by expanding the selection towards
     * both the left and right side until a boundary character or the beginning or end of the line is found.
     *
     * Boundary characters are hardcoded characters such as ; and ".
     * Check the method its implementation for more details.
     *
     * @param caret    The caret to perform the toggle for.
     * @param editor   The editor in which the caret(s) are present.
     */
    private void expandCaretSelection(Caret caret, Editor editor) {
        /* The characters that indicate a word/symbol its boundaries. Used for left and right side.
         * The beginning and end of the line the caret is on also function as boundaries.
         * Boundary characters should be declared somewhere else to improve
         * maintainability as these characters are also used elsewhere. */
        Character[] boundaryChars = {' ', ';', ':', '.', ',', '`', '"', '\'', '(', ')', '[', ']', '{', '}'};
        int currentColumnLeftSide = caret.getSelectionStartPosition().column;
        int currentColumnRightSide = caret.getSelectionEndPosition().column;
        int currentLine = caret.getLeadSelectionPosition().line;

        String textOnCurrentLine = editor.getDocument().getText(TextRange.create(
                editor.getDocument().getLineStartOffset(currentLine),
                editor.getDocument().getLineEndOffset(currentLine)));

        /* Try catch added as temporary measure against StringIndexOutOfBoundsException.
         * The exception is sometimes thrown when the caret isn't set correctly after
         * the user selects a different location. */
        try {
            /* Text expansion by extending the left side and then the right side. */
            while ((currentColumnLeftSide > 0) &&
                    (-1 == Arrays.toString(boundaryChars).indexOf(
                            textOnCurrentLine.charAt(currentColumnLeftSide - 1)))) {
                currentColumnLeftSide--;
            }
            while ((currentColumnRightSide < textOnCurrentLine.length()) &&
                    (-1 == Arrays.toString(boundaryChars).indexOf(
                            textOnCurrentLine.charAt(currentColumnRightSide)))) {
                currentColumnRightSide++;
            }
        } catch (StringIndexOutOfBoundsException ignored){}

        /* Start and end offset are determined because those are required for the setSelection method.
         * The offsets indicate the offset from the beginning of the document, so including all lines. */
        int startOffset = editor.logicalPositionToOffset(new LogicalPosition(currentLine, currentColumnLeftSide));
        int endOffset = editor.logicalPositionToOffset(new LogicalPosition(currentLine, currentColumnRightSide));
        caret.setSelection(startOffset, endOffset);
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        // Make sure at least one caret is available.
        boolean menuAllowed = false;
        if (editor != null && project != null) {
            // Ensure the list of carets in the editor is not empty.
            menuAllowed = !editor.getCaretModel().getAllCarets().isEmpty();
        }
        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    /**
     * Find the next word/symbol for the provided word/symbol in the toggles.
     * The provided word/symbol is searched for in the toggles configured
     * in the plugin settings and the next one in the sequence is returned.
     * The settings can be found under Settings/Preferences -> Tools -> Toggler.
     *
     * @param keyword The word/symbol to be replaced.
     * @return The next word/symbol in the sequence which the provided word/symbol is part of.
     * Null is returned if the provided word couldn't be found in the config.
     */
    private String findNextToggleInToggles(String keyword) {
        String wordInLowerCase = keyword.toLowerCase();
        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        /* The current settings. */
        List<List<String>> toggleWordsStructure = appSettingsState.toggles;

        /* O(n) search for the word/symbol to replace. */
        for (int i = 0; i < toggleWordsStructure.size(); i++) {
            for (int j = 0; j < toggleWordsStructure.get(i).size(); j++) {
                if (toggleWordsStructure.get(i).get(j).toLowerCase().equals(wordInLowerCase)) {
                    /* The next word/symbol in the sequence is retrieved.
                       The modulo is used to wrap around if the end of the sequence is reached. */
                    return toggleWordsStructure.get(i).get((j + 1) % toggleWordsStructure.get(i).size());
                }
            }
        }

        /* Null is returned if the word/symbol couldn't be found. */
        return null;
    }

    /**
     * Will take the provided toggles and create a regex pattern out of it that will match any of the toggles.
     *
     * The individual toggles have been escaped by wrapping them in \\Q and \\E. This allows characters such as * that
     * would normally be recognised as regex operators to be included in the toggles.
     *
     * The following is an example of the output of the method:
     * "(\\Qremove\\E|\\Qadd\\E)"
     *
     * @param toggleWordsStructure The data structure that holds the toggles.
     * @return The regex pattern inside of a String.
     */
    public String createRegexPatternOfToggles(List<List<String>> toggleWordsStructure) {
        List<String> names = toggleWordsStructure.stream().flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(String::length).reversed()).collect(Collectors.toList());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(\\Q").append(names.get(0)).append("\\E");
        for (int i = 1; i < names.size(); i++) {
            stringBuilder.append("|\\Q").append(names.get(i)).append("\\E");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * Provided an input, search for toggles inside of that input and if found and the caret touches it,
     * return the match its position using its beginning and end indices relative to the input string.
     * A caret touching a match means that the caret its position is inside of the match or on the edge of it.
     *
     * Priority is based on the following: greater length and left > right.
     *
     * @param regexPatternOfToggles A regex pattern that contains all toggles.
     *                              For more information check out the createRegexPatternOfToggles method.
     * @param input The text that will be searched for matches.
     * @param allowPartialMatch Whether to allow partial matches or not. If not, then only a match that is of
     *                          the same length as the input (aka a full match) is deemed valid.
     * @param caretPosition The position the caret is in relative to the input.
     *                      E.g. if the input is "add", then the caretPosition should be between 0 and 4.
     * @return A pair of integers that indicate the beginning and end of the match relative to the input string.
     */
    public List<Integer> getPositionOfToggleMatch(String regexPatternOfToggles, String input,
                                                  boolean allowPartialMatch, int caretPosition) {
        Matcher matcher = Pattern.compile(regexPatternOfToggles, Pattern.CASE_INSENSITIVE).matcher(input);

        // Sort the matches by string length, so that longer matches get priority over smaller ones.
        List<MatchResult> matches = matcher.results().collect(Collectors.toList());

        // A full match is returned if it can be found.
        if (matches.size() != 0 && matches.get(0).end() - matches.get(0).start() == input.length()) {
            return new ArrayList<>(Arrays.asList(matches.get(0).start(), matches.get(0).end()));
        } else if (allowPartialMatch) {
            /* Else, if partial matches are allowed, a partial match is returned. This is only done if it has
             * the caret inside of it. Because the toggles were sorted by string length whilst creating the regex
             * pattern in @createRegexPatternOfToggles, the longer matches get priority. */
            for (MatchResult match : matches) {
                if (match.start() <= caretPosition && match.end() >= caretPosition) {
                    return new ArrayList<>(Arrays.asList(match.start(), match.end()));
                }
            }
        }
        // If no match that fits the requirements is found, then we return null.
        return null;
    }
}
