import core.ToggleAction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ToggleActionTest {
    @Test
    public void regexPatternIsCorrectlyCreatedFromTheTogglePairs() {
        // Arrange
        List<String> smallTogglePair = new ArrayList<>(Arrays.asList("add", "remove"));
        List<String> longTogglePair = new ArrayList<>(Arrays.asList("addClass", "removeClass"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Arrays.asList(smallTogglePair, longTogglePair));

        // Act
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

        // Assert
        String correctRegexPattern = "(\\QremoveClass\\E|\\QaddClass\\E|\\Qremove\\E|\\Qadd\\E)";
        assertEquals(correctRegexPattern, regexPattern, "The regex pattern that was created is incorrect.");
    }

    @Test
    public void fullMatchIsFoundCorrectlyWithPartialMatchingEnabled() {
        // Arrange
        List<String> smallTogglePair = new ArrayList<>(Arrays.asList("add", "remove"));
        List<String> longTogglePair = new ArrayList<>(Arrays.asList("addClass", "removeClass"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Arrays.asList(smallTogglePair, longTogglePair));
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

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
    public void partialMatchIsFoundCorrectlyWithPartialMatchingEnabled() {
        // Arrange
        List<String> smallTogglePair = new ArrayList<>(Arrays.asList("add", "remove"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Collections.singletonList(smallTogglePair));
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

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
    public void fullMatchIsFoundCorrectlyWithPartialMatchingDisabled() {
        // Arrange
        List<String> smallTogglePair = new ArrayList<>(Arrays.asList("add", "remove"));
        List<String> longTogglePair = new ArrayList<>(Arrays.asList("addClass", "removeClass"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Arrays.asList(smallTogglePair, longTogglePair));
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

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
    public void partialMatchIsNotFoundCorrectlyWithPartialMatchingDisabled() {
        // Arrange
        List<String> smallTogglePair = new ArrayList<>(Arrays.asList("add", "remove"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Collections.singletonList(smallTogglePair));
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

        String input = "addClass";

        // Act
        List<Integer> resultPositions = newToggleAction.getPositionOfToggleMatch(
                regexPattern, input, false, -1);

        // Assert
        assertTrue(resultPositions.isEmpty(),
                "The positions returned by findToggle for the partial match are incorrect.");
    }

    @Test
    public void partialMatchUnderCaretIsFoundCorrectly() {
        // Arrange
        List<String> firstTogglePair = new ArrayList<>(Arrays.asList("add", "remove"));
        List<String> secondTogglePair = new ArrayList<>(Arrays.asList("class", "interface"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Arrays.asList(firstTogglePair, secondTogglePair));
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

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
    public void partialMatchIsNotFoundCorrectlyWhenCaretPositionIsOutsideOfIt() {
        // Arrange
        List<String> firstTogglePair = new ArrayList<>(Arrays.asList("add", "remove"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Collections.singletonList(firstTogglePair));
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

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
    public void ifThereArePartialMatchesOnEitherSideOfTheCaretThenTheOneOnTheRightIsCorrectlyReturned() {
        // Arrange
        List<String> smallTogglePair = new ArrayList<>(Arrays.asList("lov", "add"));
        List<String> longTogglePair = new ArrayList<>(Arrays.asList("ely", "remove"));
        List<List<String>> toggleActionStructure = new ArrayList<>(Arrays.asList(smallTogglePair, longTogglePair));
        ToggleAction newToggleAction = new ToggleAction();
        String regexPattern = newToggleAction.createRegexPatternOfToggles(toggleActionStructure);

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

}
