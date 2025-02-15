package core;

import org.junit.Test;
import utils.ConfigParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ToggleActionTest {
    @Test
    public void regexPatternIsCorrectlyCreatedFromTheTogglePairs() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"], [\"addClass\", \"removeClass\"]]";

        // Act
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        // Assert
        String correctRegexPattern = "(\\QremoveClass\\E|\\QaddClass\\E|\\Qremove\\E|\\Qadd\\E)";
        assertEquals(correctRegexPattern, regexPattern, "The regex pattern that was created is incorrect.");
    }

    @Test
    public void fullMatchIsFoundCorrectlyWithPartialMatchingEnabled() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"], [\"addClass\", \"removeClass\"]]";
        ToggleAction newToggleAction = new ToggleAction();
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        String input = "addClass";

        // Act
        List<Integer> resultPositions = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, true, -1);

        // Assert
        List<Integer> correctPositions = new ArrayList<>(Arrays.asList(0, 8));
        assertEquals(correctPositions, resultPositions,
                "The positions returned by findToggle for the full match are incorrect.");
    }

    @Test
    public void partialMatchIsFoundCorrectlyWithPartialMatchingEnabled() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"]]";
        ToggleAction newToggleAction = new ToggleAction();
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        String input = "addClass";

        // Act
        List<Integer> resultPositions = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, true, 1);

        // Assert
        List<Integer> correctPositions = new ArrayList<>(Arrays.asList(0, 3));
        assertEquals(correctPositions, resultPositions,
                "The positions returned by findToggle for the partial match are incorrect.");
    }

    @Test
    public void fullMatchIsFoundCorrectlyWithPartialMatchingDisabled() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"], [\"addClass\", \"removeClass\"]]";
        ToggleAction newToggleAction = new ToggleAction();
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        String input = "addClass";

        // Act
        List<Integer> resultPositions = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, false, -1);

        // Assert
        List<Integer> correctPositions = new ArrayList<>(Arrays.asList(0, 8));
        assertEquals(correctPositions, resultPositions,
                "The positions returned by findToggle for the full match are incorrect.");
    }

    @Test
    public void partialMatchIsNotFoundCorrectlyWithPartialMatchingDisabled() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"]]";
        ToggleAction newToggleAction = new ToggleAction();
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        String input = "addClass";

        // Act
        List<Integer> resultPositions = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, false, -1);

        // Assert
        assertTrue(resultPositions.isEmpty(),
                "The positions returned by findToggle for the partial match are incorrect.");
    }

    @Test
    public void partialMatchUnderCaretIsFoundCorrectly() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"], [\"class\", \"interface\"]]";
        ToggleAction newToggleAction = new ToggleAction();
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        String input = "addClass";

        // Act
        List<Integer> resultPositionsAdd = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, true, 1);
        List<Integer> resultPositionsClass = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, true, 5);

        // Assert
        List<Integer> correctPositionsAdd = new ArrayList<>(Arrays.asList(0, 3));
        assertEquals(correctPositionsAdd, resultPositionsAdd,
                "The positions returned by findToggle for the partial " +
                        "match with the caret inside of 'add' are incorrect.");
        List<Integer> correctPositionsClass = new ArrayList<>(Arrays.asList(3, 8));
        assertEquals(correctPositionsClass, resultPositionsClass,
                "The positions returned by findToggle for the partial " +
                        "match with the caret inside of 'Class' are incorrect.");
    }

    @Test
    public void partialMatchIsNotFoundCorrectlyWhenCaretPositionIsOutsideOfIt() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"]]";
        ToggleAction newToggleAction = new ToggleAction();
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        String input = "addClass";

        // Act
        List<Integer> resultPositionsWrongWord = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, true, 5);
        List<Integer> resultPositionsOutOfBounds = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, true, -1);

        // Assert
        assertTrue(resultPositionsWrongWord.isEmpty(),
                "The positions returned by findToggle for the partial match " +
                        "whilst the caret position is in the wrong word are incorrect.");
        assertTrue(resultPositionsOutOfBounds.isEmpty(),
                "The positions returned by findToggle for the partial match " +
                        "whilst the caret position is out of bounds are incorrect.");
    }

    @Test
    public void ifThereArePartialMatchesOnEitherSideOfTheCaretThenTheOneOnTheRightIsCorrectlyReturned() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"lov\", \"add\"], [\"ely\", \"remove\"]]";
        ToggleAction newToggleAction = new ToggleAction();
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        String regexPattern = togglesConfig.getRegexPatternOfToggles();

        String input = "Lovely";

        // Act
        List<Integer> resultPositions = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, true, 4);

        // Assert
        List<Integer> correctPositions = new ArrayList<>(Arrays.asList(3, 6));
        assertEquals(correctPositions, resultPositions,
                "The findToggle method incorrectly returned the partial " +
                        "on the left instead of the one on the right.");
    }

    // Fix the current format used to store the toggles persistently. This is here to catch breaking changes.
    @Test
    public void togglesConfigurationPersistentStorageFormatHasNotDeviatedFromOriginal() throws ConfigParser.TogglesFormatException {
        // Arrange
        String toggles = "[[\"add\", \"remove\"], [\"addClass\", \"removeClass\"]]";
        TogglesConfig togglesConfig = new TogglesConfig(toggles);
        TogglerStructureConverter converter = new TogglerStructureConverter();

        // Act
        String persistentStoredToggles = converter.toString(togglesConfig);

        // Assert
        String expectedTogglesFormat = """
            [
            \t["add", "remove"],
            \t["addClass", "removeClass"]
            ]""";
        assertEquals(expectedTogglesFormat, persistentStoredToggles,
            "The toggles config format deviates from the expected formatting.");
    }

}
