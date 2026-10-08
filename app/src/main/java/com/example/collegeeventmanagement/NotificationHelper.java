package com.example.collegeeventmanagement;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID =
            "college_event_reminders";

    private static final String CHANNEL_NAME =
            "College Event Reminders";

    private static final int NOTIFICATION_BASE =
            10000;


    // =====================================================
    // CREATE NOTIFICATION CHANNEL
    // =====================================================

    public static void createNotificationChannel(
            Context context) {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager
                                    .IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    "Automatic reminders for registered college events"
            );

            channel.enableVibration(true);
            channel.setShowBadge(true);

            NotificationManager manager =
                    context.getSystemService(
                            NotificationManager.class
                    );

            if (manager != null) {

                manager.createNotificationChannel(
                        channel
                );
            }
        }
    }


    // =====================================================
    // CHECK NOTIFICATION PERMISSION
    // =====================================================

    private static boolean canNotify(
            Context context) {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            return context.checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    // =====================================================
    // SAVE NOTIFICATION TO DATABASE
    // =====================================================

    private static void saveNotificationToHistory(
            Context context,
            int eventId,
            String title,
            String message,
            int notificationType) {

        try {

            DatabaseHelper databaseHelper =
                    new DatabaseHelper(context);

            databaseHelper.saveNotification(
                    eventId,
                    title,
                    message,
                    notificationType
            );

            databaseHelper.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =====================================================
    // ONE HOUR REMINDER
    // =====================================================

    public static void showOneHourReminder(
            Context context,
            int eventId,
            String eventName,
            String eventDate,
            String eventTime,
            String venue) {

        String title =
                "Event in 1 Hour";

        String message =
                eventName +
                        " is starting in 1 hour.\n" +
                        "Time: " + eventTime +
                        "\nVenue: " + venue;


        // Save to in-app notification history
        // BEFORE checking Android permission.
        saveNotificationToHistory(
                context,
                eventId,
                title,
                message,
                1
        );


        // Show Android notification
        if (!canNotify(context)) {

            return;
        }


        showNotification(
                context,
                eventId,
                1,
                title,
                message
        );
    }


    // =====================================================
    // FIVE MINUTE REMINDER
    // =====================================================

    public static void showFiveMinuteReminder(
            Context context,
            int eventId,
            String eventName,
            String eventDate,
            String eventTime,
            String venue) {

        String title =
                "Event in 5 Minutes";

        String message =
                eventName +
                        " starts in 5 minutes.\n" +
                        "Time: " + eventTime +
                        "\nVenue: " + venue;


        // Save to in-app notification history
        // BEFORE checking Android permission.
        saveNotificationToHistory(
                context,
                eventId,
                title,
                message,
                2
        );


        // Show Android notification
        if (!canNotify(context)) {

            return;
        }


        showNotification(
                context,
                eventId,
                2,
                title,
                message
        );
    }


    // =====================================================
    // EVENT STARTED NOTIFICATION
    // =====================================================

    public static void showEventStartedNotification(
            Context context,
            int eventId,
            String eventName,
            String eventDate,
            String eventTime,
            String venue) {

        String title =
                "Event Started";

        String message =
                eventName +
                        " has started.\n" +
                        "Event: " + eventName +
                        "\nDate: " + eventDate +
                        "\nTime: " + eventTime +
                        "\nVenue: " + venue;


        // Save to in-app notification history
        // BEFORE checking Android permission.
        saveNotificationToHistory(
                context,
                eventId,
                title,
                message,
                3
        );


        // Show Android notification
        if (!canNotify(context)) {

            return;
        }


        showNotification(
                context,
                eventId,
                3,
                title,
                message
        );
    }


    // =====================================================
    // SHOW ANDROID NOTIFICATION
    // =====================================================

    private static void showNotification(
            Context context,
            int eventId,
            int reminderType,
            String title,
            String message) {

        createNotificationChannel(context);


        int notificationId =
                NOTIFICATION_BASE +
                        (eventId * 10) +
                        reminderType;


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                );


        builder.setSmallIcon(
                R.drawable.ic_notifications
        );


        builder.setContentTitle(
                title
        );


        builder.setContentText(
                message
        );


        builder.setStyle(
                new NotificationCompat.BigTextStyle()
                        .bigText(message)
        );


        builder.setPriority(
                NotificationCompat.PRIORITY_HIGH
        );


        builder.setCategory(
                NotificationCompat.CATEGORY_EVENT
        );


        builder.setAutoCancel(true);


        builder.setDefaults(
                NotificationCompat.DEFAULT_ALL
        );


        try {

            NotificationManagerCompat
                    .from(context)
                    .notify(
                            notificationId,
                            builder.build()
                    );

        } catch (SecurityException e) {

            e.printStackTrace();
        }
    }
}