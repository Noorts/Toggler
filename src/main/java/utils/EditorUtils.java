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

    private static boolean isBoundaryChar(char c) {
        return Constants.BOUNDARY_CHARS.contains(c);
    }

    /**
     * Expand the caret's selection to encompass a word/symbol.
     * <p>
     * There is the built-in {@link Caret#selectWordAtCaret}, but custom behavior was desired
     * for Toggler.
     * <p>
     * The selection is expanded from the caret position to the left and right until a boundary
     * character ({@link Constants#BOUNDARY_CHARS}) or the beginning or end of the line is found.
     * An existing caret selection is overwritten.
     */
    public static void expandCaretSelection(Caret caret, Editor editor) {
        Project project = editor.getProject();

        int selectionStart = caret.getLogicalPosition().column;
        int selectionEnd = selectionStart;
        int currentLine = caret.getLogicalPosition().line;

        String textLine = editor.getDocument().getText(TextRange.create(
            editor.getDocument().getLineStartOffset(currentLine),
            editor.getDocument().getLineEndOffset(currentLine)));

        /* The clause below provides limited support for tabs as whitespace in
         * files. It requires the "Use tab character" option in the "Tabs and
         * Indents" menu to be ticked and requires all tabs in the file to
         * adhere to the "Tab size" set in the same menu.
         *
         * Handling ambiguous tab sizes (e.g., tabs of size 2 and 4 in the same
         * document) is not supported yet. */
        boolean projectUsesTabCharacter = editor.getSettings().isUseTabCharacter(project);
        if (projectUsesTabCharacter) {
            int editorTabSize = editor.getSettings().getTabSize(project);
            textLine = textLine.replace("\t", " ".repeat(editorTabSize));
        }

        /* Try catch added as temporary measure against
         * StringIndexOutOfBoundsException. The exception is sometimes thrown
         * when the caret isn't set correctly after the user selects a different
         * location. */
        try {
            // Caret positions are in between characters. So for "true", start = 0, end = 4.
            while (0 < selectionStart && !isBoundaryChar(textLine.charAt(selectionStart - 1))) {
                selectionStart--;
            }
            while (selectionEnd < textLine.length() && !isBoundaryChar(textLine.charAt(selectionEnd))) {
                selectionEnd++;
            }
        } catch (StringIndexOutOfBoundsException ignored) {}

        // Offsets start from the beginning of the document (they include all lines).
        int startOffset = editor.logicalPositionToOffset(new LogicalPosition(currentLine, selectionStart));
        int endOffset = editor.logicalPositionToOffset(new LogicalPosition(currentLine, selectionEnd));

        caret.setSelection(startOffset, endOffset);
    }
}
