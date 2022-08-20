package core;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.JsonParser;
import utils.NotificationHandler;

import java.util.List;

@State(
        name = "TogglerSettingsState",
        storages = {@Storage("togglerPluginSettings.xml")}
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {
    private static final boolean DEFAULT_PARTIAL_MATCHING_STATUS = true;

    AppSettingsState() { resetSettingsToDefault(); }

    @OptionTag(converter = TogglerStructureConverter.class)
    public List<List<String>> toggles;
    private boolean partialMatchingIsEnabled;

    public void resetSettingsToDefault() {
        try {
            toggles = JsonParser.parseJsonToToggles(Config.DEFAULT_TOGGLES);
            partialMatchingIsEnabled = DEFAULT_PARTIAL_MATCHING_STATUS;
        } catch (JsonParser.TogglesFormatException e) {
            NotificationHandler.notify("The defaultToggles provided by the creator of the " +
                            "plugin don't conform to the JSON format.",
                    NotificationType.ERROR);
        }
    }

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isPartialMatchingIsEnabled() {
        return partialMatchingIsEnabled;
    }

    public void setPartialMatchingIsEnabled(boolean partialMatchingIsEnabled) {
        this.partialMatchingIsEnabled = partialMatchingIsEnabled;
    }
}
