package com.example.collegeeventmanagement;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static final String CHANNEL_ID =
            "event_reminder_channel";

    private static final String CHANNEL_NAME =
            "Event Reminders";

    private static final String CHANNEL_DESCRIPTION =
            "Notifications for upcoming college events";


    // =====================================================
    // CREATE NOTIFICATION CHANNEL
    // =====================================================

    public static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager manager =
                    context.getSystemService(
                            NotificationManager.class
                    );

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


    // =====================================================
    // SHOW 1 HOUR REMINDER
    // =====================================================

    public static void showOneHourReminder(
            Context context,
            String eventName,
            String eventDate,
            String eventTime,
            String venue,
            int notificationId) {

        showNotification(
                context,
                "Event Reminder",
                eventName + " starts in 1 hour",
                eventName,
                eventDate,
                eventTime,
                venue,
                notificationId
        );
    }


    // =====================================================
    // SHOW 5 MINUTE REMINDER
    // =====================================================

    public static void showFiveMinuteReminder(
            Context context,
            String eventName,
            String eventDate,
            String eventTime,
            String venue,
            int notificationId) {

        showNotification(
                context,
                "Event Reminder",
                eventName + " starts in 5 minutes",
                eventName,
                eventDate,
                eventTime,
                venue,
                notificationId
        );
    }


    // =====================================================
    // SHOW EVENT STARTED NOTIFICATION
    // =====================================================

    public static void showEventStartedNotification(
            Context context,
            String eventName,
            String eventDate,
            String eventTime,
            String venue,
            int notificationId) {

        showNotification(
                context,
                "Event Started",
                eventName + " has started",
                eventName,
                eventDate,
                eventTime,
                venue,
                notificationId
        );
    }


    // =====================================================
    // COMMON NOTIFICATION METHOD
    // =====================================================

    private static void showNotification(
            Context context,
            String title,
            String shortText,
            String eventName,
            String eventDate,
            String eventTime,
            String venue,
            int notificationId) {

        createNotificationChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                );

        builder.setSmallIcon(
                android.R.drawable.ic_dialog_info
        );

        builder.setContentTitle(title);

        builder.setContentText(shortText);

        builder.setStyle(
                new NotificationCompat.BigTextStyle()
                        .bigText(
                                shortText +
                                        "\n\nEvent: " +
                                        eventName +
                                        "\nDate: " +
                                        eventDate +
                                        "\nTime: " +
                                        eventTime +
                                        "\nVenue: " +
                                        venue
                        )
        );

        builder.setPriority(
                NotificationCompat.PRIORITY_HIGH
        );

        builder.setAutoCancel(true);

        builder.setDefaults(
                NotificationCompat.DEFAULT_ALL
        );

        NotificationManagerCompat manager =
                NotificationManagerCompat.from(context);

        try {

            manager.notify(
                    notificationId,
                    builder.build()
            );

        } catch (SecurityException e) {

            // Notification permission not granted
        }
    }
}