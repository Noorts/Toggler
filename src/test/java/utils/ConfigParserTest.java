package utils;

import core.Constants;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ConfigParserTest {
    private static final String BASIC_TOGGLES =
        """
            [
            \t["public", "private", "protected"],
            \t["class", "interface"],
            \t["&&", "||"]
            ]""";

    private static final List<List<String>> BASIC_TOGGLES_LIST = List.of(
        List.of("public", "private", "protected"),
        List.of("class", "interface"),
        List.of("&&", "||")
    );

    @Test
    public void convertTogglesToString() {
        assertDoesNotThrow(() -> {
            String toggles = ConfigParser.toJson(BASIC_TOGGLES_LIST);
            assertEquals(BASIC_TOGGLES, toggles);
        });
    }

    @Test
    public void parseTogglesWithTabs() {
        assertDoesNotThrow(() -> {
            List<List<String>> parsedToggles = ConfigParser.parseJsonToToggles(BASIC_TOGGLES);
            assertEquals(BASIC_TOGGLES_LIST, parsedToggles);
        });
    }

    @Test
    public void parseTogglesWithSpaces() {
        assertDoesNotThrow(() -> {
            String togglesWithSpaces = BASIC_TOGGLES.replace("\t", "    ");

            List<List<String>> parsedToggles = ConfigParser.parseJsonToToggles(togglesWithSpaces);
            assertEquals(BASIC_TOGGLES_LIST, parsedToggles);
        });
    }

    @Test
    public void parseTogglesCollapsedOnOneLine() {
        assertDoesNotThrow(() -> {
            String togglesOnOneLine = BASIC_TOGGLES.replaceAll("[\\t\\n\\s]", "");

            List<List<String>> parsedToggles = ConfigParser.parseJsonToToggles(togglesOnOneLine);
            assertEquals(BASIC_TOGGLES_LIST, parsedToggles);
        });
    }

    @Test
    public void throwOnDuplicateWordInSameToggle() {
        assertThrows(ConfigParser.TogglesFormatException.class, () -> {
            String togglesWithDuplicate = """
                [
                \t["duplicate", "private", "duplicate"],
                \t["class", "interface"]
                ]""";

            ConfigParser.parseJsonToToggles(togglesWithDuplicate);
        });
    }

    @Test
    public void throwOnDuplicateWordInDifferentToggles() {
        assertThrows(ConfigParser.TogglesFormatException.class, () -> {
            String togglesWithDuplicate = """
                [
                \t["duplicate", "private", "protected"],
                \t["class", "duplicate"]
                ]""";

            ConfigParser.parseJsonToToggles(togglesWithDuplicate);
        });
    }

    @Test
    public void throwIfWordContainsBoundaryCharacter() {
        assertThrows(ConfigParser.TogglesFormatException.class, () -> {
            char boundaryCharacter = ';';
            String wordWithBoundaryCharacter = "public" + boundaryCharacter;
            String togglesWithDuplicate = """
                [
                \t["%s", "private", "protected"],
                \t["class", "interface"]
                ]""".formatted(wordWithBoundaryCharacter);

            assertTrue(Constants.BOUNDARY_CHARS.contains(boundaryCharacter));
            ConfigParser.parseJsonToToggles(togglesWithDuplicate);
        });
    }
}
