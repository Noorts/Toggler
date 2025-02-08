package ui;

import com.automation.remarks.junit5.Video;
import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.TextEditorFixture;
import com.intellij.remoterobot.utils.Keyboard;
import org.assertj.swing.core.MouseButton;
import org.junit.jupiter.api.BeforeAll;
import ui.pages.IdeaFrameFixture;
import ui.pages.NotificationFixture;
import ui.pages.SettingsFrameFixture;
import ui.steps.CommonSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.utils.StepsLogger;

import java.awt.event.KeyEvent;
import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitForIgnoringError;
import static java.time.Duration.ofSeconds;
import static org.assertj.swing.timing.Pause.pause;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ui.utils.KeyboardUtils.enterKeycodeSequence;
import static ui.utils.KeyboardUtils.repeatKeycodeNTimes;

public class ToggleUITest {
    private final RemoteRobot remoteRobot = new RemoteRobot("http://127.0.0.1:8082");
    private final Keyboard keyboard = new Keyboard(remoteRobot);
    private final CommonSteps commonSteps = new CommonSteps(remoteRobot);

    @BeforeAll
    public static void initLogging() {
        StepsLogger.init();
    }

    @BeforeEach
    public void testInit() {
        waitForIgnoringError(Duration.ofMinutes(3), ofSeconds(5),
            "Wait till IDE is started", "IDE is not started",
            () -> remoteRobot.callJs("true"));

        // TODO: Only reset if settings have been modified.
        step("Reset Toggler settings", commonSteps::resetTogglerSettings);

        step("Create new project", commonSteps::createNewProject);

        step("Wait till IDE pane is loaded", () -> {
            final IdeaFrameFixture idea =
                remoteRobot.find(IdeaFrameFixture.class, ofSeconds(10));
            // While waiting the IDE performs tasks such as indexing.
            waitFor(Duration.ofMinutes(5), () -> !idea.isDumbMode());
        });

        // E.g., close "Download pre-built shared binaries" notification.
        step("Close active notifications", commonSteps::closeNotifications);
    }

    @AfterEach
    public void testCleanup() {
        // TODO: Handle cleaning up the projects? Delete the project afterwards?
        step("Close the project", commonSteps::closeProject);
    }

    @Test
    @Video
    void testToggleAction() {
        final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class);
        final TextEditorFixture editor = idea.textEditor(ofSeconds(5));

        step("Set editor text to True", () -> {
            // This overwrites the current editor's content.
            editor.getEditor().setText("True");
        });

        step("Toggle and verify that True was toggled to False", () -> {
            editor.getEditor().findText("True").click();
            commonSteps.triggerToggleAction();
            assertEquals("False", editor.getEditor().getText());
        });

