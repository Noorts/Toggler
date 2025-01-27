package ui.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.FixtureName;
import org.jetbrains.annotations.NotNull;

@FixtureName(name = "JBTextArea")
public class JBTextAreaFixture extends ComponentFixture {
    RemoteRobot remoteRobot;

    public JBTextAreaFixture(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    public String getText() {
        return callJs("component.getText() || \"\"", true);
    }
}
