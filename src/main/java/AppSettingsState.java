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
                    "[" +
                        "[`public`,`private`,`protected`]," +
                        "[`class`,`interface`]," +
                        "[`extends`,`implements`]," +
                        "[`import`,`export`]," +
                        "[`byte`,`short`,`int`,`long`,`float`,`double`]," +
                        "[`String`,`Character`]," +

                        "[`get`,`set`]," +
                        "[`add`,`remove`]," +
                        "[`min`,`max`]," +
                        "[`pop`,`push`]," +

                        "[`true`,`false`]," +
                        "[`yes`,`no`]," +
                        "[`on`,`off`]," +
                        "[`0`,`1`]," +
                        "[`x`,`y`]," +
                        "[`enable`,`disable`]," +
                        "[`enabled`,`disabled`]," +
                        "[`open`,`close`]," +

                        "[`up`,`down`]," +
                        "[`left`,`right`]," +
                        "[`top`,`bottom`]," +
                        "[`start`,`end`]," +
                        "[`first`,`last`]," +
                        "[`before`,`after`]," +
                        "[`ceil`,`floor`]," +
                        "[`read`,`write`]," +
                        "[`show`,`hide`]," +
                        "[`input`,`output`]," +
                        "[`dev`,`prod`]," +
                        "[`development`,`production`]," +
                        "[`row`,`column`]," +
                        "[`req`,`res`]," +

                        "[`&&`,`||`]," +
                        "[`&`,`|`]," +
                        "[`<`,`>`]," +
                        "[`+`,`-`]," +
                        "[`*`,`/`]," +
                        "[`++`,`--`]," +
                        "[`+=`,`-=`]," +
                        "[`*=`,`/=`]," +
                        "[`&=`,`|=`]," +
                        "[`<<=`,`>>=`]," +
                        "[`<=`,`>=`]," +
                        "[`==`,`!=`]," +
                        "[`===`,`!==`]," +
                    "]"
            ).replace('`', '"');

    AppSettingsState() { resetTogglesToDefault(); }

    @OptionTag(converter = TogglerStructureConverter.class)
    public List<List<String>> toggles;

    public void resetTogglesToDefault(){
        try {
            toggles = JsonParser.parseJsonToToggles(defaultToggles);
        } catch (JsonParser.TogglesFormatException e) {
            System.err.println("The defaultToggles provided by the creator of the " +
                    "plugin don't conform to the JSON format.");
        }
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
