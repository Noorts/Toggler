package ui.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.*;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

@FixtureName(name = "Notification")
@DefaultXpath(by = "NotificationCenter type",
    xpath = "//div[@javaclass='com.intellij.ui.BalloonImpl$MyComponent']")
public class NotificationFixture extends CommonContainerFixture {
    RemoteRobot remoteRobot;

    public NotificationFixture(RemoteRobot remoteRobot,
                               RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    // TODO: Get type of notification? E.g., INFORMATION. See val_icon name.

    public String getTitleText() {
        return remoteRobot.find(JLabelFixture.class,
                byXpath("//div[@class='NotificationCenterPanel']//div[@class='JLabel']"))
            .getValue();
    }

    public String getDescriptionText() {
        return remoteRobot.find(ComponentFixture.class,
                byXpath("//div[@class='NotificationCenterPanel']//div[@class='JEditorPane']"))
            .callJs("component.getText() || \"\"", true);
    }

    public void closeNotification() {
        // TODO: Use child based find instead. See: `this.button(locator)`.
        //  Apply this child based approach to all custom fixtures.
        this.remoteRobot.find(JButtonFixture.class, byXpath("//div" +
            "[@myhovericon='closeHover.svg']")).click();
    }
}
