import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import utils.JsonParser;

import javax.swing.*;
import java.util.List;

/* When the settings are changed to something else than the defaultToggles, then the settings are persisted to a file named config/options/SdkSettingsPlugin.xml */
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
        boolean modified = !mySettingsComponent.getJsonText().equals(JsonParser.toJson(settings.toggleWords));
//        modified |= mySettingsComponent.getIdeaUserStatus() != settings.ideaStatus;
        return modified;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        List<List<String>> currentSettingsFromMenu = JsonParser.parseJson(mySettingsComponent.getJsonText());
        if (currentSettingsFromMenu != null) {
            settings.toggleWords = currentSettingsFromMenu;
            mySettingsComponent.setErrorMessage("Status: saving JSON was successful.");
        } else {
            mySettingsComponent.setErrorMessage("Error: saving JSON failed.");
        }
    }

    /* Will reset the fields in the settings menu to the values that are currently loaded. */
    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setJsonText(JsonParser.toJson(settings.toggleWords));
        mySettingsComponent.setErrorMessage("Status: previous values were loaded.");
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
