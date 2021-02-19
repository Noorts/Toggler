import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.components.BorderLayoutPanel;
import org.jetbrains.annotations.NotNull;
import utils.JsonParser;

import javax.swing.*;
import java.awt.*;

public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextArea jsonText = new JBTextArea();
    private final JBLabel statusLabel = new JBLabel();

    public AppSettingsComponent() {
        JButton resetButton = new JButton("Reset to Defaults");
        resetButton.setToolTipText("Reset the toggles to the default toggles the plugin ships with. " +
                "Note: the reset is applied instantly.");

        resetButton.addActionListener(a -> executeResetButtonAction());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(resetButton);

        BorderLayoutPanel headerPanel = new BorderLayoutPanel();
        headerPanel.add(statusLabel, BorderLayout.LINE_START);
        headerPanel.add(buttonPanel, BorderLayout.LINE_END);

        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(headerPanel)
                .addComponentFillVertically(jsonText, 0)
                .getPanel();
    }

    public void executeResetButtonAction() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        appSettingsState.resetTogglesToDefault();
        setJsonText(JsonParser.toJson(appSettingsState.toggles));
        setErrorMessage("Status: resetting JSON to defaults was successful.");
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return jsonText;
    }

    @NotNull
    public String getJsonText() {
        return jsonText.getText();
    }

    public void setJsonText(@NotNull String newText) {
        jsonText.setText(newText);
    }

    public void setErrorMessage(@NotNull String errorMessage) {
        statusLabel.setText(errorMessage);
    }
}
