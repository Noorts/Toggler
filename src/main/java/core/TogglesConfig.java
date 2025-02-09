package core;

import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;
import utils.JsonParser;
import utils.NotificationHandler;

import java.util.List;

public class TogglesConfig {
    private List<List<String>> toggles;

    public TogglesConfig() {
        resetTogglesToDefault();
    }

    public void resetTogglesToDefault() {
        try {
            toggles = JsonParser.parseJsonToToggles(Constants.DEFAULT_TOGGLES);
        } catch (JsonParser.TogglesFormatException e) {
            NotificationHandler.notify("The defaultToggles provided by the creator of the " +
                    "plugin don't conform to the JSON format.",
                NotificationType.ERROR);
        }
    }

    public static TogglesConfig deserialize(@NotNull String togglesString) {
        TogglesConfig config = new TogglesConfig();
        try {
            config.toggles = JsonParser.parseJsonToToggles(togglesString);
            return config;
        } catch (JsonParser.TogglesFormatException e) {
            NotificationHandler.notify("The toggles couldn't be parsed from the " +
                    "plugin setting storage successfully.",
                NotificationType.ERROR);
            return null;
        }
    }

    public String serialize() {
        return JsonParser.toJson(this.toggles);
    }

    public List<List<String>> getToggles() {
        return toggles;
    }

    public void setToggles(List<List<String>> toggles) {
        this.toggles = toggles;
    }
}
