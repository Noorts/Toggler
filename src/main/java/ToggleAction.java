import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import utils.StringTransformer;

import java.util.List;

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

        List<Caret> carets = editor.getCaretModel().getAllCarets();

        /* Lock the file and perform the toggle on all carets in the editor. */
        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (Caret caret : carets) {
                performToggleOnSingleCaret(caret, document);
            }
        });

    }

    /**
     * Will perform the toggle action on the provided caret.
     * Full selection of the word and a caret next to or inside the word are supported.
     * @param caret The caret to perform the toggle for.
     * @param document The document in which the caret(s) are present.
     */
    public void performToggleOnSingleCaret(Caret caret, Document document) {
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
         *  The position is held in two variables so that we can reselect it if necessary.
         *  See the last comment block in this method for more information. */
        int start = caret.getSelectionStart();
        int end = caret.getSelectionEnd();
        int oldPosition = end;

        /* If no word was selected, then a word will be selected automatically
         * next to or underneath the caret so that it can be replaced with the new word. */
        boolean caretHasASelection = caret.hasSelection();
        if (!caretHasASelection) {
            caret.selectWordAtCaret(false);
            start = caret.getSelectionStart();
            end = caret.getSelectionEnd();
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

        // Get the replacementToggle and toggle the caret its toggle with the replacement.
        String replacementToggle = findNextToggleInToggles(selectedToggleFromCaret);
        if (replacementToggle != null) {
            replacementToggle = StringTransformer.transferCapitalisation(
                    selectedToggleFromCaret, replacementToggle);
            document.replaceString(start, end, replacementToggle);
        } else {
            Notifications.Bus.notify(new Notification(
                    togglerNotificationGroup.getDisplayId(), "Toggler",
                            "No match was found.<br>" +
                                    "Add new words through the configuration menu.<br>" +
                                    "Go to Preferences -> Tools -> Toggler.",
                    NotificationType.WARNING, null));
        }

        /* Reset the caret selection to the state before the action was performed.
         * E.g. A selection will turn into a selection of the replacement word.
         *      No selection will not select the replacement word but will instead
         *      place the caret at the original position it was in. */
        if (!caretHasASelection) {
            caret.setSelection(oldPosition, oldPosition);
        }
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        e.getPresentation().setEnabledAndVisible(project != null && editor != null);
    }

    /**
     * Find the next word/symbol for the provided word/symbol in the toggles.
     * The provided word/symbol is searched for in the toggles configured
     * in the plugin settings and the next one in the sequence is returned.
     * The settings can be found under Preferences -> Tools -> Toggler.
     * @param keyword The word/symbol to be replaced.
     * @return The next word/symbol in the sequence which the provided word/symbol is part of.
     *         Null is returned if the provided word couldn't be found in the config.
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
}
