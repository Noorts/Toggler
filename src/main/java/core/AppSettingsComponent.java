package core;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.components.BorderLayoutPanel;
import org.jetbrains.annotations.NotNull;
import utils.ConfirmResetDialogWrapper;
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
        importButton.setToolTipText("Import toggles from a file. " +
            "Warning: current toggles are overwritten. " +
            "The toggles have to be formatted correctly " +
            "(see the default toggles for an example).");

        JButton exportButton = new JButton("Export");
        exportButton.setToolTipText(
            "Export the currently saved toggles to a file.");

        JButton resetButton = new JButton("Reset to Defaults");
        resetButton.setToolTipText("Reset the settings to the default " +
            "settings this plugin ships with. The reset is applied and saved " +
            "instantly.");

        checkBox = new JCheckBox("Enable partial matching");
        checkBox.setToolTipText("Partial matching allows Toggler to search for " +
            "toggles at the cursor that do not encompass the entire word/symbol. " +
            "For example, when the cursor is placed somewhere on 'add', then it " +
            "allows toggling 'add' inside 'addClass'.");

        resetButton.addActionListener(a -> promptResetButtonAction());
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

    public void promptResetButtonAction() {
        if (new ConfirmResetDialogWrapper().showAndGet()) {
            executeResetButtonAction();
            setStatusMessage("Resetting settings to defaults was successful.");
        } else {
            setStatusMessage("Resetting settings to defaults was cancelled.");
        }
    }

    public void executeResetButtonAction() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        appSettingsState.resetSettingsToDefault();
        setJsonText(JsonParser.toJson(appSettingsState.toggles));
        setCheckboxStatus(appSettingsState.isPartialMatchingIsEnabled());
    }

    private void importTogglesFromJsonFile() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();

        // Load and parse the contents of the JSON file and set the toggles to
        // the loaded toggles.
        try {
            appSettingsState.toggles = JsonParser.parseJsonToToggles(FileHandler.loadContentFromFileToString());
        } catch (JsonParser.TogglesFormatException e) {
            setStatusErrorMessage(e.getMessage());
            return;
        } catch (FileHandler.FileSelectionCancelledException e) {
            setStatusErrorMessage("No file was selected, importing toggles failed.");
            return;
        } catch (IOException e) {
            setStatusErrorMessage("Importing toggles from file failed.");
            return;
        }

        // Reset the settings menu JsonText textarea to the toggles that have
        // been loaded.
        setJsonText(JsonParser.toJson(appSettingsState.toggles));
        setStatusMessage("Importing toggles from file was successful.");
    }

    private void exportTogglesToJsonFile() {
        // Save the toggles to a file in JSON format.
        try {
            FileHandler.saveTextToDisk(JsonParser.toJson(AppSettingsState.getInstance().toggles));
        } catch (FileHandler.FileSelectionCancelledException e) {
            setStatusErrorMessage("No file was saved, exporting toggles failed.");
            return;
        } catch (IOException e) {
            setStatusErrorMessage("Exporting toggles to JSON file failed.");
            return;
        }

        setStatusMessage("Exporting toggles to JSON file succeeded.");
    }

    public JPanel getPanel() { return mainPanel; }

    public JComponent getPreferredFocusedComponent() {
        return jsonText;
    }

    @NotNull
    public String getJsonText() { return jsonText.getText(); }

    public void setJsonText(@NotNull String newText) { jsonText.setText(newText); }

    private void setStatusLabel(@NotNull String message) { statusLabel.setText(message); }

    public void setStatusMessage(@NotNull String statusMessage) { setStatusLabel("Status: " + statusMessage); }

    public void setStatusErrorMessage(@NotNull String errorMessage) { setStatusLabel("Error: " + errorMessage); }

    public boolean getCheckboxStatus() { return checkBox.isSelected(); }

    public void setCheckboxStatus(boolean partialMatchingIsEnabled) { checkBox.setSelected(partialMatchingIsEnabled); }
}
