package core;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

@Service
@State(
    name = "TogglerSettingsState",
    storages = {@Storage("togglerPluginSettings.xml")}
)
public final class AppSettings implements PersistentStateComponent<SettingsState> {
    AppSettings() {
        settingsState = new SettingsState();
    }

    private SettingsState settingsState;

    public static AppSettings getInstance() {
        return ApplicationManager.getApplication().getService(AppSettings.class);
    }

    @Override
    @NotNull
    public SettingsState getState() {
        return settingsState;
    }

    @Override
    public void loadState(@NotNull SettingsState config) {
        settingsState = config;
    }
}
