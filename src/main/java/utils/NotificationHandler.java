package utils;

import com.intellij.notification.NotificationType;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.openapi.editor.Editor;

/**
 * A utility that handles notifications.
 *
 * See: https://plugins.jetbrains.com/docs/intellij/notifications.html#top-level-notifications-balloons
 */
public class NotificationHandler {
    private NotificationHandler() { throw new IllegalStateException("Utility class"); }

    public static void notify(String content, NotificationType notificationType, Editor editor) {
        NotificationGroupManager.getInstance().getNotificationGroup("Toggler")
                .createNotification(content, notificationType)
                .notify(editor.getProject());
    }
}
