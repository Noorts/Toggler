package ui.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.UtilsKt.hasAnyComponent;

/**
 * The Welcome Frame that is displayed on IDE launch when a project has not
 * been opened yet.
 */
@FixtureName(name = "Welcome Frame")
@DefaultXpath(by = "FlatWelcomeFrame type", xpath = "//div[@class='FlatWelcomeFrame']")
public class WelcomeFrameFixture extends CommonContainerFixture {
    RemoteRobot remoteRobot;

    public WelcomeFrameFixture(RemoteRobot remoteRobot, RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Opens the new project dialog upon clicking.
     */
    public ComponentFixture openNewProjectDialogButton() {
        if (hasAnyComponent(remoteRobot, byXpath("//div[@text='Welcome to IntelliJ IDEA']"))) {
            return remoteRobot.find(ComponentFixture.class,
                byXpath("//div[@defaulticon='createNewProjectTab.svg']"));
        }
        return remoteRobot.find(ComponentFixture.class,
            byXpath("//div[@visible_text='New Project']"));
    }

    /**
     * The create button inside the "new project dialog". Upon click, creates a
     * new project and opens it in the IDE.
     * <p>
     * Requires the new project dialog to be open.
     * See: openNewProjectDialogButton.
     */
    public ComponentFixture createNewProjectButton() {
        return remoteRobot.find(ComponentFixture.class,
            byXpath("//div[@text='Create']"), Duration.ofSeconds(10));
    }
}