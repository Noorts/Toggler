import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import utils.JsonParser;

import javax.swing.*;
import java.util.List;

/* When the settings are changed to something else than the defaultToggles,
 * then the settings are persisted to a file named config/options/SdkSettingsPlugin.xml */
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
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getJsonText().equals(JsonParser.toJson(settings.toggles));
        modified |= mySettingsComponent.getCheckboxStatus() != settings.partialMatchingIsEnabled;
        return modified;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();

        try {
            /* Set whether or not the partial matching functionality is enabled. */
            settings.partialMatchingIsEnabled = mySettingsComponent.getCheckboxStatus();

            List<List<String>> currentSettingsFromMenu = JsonParser.parseJsonToToggles(
                    mySettingsComponent.getJsonText());
            settings.toggles = currentSettingsFromMenu;

            /* Set the JsonTextarea in the settings menu to the toggles saved to the plugin.
             * The side effect is that eventual errors entered by the user that aren't included by the JsonParser
             * are removed from the textarea input as the input is forcefully reset. */
            mySettingsComponent.setJsonText(JsonParser.toJson(currentSettingsFromMenu));
            mySettingsComponent.setStatusMessage("Status: Saving was successful.");
        } catch (JsonParser.TogglesFormatException e) {
            mySettingsComponent.setStatusMessage(String.format("Error: %s", e.getMessage()));
        }
    }

    /* Will reset the fields in the settings menu to the values that are currently loaded. */
    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setJsonText(JsonParser.toJson(settings.toggles));
        mySettingsComponent.setCheckboxStatus(settings.partialMatchingIsEnabled);
        mySettingsComponent.setStatusMessage("Status: Loaded previous settings.");
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
