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

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitForIgnoringError;
import static java.time.Duration.ofSeconds;
import static org.assertj.swing.timing.Pause.pause;

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
            assert (editor.getEditor().getText().equals("False"));
        });

        step("Toggle and verify that False was toggled back to True", () -> {
            commonSteps.triggerToggleAction();
            assert (editor.getEditor().getText().equals("True"));
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
            assert (commonSteps.getNotification()
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
            assert (notification != null &&
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
            assert (settingsFrame.partialMatchingCheckbox().isSelected());
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
                assert (editor.getEditor().getText().equals("removeClass"));
                commonSteps.triggerToggleAction();
                assert (editor.getEditor().getText().equals("addClass"));
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
            assert (notification != null &&
                notification.getDescriptionText().contains("No match"));
        });
    }
}
