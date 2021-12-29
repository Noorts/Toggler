package core;

import com.intellij.notification.NotificationType;
import com.intellij.util.xmlb.Converter;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import utils.JsonParser;
import utils.NotificationHandler;

import java.util.List;

/**
 * A converter used by the AppSettingsState to write and load the state of the toggles to and from the
 * internal settings file of this plugin managed by the IDE.
 */
class TogglerStructureConverter extends Converter<List<List<String>>> {
    public List<List<String>> fromString(@NotNull String value) {
        try {
            return JsonParser.parseJsonToToggles(value);
        } catch (JsonParser.TogglesFormatException e) {
            NotificationHandler.notify("The toggles couldn't be parsed from the " +
                            "plugin setting storage successfully.",
                    NotificationType.ERROR);
            return Collections.emptyList();
        }
    }

    public String toString(@NotNull List<List<String>> value) {
        return JsonParser.toJson(value);
    }
}
