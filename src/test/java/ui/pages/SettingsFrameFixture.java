package ui.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.*;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitForIgnoringError;
import static java.time.Duration.ofSeconds;

@FixtureName(name = "Settings frame")
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='MyDialog']")
public class SettingsFrameFixture extends CommonContainerFixture {
    RemoteRobot remoteRobot;

    public SettingsFrameFixture(RemoteRobot remoteRobot, RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    public boolean isTogglerSettingsPanelVisible() {
        // Bread crumb at top of the settings panel should be "Tools > Toggler".
        return remoteRobot.find(ComponentFixture.class,
                byXpath("//div[@class='NonOpaquePanel'][.//div[@class='Breadcrumbs']]"),
                Duration.ofSeconds(10))
            .hasText("Toggler");
    }

    /**
     * Waits until Toggler's settings panel is visible, or until time out.
     * Fails the test upon time out.
     * <p>
     * This method only tests for visibility, it does not take any
     * action to open Toggler's settings. See CommonSteps.openTogglerSettings
     * for that.
     */
    public void waitUntilTogglerSettingsPanelIsVisible() {
        waitForIgnoringError(Duration.ofSeconds(10), ofSeconds(2),
            "Wait till Toggler settings panel is visible",
            "Toggler settings panel is not visible",
            this::isTogglerSettingsPanelVisible);
    }

    public JTextFieldFixture searchBar() {
        return remoteRobot.find(JTextFieldFixture.class,
            byXpath("//div[@class='TextFieldWithProcessing']"));
    }

    public JButtonFixture cancelButton() {
        return remoteRobot.find(JButtonFixture.class,
            byXpath("//div[@text='Cancel']"));
    }

    public JButtonFixture applyButton() {
        return remoteRobot.find(JButtonFixture.class,
            byXpath("//div[@text='Apply']"));
    }

    public JButtonFixture okButton() {
        return remoteRobot.find(JButtonFixture.class,
            byXpath("//div[@text='OK']"));
    }

    /* Toggler specific components. */

    public JCheckboxFixture partialMatchingCheckbox() {
        return remoteRobot.find(JCheckboxFixture.class,
            byXpath("//div[@class='JCheckBox']"));
    }

    public JLabelFixture statusLabel() {
        return remoteRobot.find(JLabelFixture.class,
            byXpath("//div[@class='ConfigurableCardPanel']" +
                "//div[@class='JPanel']//div[@class='JBLabel']"));
    }

    public JBTextAreaFixture togglesTextArea() {
        return remoteRobot.find(JBTextAreaFixture.class,
            byXpath("//div[@class='JBTextArea']"));
    }

    public JButtonFixture importButton() {
        return remoteRobot.find(JButtonFixture.class,
            byXpath("//div[@text='Import']"));
    }

    public JButtonFixture exportButton() {
        return remoteRobot.find(JButtonFixture.class,
            byXpath("//div[@text='Export']"));
    }

    public JButtonFixture resetToDefaultButton() {
        return remoteRobot.find(JButtonFixture.class,
            byXpath("//div[@text='Reset to Defaults']"));
    }
    // TODO: Add "Reset to Default" confirmation dialog fixtures.
}
