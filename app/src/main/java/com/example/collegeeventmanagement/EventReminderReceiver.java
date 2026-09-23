package com.example.collegeeventmanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EventReminderReceiver
        extends BroadcastReceiver {

    @Override
    public void onReceive(
            Context context,
            Intent intent) {

        // =================================================
        // GET EVENT DETAILS
        // =================================================

        int eventId =
                intent.getIntExtra(
                        "EVENT_ID",
                        0
                );

        String eventName =
                intent.getStringExtra(
                        "EVENT_NAME"
                );

        String eventDate =
                intent.getStringExtra(
                        "EVENT_DATE"
                );

        String eventTime =
                intent.getStringExtra(
                        "EVENT_TIME"
                );

        String venue =
                intent.getStringExtra(
                        "VENUE"
                );

        int reminderType =
                intent.getIntExtra(
                        "REMINDER_TYPE",
                        0
                );


        // =================================================
        // DEFAULT VALUES
        // =================================================

        if (eventName == null) {
            eventName = "College Event";
        }

        if (eventDate == null) {
            eventDate = "";
        }

        if (eventTime == null) {
            eventTime = "";
        }

        if (venue == null) {
            venue = "";
        }


        // =================================================
        // SHOW CORRECT NOTIFICATION
        // =================================================

        if (reminderType == 1) {

            // 1 HOUR BEFORE

            NotificationHelper.showOneHourReminder(
                    context,
                    eventName,
                    eventDate,
                    eventTime,
                    venue,
                    eventId * 10 + 1
            );

        } else if (reminderType == 2) {

            // 5 MINUTES BEFORE

            NotificationHelper.showFiveMinuteReminder(
                    context,
                    eventName,
                    eventDate,
                    eventTime,
                    venue,
                    eventId * 10 + 2
            );

        } else if (reminderType == 3) {

            // EVENT STARTED

            NotificationHelper.showEventStartedNotification(
                    context,
                    eventName,
                    eventDate,
                    eventTime,
                    venue,
                    eventId * 10 + 3
            );
        }
    }
}