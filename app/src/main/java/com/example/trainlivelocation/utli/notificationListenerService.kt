package com.example.trainlivelocation.utli

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification


class notificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Handle incoming notification here
        if (sbn.packageName == "com.example.trainlivelocation.utli") {
            val notification: Notification = sbn.notification
            val extras: Bundle = notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE)
            val text = extras.getString(Notification.EXTRA_TEXT)
            // Perform any necessary processing here
        }
    }
}