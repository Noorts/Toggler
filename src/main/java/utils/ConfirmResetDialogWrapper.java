package utils;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * The DialogWrapper used to confirm if the toggles should be reset or not. This dialog is displayed once
 * the "Reset to Defaults" button in the configuration menu is pressed.
 *
 * See: https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html
 *
 * @author Noorts
 */
public class ConfirmResetDialogWrapper extends DialogWrapper {
    private static final String TITLE = "Confirm Reset";
    private static final String LABEL_TEXT = "<html>Are you sure you want to reset the toggles to the defaults?</html>";

    public ConfirmResetDialogWrapper() {
        super(true);
        setTitle(TITLE);
        init();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action[] actions = new Action[2];
        actions[0] = new DialogWrapperExitAction("Yes", OK_EXIT_CODE);
        actions[1] = new DialogWrapperExitAction("No", CLOSE_EXIT_CODE);
        return actions;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(LABEL_TEXT);
        label.setPreferredSize(new Dimension(60, 60));
        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }
}
