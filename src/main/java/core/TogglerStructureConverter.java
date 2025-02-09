package core;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;

/**
 * A converter used by the AppSettingsState to write and load the state of the
 * toggles to and from the internal settings file of this plugin managed by the
 * IDE.
 */
class TogglerStructureConverter extends Converter<TogglesConfig> {
    public TogglesConfig fromString(@NotNull String togglesString) {
        return TogglesConfig.deserialize(togglesString);
    }

    public String toString(@NotNull TogglesConfig togglesConfig) {
        return togglesConfig.serialize();
    }
}
