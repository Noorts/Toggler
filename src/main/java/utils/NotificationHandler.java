package utils;

import com.intellij.notification.NotificationType;
import com.intellij.notification.NotificationGroupManager;

public class NotificationHandler {
    public static void notify(String content, NotificationType notificationType) {
        NotificationGroupManager.getInstance().getNotificationGroup("Toggler")
                .createNotification("Toggler", content, notificationType, null)
                .notify(null);
    }
}
