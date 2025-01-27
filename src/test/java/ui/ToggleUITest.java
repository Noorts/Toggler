package ui;

import com.automation.remarks.junit5.Video;
import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.TextEditorFixture;
import com.intellij.remoterobot.utils.Keyboard;
import org.junit.jupiter.api.BeforeAll;
import ui.pages.IdeaFrameFixture;
import ui.steps.CommonSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.utils.StepsLogger;

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitForIgnoringError;

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
        waitForIgnoringError(Duration.ofMinutes(3), Duration.ofSeconds(5),
            "Wait till IDE is started", "Ide is not started",
            () -> remoteRobot.callJs("true"));

        step("Create new project", commonSteps::createNewProject);

        step("Wait till IDE pane is loaded", () -> {
            final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class,
                Duration.ofSeconds(10));
            waitFor(Duration.ofMinutes(5), () -> !idea.isDumbMode());
        });
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
        final TextEditorFixture editor = idea.textEditor(Duration.ofSeconds(5));

        step("Set editor text to True", () -> {
            // This overwrites the current editor's content.
            editor.getEditor().setText("True");
        });

        step("Toggle word", () -> {
            editor.getEditor().findText("True").click();
            commonSteps.triggerToggleAction();
        });

        step("Verify that word was toggled", () -> {
            assert(editor.getEditor().getText().equals("False"));
        });
    }
}