        step("Toggle and verify that False was toggled back to True", () -> {
            commonSteps.triggerToggleAction();
            assertEquals("True", editor.getEditor().getText());
        });
    }

    @Test
    @Video
    void testNotificationOnNoMatchToggle() {
        final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class);
        final TextEditorFixture editor = idea.textEditor(ofSeconds(5));

        step("Toggle 'void' and verify that " +
            "a 'no match' notification was displayed", () -> {
            editor.getEditor().findText("void").click();
            commonSteps.triggerToggleAction();
            assertTrue(commonSteps.getNotification()
                .getDescriptionText().contains("No match"));
        });
    }

    @Test
    @Video
    void testNotificationOnEmptyFileToggle() {
        final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class);
        final TextEditorFixture editor = idea.textEditor(ofSeconds(5));

        step("Toggle in an empty file and verify that " +
            "a 'no text could be selected' notification was displayed", () -> {
            editor.getEditor().setText("");
            editor.getEditor().clickOnOffset(0, MouseButton.LEFT_BUTTON, 1);
            pause(ofSeconds(1).toMillis());
            commonSteps.triggerToggleAction();
            NotificationFixture notification = commonSteps.getNotification();
            assertTrue(notification != null &&
                notification.getDescriptionText()
                    .contains("No text could be selected"));
        });
    }

    @Test
    @Video
    void testPartialMatching() {
        final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class);
        final TextEditorFixture editor = idea.textEditor(ofSeconds(5));

        step("Verify that partial matching is on", () -> {
            SettingsFrameFixture settingsFrame =
                commonSteps.openTogglerSettings();
            assertTrue(settingsFrame.partialMatchingCheckbox().isSelected());
            settingsFrame.okButton().click();
        });

        step("Set editor text to 'addClass'", () -> {
            // This overwrites the current editor's content.
            editor.getEditor().setText("addClass");
            pause(ofSeconds(1).toMillis());
        });

        step("PM ON: Toggle and verify toggle from 'addClass' to 'removeClass'",
            () -> {
                editor.getEditor().findText("addClass").click();
                commonSteps.triggerToggleAction();
                assertEquals("removeClass", editor.getEditor().getText());
                commonSteps.triggerToggleAction();
                assertEquals("addClass", editor.getEditor().getText());
            });

        step("Turn off partial matching", () -> {
            SettingsFrameFixture settingsFrame =
                commonSteps.openTogglerSettings();
            settingsFrame.partialMatchingCheckbox().setValue(false);
            settingsFrame.okButton().click();
        });

        step("PM OFF: Invalid toggle triggers notification", () -> {
            editor.getEditor().findText("addClass").click();
            commonSteps.triggerToggleAction();

            NotificationFixture notification = commonSteps.getNotification();
            assertTrue(notification != null &&
                notification.getDescriptionText().contains("No match"));
        });
    }

    @Test
    @Video
    void testTogglesConfiguration() {
        final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class);
        final TextEditorFixture editor = idea.textEditor(ofSeconds(5));

        final String newCombination = "[\"Foo\", \"Bar\"]";

        // Set editor text to "Foo"
        step("Set editor text to 'Foo'", () -> {
            // This overwrites the current editor's content.
            editor.getEditor().setText("Foo");
            pause(ofSeconds(1).toMillis());
        });

        // Toggle and confirm that notification shows up
        step("PM OFF: Invalid toggle triggers notification", () -> {
            editor.getEditor().findText("Foo").click();
            commonSteps.triggerToggleAction();

            NotificationFixture notification = commonSteps.getNotification();
            assertTrue(notification != null &&
                notification.getDescriptionText().contains("No match"));
        });

        step("Add [\"Foo\", \"Bar\"] toggle to configured toggles", () -> {
            SettingsFrameFixture settingsFrame =
                commonSteps.openTogglerSettings();
            settingsFrame.togglesTextArea().click();
            keyboard.selectAll();
            enterKeycodeSequence(keyboard,
                KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT, KeyEvent.VK_COMMA, // Move to last row, add comma
                KeyEvent.VK_ENTER, KeyEvent.VK_TAB); // Create new row
            keyboard.enterText(newCombination);
            settingsFrame.okButton().click();
        });

        step("Toggle and verify toggle from 'Foo' to 'Bar'",
            () -> {
                editor.getEditor().findText("Foo").click();
                commonSteps.triggerToggleAction();
                assertEquals("Bar", editor.getEditor().getText());
                commonSteps.triggerToggleAction();
                assertEquals("Foo", editor.getEditor().getText());
            });

        step("Remove [\"Foo\", \"Bar\"] toggle from configured toggles", () -> {
            SettingsFrameFixture settingsFrame =
                commonSteps.openTogglerSettings();
            settingsFrame.togglesTextArea().click();
            keyboard.selectAll();
            enterKeycodeSequence(keyboard, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT);
            repeatKeycodeNTimes(keyboard, KeyEvent.VK_BACK_SPACE, newCombination.length()); // Delete new combination.
            repeatKeycodeNTimes(keyboard, KeyEvent.VK_BACK_SPACE, 3); // Delete up tab, line, comma
            settingsFrame.okButton().click();
        });

        step("Invalid toggle triggers notification", () -> {
            editor.getEditor().findText("Foo").click();
            commonSteps.triggerToggleAction();

            NotificationFixture notification = commonSteps.getNotification();
            assertTrue(notification != null &&
                notification.getDescriptionText().contains("No match"));
        });
    }
}
