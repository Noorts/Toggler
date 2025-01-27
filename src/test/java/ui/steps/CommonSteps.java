package ui.steps;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.utils.Keyboard;
import ui.pages.SettingsFrameFixture;
import ui.pages.WelcomeFrameFixture;

import java.time.Duration;

public class CommonSteps {
    final private RemoteRobot remoteRobot;
    final private Keyboard keyboard;
    final private com.intellij.remoterobot.steps.CommonSteps commonSteps;

    public CommonSteps(RemoteRobot remoteRobot) {
        this.remoteRobot = remoteRobot;
        this.keyboard = new Keyboard(remoteRobot);
        this.commonSteps = new com.intellij.remoterobot.steps.CommonSteps(remoteRobot);
    }

    public void createNewProject() {
        WelcomeFrameFixture welcomeFrame =
            remoteRobot.find(WelcomeFrameFixture.class, Duration.ofSeconds(10));
        welcomeFrame.openNewProjectDialogButton().click();
        welcomeFrame.createNewProjectButton().click();
    }

    public void closeProject() {
        commonSteps.closeProject();
    }

    public SettingsFrameFixture openTogglerSettings() {
        // Open settings panel
        commonSteps.invokeAction("ShowSettings");

        SettingsFrameFixture settingsFrame =
            remoteRobot.find(SettingsFrameFixture.class, Duration.ofSeconds(10));
        // Open Toggler settings
        settingsFrame.searchBar().setText("Toggler");
        settingsFrame.waitUntilTogglerSettingsAreAccessible();

        return settingsFrame;
    }

    public void triggerToggleAction() {
        commonSteps.invokeAction("ToggleAction");
    }

    public void triggerToggleActionReverse() {
        commonSteps.invokeAction("ToggleActionReverse");
    }
}
