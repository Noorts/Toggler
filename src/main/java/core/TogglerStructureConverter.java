package core;

import com.intellij.notification.NotificationType;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import utils.ConfigParser;
import utils.NotificationHandler;

/**
 * A converter used by the AppSettingsState to write and load the state of the
 * toggles to and from the internal settings file of this plugin managed by the
 * IDE.
 */
class TogglerStructureConverter extends Converter<TogglesConfig> {
    public TogglesConfig fromString(@NotNull String togglesString) {
        try {
            return new TogglesConfig(togglesString);
        } catch (ConfigParser.TogglesFormatException e) {
            NotificationHandler.notify("The toggles couldn't be parsed from the " +
                    "plugin settings storage successfully.",
                NotificationType.ERROR);
            return null;
        }
    }

    public String toString(@NotNull TogglesConfig togglesConfig) {
        return togglesConfig.toString();
    }
}
