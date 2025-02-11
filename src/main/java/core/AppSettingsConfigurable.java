package core;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import utils.ConfigParser;

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
        SettingsState settings = AppSettings.getInstance().getState();
        boolean modified = !mySettingsComponent.getJsonText().equals(settings.toggles.toString());
        modified |= mySettingsComponent.getPartialMatchingCheckboxStatus() != settings.isPartialMatchingEnabled();
        return modified;
    }

    @Override
    public void apply() {
        SettingsState settings = AppSettings.getInstance().getState();

        try {
            /* Set whether the partial matching functionality is enabled. */
            settings.setPartialMatchingIsEnabled(mySettingsComponent.getPartialMatchingCheckboxStatus());

            settings.toggles.overwriteToggles(mySettingsComponent.getJsonText());

            /* Set the JsonTextarea in the settings menu to the toggles saved to
             * the plugin. The side effect is that eventual errors entered by
             * the user that aren't included by the JsonParser are removed from
             * the textarea input as the input is forcefully reset. */
            mySettingsComponent.setJsonText(settings.toggles.toString());
            mySettingsComponent.setStatusMessage("Saving was successful.");
        } catch (ConfigParser.TogglesFormatException e) {
            mySettingsComponent.setStatusErrorMessage(e.getMessage());
        }
    }

    /* Reset the fields in the settings menu to the values that are currently
     * loaded. */
    @Override
    public void reset() {
        SettingsState settings = AppSettings.getInstance().getState();
        mySettingsComponent.setJsonText(settings.toggles.toString());
        mySettingsComponent.setPartialMatchingCheckboxStatus(settings.isPartialMatchingEnabled());
        mySettingsComponent.setStatusMessage("Loaded previous settings.");
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
