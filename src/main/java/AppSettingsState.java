import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.JsonParser;

import java.util.List;

@State(
        name = "AppSettingsState",
        storages = {@Storage("TogglerSettingsPlugin.xml")}
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    private static final String defaultToggles =
            (
                    "[[`true`,`false`],[`one`,`two`,`three`]," +
                    "[`public`,`private`,`protected`]," +
                    "[`class`,`interface`]]"
            ).replace('`', '"');

    AppSettingsState() { resetTogglesToDefault(); }

    @OptionTag(converter = TogglerStructureConverter.class)
    public List<List<String>> toggles;

    public void resetTogglesToDefault(){
        toggles = JsonParser.parseJson(defaultToggles);
    }

    public static AppSettingsState getInstance() {
        return ServiceManager.getService(AppSettingsState.class);
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
}
