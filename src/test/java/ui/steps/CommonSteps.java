package ui.steps;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.Fixture;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.utils.Keyboard;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import ui.pages.NotificationFixture;
import ui.pages.SettingsFrameFixture;
import ui.pages.WelcomeFrameFixture;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static java.time.Duration.ofSeconds;
import static org.assertj.swing.timing.Pause.pause;

public class CommonSteps {
    final private RemoteRobot remoteRobot;
    final private Keyboard keyboard;
    final private com.intellij.remoterobot.steps.CommonSteps commonSteps;

    public CommonSteps(RemoteRobot remoteRobot) {
        this.remoteRobot = remoteRobot;
        this.keyboard = new Keyboard(remoteRobot);
        this.commonSteps = new com.intellij.remoterobot.steps.CommonSteps(remoteRobot);
    }

    /**
     * Returns True if a fixture of the fixtureClass is present/findable at
     * least once.
     */
    public <T extends Fixture> boolean isFixturePresent(Class<T> fixtureClass) {
        return !remoteRobot.findAll(fixtureClass).isEmpty();
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
        // TODO: Make dynamic. Sometimes ShowSettings is invoked when the IDE is
        //  not yet ready, and thus the settings are not opened. E.g., between
        //  test runs.
        pause(ofSeconds(1).toMillis());

        // Open the IDE's settings panel if not already open
        if (!isFixturePresent(SettingsFrameFixture.class)) {
            commonSteps.invokeAction("ShowSettings");
        }

        SettingsFrameFixture settingsFrame =
            remoteRobot.find(SettingsFrameFixture.class, Duration.ofSeconds(10));

        // Navigate to Toggler's settings
        if (!settingsFrame.isTogglerSettingsPanelVisible()) {
            JTreeFixture treeFixture = remoteRobot.find(JTreeFixture.class,
                byXpath("//div[@class='MyTree']"), Duration.ofSeconds(5));
            if (!treeFixture.isPathExists(new String[]{"Toggler"}, true)) {
                // Expand "Tools" row to make the "Toggler" row visible.
                treeFixture.doubleClickRowWithText("Tools", true);
            }
            // Open Toggler's settings.
            treeFixture.clickRowWithText("Toggler", true);

        }
        settingsFrame.waitUntilTogglerSettingsPanelIsVisible();

        return settingsFrame;
    }

    public void resetTogglerSettings() {
        SettingsFrameFixture settingsFrame =
            openTogglerSettings();
        settingsFrame.resetToDefaultOpenDialogButton().click();
        settingsFrame.resetToDefaultDialogYesButton().click();
        settingsFrame.okButton().click();
    }

    public void triggerToggleAction() {
        commonSteps.invokeAction("ToggleAction");
    }

    public void triggerToggleActionReverse() {
        commonSteps.invokeAction("ToggleActionReverse");
    }

    /**
     * Returns one of the currently active notifications. If none are active,
     * returns null.
     */
    public NotificationFixture getNotification() {
        try {
            return remoteRobot.find(
                NotificationFixture.class, Duration.ofSeconds(5));
        } catch (WaitForConditionTimeoutException e) {
            return null;
        }
    }

    /**
     * Close all currently open notifications one by one.
     */
    public void closeNotifications() {
        NotificationFixture fixture = getNotification();
        while (fixture != null) {
            // TODO: Potential race condition between finding the
            //  notification and clicking to close it. It might've already
            //  been closed by the notification time out.
            fixture.closeNotification();
            fixture = getNotification();
        }
        ;
    }
}
