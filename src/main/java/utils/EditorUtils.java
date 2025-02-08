package utils;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import core.Constants;

public class EditorUtils {
    private EditorUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Expand the caret's selection to encompass a word/symbol. This is done
     * by expanding the selection towards both the left and right side until a
     * boundary character or the beginning or end of the line is found.
     * <p>
     * This method will expand outwards from the cursor position of the caret.
     * If a selection has already been made, then that won't be taken into
     * account.
     */
    public static void expandCaretSelection(Caret caret, Editor editor) {
        Project project = editor.getProject();

        int currentColumnLeftSide = caret.getLogicalPosition().column;
        int currentColumnRightSide = currentColumnLeftSide;
        int currentLine = caret.getLogicalPosition().line;

        String textOnCurrentLine = editor.getDocument().getText(TextRange.create(
            editor.getDocument().getLineStartOffset(currentLine),
            editor.getDocument().getLineEndOffset(currentLine)));

        /* The clause below provides limited support for tabs as whitespace in
         * files. It requires the "Use tab character" option in the "Tabs and
         * Indents" menu to be ticked and requires all tabs in the file to
         * adhere to the "Tab size" set in the same menu.
         *
         * Handling ambiguous tab sizes is a more complicated problem and is
         * left to be solved. */
        boolean projectUsesTabCharacter = editor.getSettings().isUseTabCharacter(project);
        if (projectUsesTabCharacter) {
            int editorTabSize = editor.getSettings().getTabSize(project);
            textOnCurrentLine = textOnCurrentLine.replace("\t", " ".repeat(editorTabSize));
        }

        /* Try catch added as temporary measure against
         * StringIndexOutOfBoundsException. The exception is sometimes thrown
         * when the caret isn't set correctly after the user selects a different
         * location. */
        try {
            /* Text expansion by extending the left side and then the right side. */
            while (0 < currentColumnLeftSide &&
                !Constants.BOUNDARY_CHARS.contains(textOnCurrentLine.charAt(currentColumnLeftSide - 1)))
            {
                currentColumnLeftSide--;
            }

            while (currentColumnRightSide < textOnCurrentLine.length() &&
                !Constants.BOUNDARY_CHARS.contains(textOnCurrentLine.charAt(currentColumnRightSide)))
            {
                currentColumnRightSide++;
            }
        } catch (StringIndexOutOfBoundsException ignored) {}

        /* Start and end offset are determined because those are required for
         * the setSelection method. The offsets indicate the offset from the
         * beginning of the document, so including all lines. */
        int startOffset = editor.logicalPositionToOffset(new LogicalPosition(currentLine, currentColumnLeftSide));
        int endOffset = editor.logicalPositionToOffset(new LogicalPosition(currentLine, currentColumnRightSide));
        caret.setSelection(startOffset, endOffset);
    }
}
