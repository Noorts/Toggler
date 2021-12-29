package core;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.components.BorderLayoutPanel;
import org.jetbrains.annotations.NotNull;
import utils.FileHandler;
import utils.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextArea jsonText = new JBTextArea();
    private final JBLabel statusLabel = new JBLabel();
    private final JCheckBox checkBox;

    public AppSettingsComponent() {
        JButton importButton = new JButton("Import");
        importButton.setToolTipText("Import the toggles from a file. " +
                "Clicking this button will open a file picker from which you can open a file. " +
                "Note: the toggles have to be formatted in the correct JSON format. " +
                "See the default toggles for the correct format.");

        JButton exportButton = new JButton("Export");
        exportButton.setToolTipText("Export the currently saved toggles to a JSON file. " +
                "Clicking this button will open a file saver dialog from which you can save the file.");

        JButton resetButton = new JButton("Reset to Defaults");
        resetButton.setToolTipText("Reset the settings to the default settings this plugin ships with. " +
                "Note: the reset is applied and saved instantly.");

        checkBox = new JCheckBox("Enable partial matching");
        checkBox.setToolTipText("Check the checkbox to enable the partial matching functionality. " +
                "This allows Toggler to also search for toggles at the cursor that don't encompass " +
                "the entire word/symbol. E.g. 'add' inside of 'addClass'.");

        resetButton.addActionListener(a -> executeResetButtonAction());
        importButton.addActionListener(a -> importTogglesFromJsonFile());
        exportButton.addActionListener(a -> exportTogglesToJsonFile());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(resetButton);

        BorderLayoutPanel headerPanel = new BorderLayoutPanel();
        headerPanel.add(checkBox, BorderLayout.LINE_START);
        headerPanel.add(buttonPanel, BorderLayout.LINE_END);

        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(statusLabel)
                .addComponent(headerPanel)
                .addComponent(new JSeparator())
                .addVerticalGap(8)
                .addComponentFillVertically(jsonText, 0)
                .getPanel();
    }

    public void executeResetButtonAction() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        appSettingsState.resetSettingsToDefault();
        setJsonText(JsonParser.toJson(appSettingsState.toggles));
        setCheckboxStatus(appSettingsState.isPartialMatchingIsEnabled());
        setStatusMessage("Status: Resetting settings to defaults was successful.");
    }

    private void importTogglesFromJsonFile() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();

        // Load and parse the contents of the JSON file and set the toggles to the loaded toggles.
        try {
            appSettingsState.toggles = JsonParser.parseJsonToToggles(FileHandler.loadContentFromFileToString());
        } catch (JsonParser.TogglesFormatException e) {
            setStatusMessage(String.format("Error: %s", e.getMessage()));
            return;
        } catch (FileHandler.FileSelectionCancelledException e) {
            setStatusMessage("Error: No file was selected, importing toggles failed.");
            return;
        } catch (IOException e) {
            setStatusMessage("Error: Importing toggles from file failed.");
            return;
        }

        // Reset the settings menu JsonText textarea to the toggles that have been loaded.
        setJsonText(JsonParser.toJson(appSettingsState.toggles));
        setStatusMessage("Status: Importing toggles from file was successful.");
    }

    private void exportTogglesToJsonFile() {
        // Save the toggles to a file in JSON format.
        try {
            FileHandler.saveTextToDisk(JsonParser.toJson(AppSettingsState.getInstance().toggles));
        } catch (FileHandler.FileSelectionCancelledException e) {
            setStatusMessage("Error: No file was saved, exporting toggles failed.");
            return;
        } catch (IOException e) {
            setStatusMessage("Error: Exporting toggles to JSON file failed.");
            return;
        }

        setStatusMessage("Status: Exporting toggles to JSON file succeeded.");
    }

    public JPanel getPanel() { return mainPanel; }

    public JComponent getPreferredFocusedComponent() {
        return jsonText;
    }

    @NotNull
    public String getJsonText() { return jsonText.getText(); }

    public void setJsonText(@NotNull String newText) { jsonText.setText(newText); }

    public void setStatusMessage(@NotNull String errorMessage) { statusLabel.setText(errorMessage); }

    public boolean getCheckboxStatus() { return checkBox.isSelected(); }

    public void setCheckboxStatus(boolean partialMatchingIsEnabled) { checkBox.setSelected(partialMatchingIsEnabled); }
}
