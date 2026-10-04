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


        if (intent == null) {
            return;
        }


        // =================================================
        // GET EVENT DETAILS
        // =================================================

        int eventId =
                intent.getIntExtra(
                        "EVENT_ID",
                        -1
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
                        -1
                );


        // =================================================
        // DEFAULT VALUES
        // =================================================

        if (eventName == null ||
                eventName.trim().isEmpty()) {

            eventName = "College Event";
        }


        if (eventDate == null) {
            eventDate = "";
        }


        if (eventTime == null) {
            eventTime = "";
        }


        if (venue == null ||
                venue.trim().isEmpty()) {

            venue = "College Campus";
        }


        // =================================================
        // CREATE NOTIFICATION CHANNEL
        // =================================================

        NotificationHelper.createNotificationChannel(
                context
        );


        // =================================================
        // REMINDER TYPE
        // =================================================

        switch (reminderType) {


            // =============================================
            // 1 HOUR BEFORE
            // =============================================

            case 1:

                NotificationHelper.showOneHourReminder(
                        context,
                        eventId,
                        eventName,
                        eventDate,
                        eventTime,
                        venue
                );

                break;


            // =============================================
            // 5 MINUTES BEFORE
            // =============================================

            case 2:

                NotificationHelper.showFiveMinuteReminder(
                        context,
                        eventId,
                        eventName,
                        eventDate,
                        eventTime,
                        venue
                );

                break;


            // =============================================
            // EVENT STARTED
            // =============================================

            case 3:

                NotificationHelper.showEventStartedNotification(
                        context,
                        eventId,
                        eventName,
                        eventDate,
                        eventTime,
                        venue
                );

                break;


            default:

                break;
        }
    }
}