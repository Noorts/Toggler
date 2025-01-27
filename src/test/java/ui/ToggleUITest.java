package ui;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.TextEditorFixture;
import com.intellij.remoterobot.utils.Keyboard;
import ui.pages.IdeaFrameFixture;
import ui.steps.CommonSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitForIgnoringError;

public class ToggleUITest {
    private final RemoteRobot remoteRobot = new RemoteRobot("http://127.0.0.1:8082");
    private final Keyboard keyboard = new Keyboard(remoteRobot);
    private final CommonSteps commonSteps = new CommonSteps(remoteRobot);

    @BeforeEach
    public void testInit() {
        waitForIgnoringError(Duration.ofMinutes(3), Duration.ofSeconds(5),
            "Wait for Ide started", "Ide is not started",
            () -> remoteRobot.callJs("true"));

        commonSteps.createNewProject();

        // Wait till IDE pane loads
        final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class,
            Duration.ofSeconds(10));
        waitFor(Duration.ofMinutes(5), () -> !idea.isDumbMode());
    }

    @AfterEach
    public void testCleanup() {
        // TODO: Handle cleaning up the projects? Delete the project afterwards?
        commonSteps.closeProject();
    }

    @Test
    void testTrivial() {
        final IdeaFrameFixture idea = remoteRobot.find(IdeaFrameFixture.class);
        final TextEditorFixture editor = idea.textEditor(Duration.ofSeconds(2));

        editor.getEditor().findText(";").click();

        assert (0 == 0);
    }
}
