package core;

import com.intellij.notification.NotificationType;
import com.intellij.util.xmlb.annotations.OptionTag;
import utils.JsonParser;
import utils.NotificationHandler;

import java.util.List;

public class SettingsState {
    private static final boolean DEFAULT_PARTIAL_MATCHING_STATUS = true;

    @OptionTag(converter = TogglerStructureConverter.class)
    public List<List<String>> toggles;
    @OptionTag
    private boolean partialMatchingIsEnabled;

    public SettingsState() {
        this.resetSettingsToDefault();
    }

    public void resetSettingsToDefault() {
        try {
            toggles = JsonParser.parseJsonToToggles(Constants.DEFAULT_TOGGLES);
            partialMatchingIsEnabled = DEFAULT_PARTIAL_MATCHING_STATUS;
        } catch (JsonParser.TogglesFormatException e) {
            NotificationHandler.notify("The defaultToggles provided by the creator of the " +
                    "plugin don't conform to the JSON format.",
                NotificationType.ERROR);
        }
    }

    public boolean isPartialMatchingEnabled() {
        return partialMatchingIsEnabled;
    }

    public void setPartialMatchingIsEnabled(boolean partialMatchingIsEnabled) {
        this.partialMatchingIsEnabled = partialMatchingIsEnabled;
    }
}
