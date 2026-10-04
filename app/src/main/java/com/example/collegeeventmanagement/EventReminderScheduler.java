package com.example.collegeeventmanagement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventReminderScheduler {

    private static final long ONE_HOUR =
            60 * 60 * 1000L;

    private static final long FIVE_MINUTES =
            5 * 60 * 1000L;


    // =====================================================
    // SCHEDULE ALL REMINDERS
    // =====================================================

    public static void scheduleReminder(
            Context context,
            int eventId,
            String eventName,
            String eventDate,
            String eventTime,
            String venue) {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "d/M/yyyy HH:mm",
                        Locale.getDefault()
                );

        format.setLenient(false);

        Date eventDateTime;

        try {

            eventDateTime =
                    format.parse(
                            eventDate +
                                    " " +
                                    eventTime
                    );

        } catch (ParseException e) {

            e.printStackTrace();
            return;
        }


        if (eventDateTime == null) {
            return;
        }


        long eventTimeMillis =
                eventDateTime.getTime();

        long currentTimeMillis =
                System.currentTimeMillis();


        // =================================================
        // EVENT ALREADY STARTED
        // =================================================

        if (eventTimeMillis <= currentTimeMillis) {

            return;
        }


        // =================================================
        // 1 HOUR BEFORE
        // =================================================

        long oneHourBefore =
                eventTimeMillis -
                        ONE_HOUR;


        if (oneHourBefore >
                currentTimeMillis) {

            scheduleAlarm(
                    context,
                    eventId,
                    eventName,
                    eventDate,
                    eventTime,
                    venue,
                    oneHourBefore,
                    1
            );
        }


        // =================================================
        // 5 MINUTES BEFORE
        // =================================================

        long fiveMinutesBefore =
                eventTimeMillis -
                        FIVE_MINUTES;


        /*
         * Normal case:
         *
         * Event is more than 5 minutes away.
         * Schedule the reminder exactly 5 minutes before.
         */

        if (fiveMinutesBefore >
                currentTimeMillis) {

            scheduleAlarm(
                    context,
                    eventId,
                    eventName,
                    eventDate,
                    eventTime,
                    venue,
                    fiveMinutesBefore,
                    2
            );

        }

        /*
         * Event is already within the next 5 minutes.
         *
         * Instead of silently losing the 5-minute
         * reminder, trigger it almost immediately.
         */

        else {

            long immediateReminder =
                    currentTimeMillis + 1000L;


            scheduleAlarm(
                    context,
                    eventId,
                    eventName,
                    eventDate,
                    eventTime,
                    venue,
                    immediateReminder,
                    2
            );
        }


        // =================================================
        // EVENT START
        // =================================================

        scheduleAlarm(
                context,
                eventId,
                eventName,
                eventDate,
                eventTime,
                venue,
                eventTimeMillis,
                3
        );
    }


    // =====================================================
    // SCHEDULE ONE ALARM
    // =====================================================

    private static void scheduleAlarm(
            Context context,
            int eventId,
            String eventName,
            String eventDate,
            String eventTime,
            String venue,
            long triggerTime,
            int reminderType) {


        AlarmManager alarmManager =
                (AlarmManager)
                        context.getSystemService(
                                Context.ALARM_SERVICE
                        );


        if (alarmManager == null) {
            return;
        }


        // =================================================
        // INTENT
        // =================================================

        Intent intent =
                new Intent(
                        context,
                        EventReminderReceiver.class
                );


        intent.putExtra(
                "EVENT_ID",
                eventId
        );

        intent.putExtra(
                "EVENT_NAME",
                eventName
        );

        intent.putExtra(
                "EVENT_DATE",
                eventDate
        );

        intent.putExtra(
                "EVENT_TIME",
                eventTime
        );

        intent.putExtra(
                "VENUE",
                venue
        );

        intent.putExtra(
                "REMINDER_TYPE",
                reminderType
        );


        // =================================================
        // UNIQUE REQUEST CODE
        // =================================================

        int requestCode =
                eventId * 10 +
                        reminderType;


        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );


        // =================================================
        // EXACT ALARM
        // =================================================

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.S) {


            if (alarmManager.canScheduleExactAlarms()) {


                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                );


            } else {


                /*
                 * Exact alarm permission is not available.
                 *
                 * Use the best possible fallback.
                 */

                alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                );
            }


        } else {


            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }
    }


    // =====================================================
    // CANCEL ALL REMINDERS
    // =====================================================

    public static void cancelReminders(
            Context context,
            int eventId) {


        AlarmManager alarmManager =
                (AlarmManager)
                        context.getSystemService(
                                Context.ALARM_SERVICE
                        );


        if (alarmManager == null) {
            return;
        }


        // 1 HOUR

        cancelOneReminder(
                context,
                alarmManager,
                eventId,
                1
        );


        // 5 MINUTES

        cancelOneReminder(
                context,
                alarmManager,
                eventId,
                2
        );


        // EVENT START

        cancelOneReminder(
                context,
                alarmManager,
                eventId,
                3
        );
    }


    // =====================================================
    // CANCEL ONE REMINDER
    // =====================================================

    private static void cancelOneReminder(
            Context context,
            AlarmManager alarmManager,
            int eventId,
            int reminderType) {


        Intent intent =
                new Intent(
                        context,
                        EventReminderReceiver.class
                );


        intent.putExtra(
                "EVENT_ID",
                eventId
        );

        intent.putExtra(
                "REMINDER_TYPE",
                reminderType
        );


        int requestCode =
                eventId * 10 +
                        reminderType;


        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );


        alarmManager.cancel(
                pendingIntent
        );


        pendingIntent.cancel();
    }
}