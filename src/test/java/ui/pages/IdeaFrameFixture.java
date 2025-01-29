package ui.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;

@FixtureName(name = "Idea frame")
@DefaultXpath(by = "IdeFrameImpl type", xpath = "//div[@class='IdeFrameImpl']")
public class IdeaFrameFixture extends CommonContainerFixture {
    RemoteRobot remoteRobot;

    public IdeaFrameFixture(RemoteRobot remoteRobot, RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    public boolean isDumbMode() {
        return callJs(
            """
                    const frameHelper = com.intellij.openapi.wm.impl.ProjectFrameHelper.getFrameHelper(component)
                    if (frameHelper) {
                        const project = frameHelper.getProject()
                        project ? com.intellij.openapi.project.DumbService.isDumb(project) : true
                    } else {
                        true
                    }
                """, true
        );
    }
}
