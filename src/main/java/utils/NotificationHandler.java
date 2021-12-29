package utils;

import com.intellij.notification.NotificationType;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * A utility that handles notifications.
 *
 * See: https://plugins.jetbrains.com/docs/intellij/notifications.html#top-level-notifications-balloons
 */
public class NotificationHandler {
    private NotificationHandler() { throw new IllegalStateException("Utility class"); }

    public static void notify(String content, NotificationType notificationType, @Nullable Editor editor) {
        Project project = editor != null ? editor.getProject() : null;
        NotificationGroupManager.getInstance().getNotificationGroup("Toggler")
                .createNotification(content, notificationType)
                .notify(project);
    }

    public static void notify(String content, NotificationType notificationType) {
        notify(content, notificationType, null);
    }
}
