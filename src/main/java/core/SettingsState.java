package core;

import com.intellij.util.xmlb.annotations.OptionTag;

public class SettingsState {
    private static final boolean DEFAULT_PARTIAL_MATCHING_STATUS = true;

    @OptionTag(converter = TogglerStructureConverter.class)
    public TogglesConfig toggles;
    @OptionTag
    private boolean partialMatchingIsEnabled;

    public SettingsState() {
        toggles = new TogglesConfig();
        this.resetSettingsToDefault();
    }

    public void resetSettingsToDefault() {
        toggles.resetTogglesToDefault();
        partialMatchingIsEnabled = DEFAULT_PARTIAL_MATCHING_STATUS;
    }

    public boolean isPartialMatchingEnabled() {
        return partialMatchingIsEnabled;
    }

    public void setPartialMatchingIsEnabled(boolean partialMatchingIsEnabled) {
        this.partialMatchingIsEnabled = partialMatchingIsEnabled;
    }
}
