package core;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import utils.JsonParser;

import javax.swing.*;
import java.util.List;

// Docs:
// When the settings are changed to something else than the defaultToggles, then
// the settings are persisted to a file named
// config/options/SdkSettingsPlugin.xml.

public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Toggler: Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        SettingsState settings = AppSettingsState.getInstance().getState();
        boolean modified = !mySettingsComponent.getJsonText().equals(JsonParser.toJson(settings.toggles));
        modified |= mySettingsComponent.getPartialMatchingCheckboxStatus() != settings.isPartialMatchingEnabled();
        return modified;
    }

    @Override
    public void apply() {
        SettingsState settings = AppSettingsState.getInstance().getState();

        try {
            /* Set whether the partial matching functionality is enabled. */
            settings.setPartialMatchingIsEnabled(mySettingsComponent.getPartialMatchingCheckboxStatus());

            List<List<String>> currentSettingsFromMenu = JsonParser.parseJsonToToggles(
                    mySettingsComponent.getJsonText());
            settings.toggles = currentSettingsFromMenu;

            /* Set the JsonTextarea in the settings menu to the toggles saved to
             * the plugin. The side effect is that eventual errors entered by
             * the user that aren't included by the JsonParser are removed from
             * the textarea input as the input is forcefully reset. */
            mySettingsComponent.setJsonText(JsonParser.toJson(currentSettingsFromMenu));
            mySettingsComponent.setStatusMessage("Saving was successful.");
        } catch (JsonParser.TogglesFormatException e) {
            mySettingsComponent.setStatusErrorMessage(e.getMessage());
        }
    }

    /* Reset the fields in the settings menu to the values that are currently
     * loaded. */
    @Override
    public void reset() {
        SettingsState settings = AppSettingsState.getInstance().getState();
        mySettingsComponent.setJsonText(JsonParser.toJson(settings.toggles));
        mySettingsComponent.setPartialMatchingCheckboxStatus(settings.isPartialMatchingEnabled());
        mySettingsComponent.setStatusMessage("Loaded previous settings.");
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
