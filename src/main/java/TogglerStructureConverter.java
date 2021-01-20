import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import utils.JsonParser;

import java.util.List;

class TogglerStructureConverter extends Converter<List<List<String>>> {
    public List<List<String>> fromString(@NotNull String value) {
        return JsonParser.parseJson(value);
    }

    public String toString(@NotNull List<List<String>> value) {
        return JsonParser.toJson(value);
    }
}
