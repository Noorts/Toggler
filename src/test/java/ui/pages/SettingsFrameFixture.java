package ui.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.*;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

@FixtureName(name = "Settings frame")
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='MyDialog']")
public class SettingsFrameFixture extends CommonContainerFixture {
    RemoteRobot remoteRobot;

    public SettingsFrameFixture(RemoteRobot remoteRobot, RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Returns once the toggler settings are accessible or until a timeout
     * occurs.
     */
    public void waitUntilTogglerSettingsAreAccessible() {
        // TODO: Properly throw error if failure occurs?
        // TODO: Or separate Toggler specific settings panel from the general
        //  settings panel.
        remoteRobot.find(JCheckboxFixture.class,
            byXpath("//div[@class='JCheckBox']"), Duration.ofSeconds(10));
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
