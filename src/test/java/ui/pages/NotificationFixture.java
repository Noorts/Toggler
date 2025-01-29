package ui.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.*;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

@FixtureName(name = "Notification")
@DefaultXpath(by = "NotificationCenter type",
    xpath = "//div[@class='NotificationCenterPanel']")
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
}
