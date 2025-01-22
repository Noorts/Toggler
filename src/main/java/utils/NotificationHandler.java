package utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import core.AppSettingsConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Docs
// - https://plugins.jetbrains.com/docs/intellij/notifications.html#top-level-notifications-balloons
// - https://plugins.jetbrains.com/docs/intellij/balloon.html#actions

/**
 * A utility that handles notifications.
 */
public class NotificationHandler {
    private final static String TITLE = "Toggler";
    private final static String GROUP_ID = "Toggler";

    private NotificationHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static void notify(String content, NotificationType notificationType, @Nullable Editor editor) {
        Project project = editor != null ? editor.getProject() : null;

        NotificationGroupManager.getInstance().getNotificationGroup(GROUP_ID)
            .createNotification(TITLE, content, notificationType)
            .notify(project);
    }

    public static void notify(String content, NotificationType notificationType) {
        notify(content, notificationType, null);
    }

    /**
     * Notify the user. The notification includes a button that opens the
     * Toggler settings panel.
     */
    public static void notifyWithOpenSettingsAction(String content, NotificationType notificationType,
                                                    @Nullable Editor editor) {
        Project project = editor != null ? editor.getProject() : null;
        NotificationGroupManager.getInstance().getNotificationGroup(GROUP_ID)
            .createNotification(TITLE, content, notificationType)
            .addAction(new OpenTogglerSettingsAction())
            .notify(project);
    }

    public static class OpenTogglerSettingsAction extends NotificationAction {
        public OpenTogglerSettingsAction() {
            super("Configure toggles...");
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
            ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), AppSettingsConfigurable.class);
        }
    }
}
