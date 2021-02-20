import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import utils.JsonParser;

import java.util.List;

class TogglerStructureConverter extends Converter<List<List<String>>> {
    public List<List<String>> fromString(@NotNull String value) {
        try {
            return JsonParser.parseJsonToToggles(value);
        } catch (JsonParser.TogglesFormatException e) {
            System.err.println("The toggles couldn't be parsed from the plugin setting storage successfully.");
            return null;
        }
    }

    public String toString(@NotNull List<List<String>> value) {
        return JsonParser.toJson(value);
    }
}
