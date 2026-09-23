package com.example.collegeeventmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ManageEventsActivity extends AppCompatActivity {

    ImageButton backButton;
    LinearLayout eventsContainer;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_events);

        // =========================
        // DATABASE
        // =========================

        databaseHelper =
                new DatabaseHelper(this);

        // =========================
        // CONNECT XML VIEWS
        // =========================

        backButton =
                findViewById(R.id.backButton);

        eventsContainer =
                findViewById(R.id.eventsContainer);

        // =========================
        // BACK BUTTON
        // =========================

        backButton.setOnClickListener(v -> finish());

        // =========================
        // LOAD EVENTS
        // =========================

        loadEvents();
    }


    // =====================================================
    // LOAD ALL EVENTS
    // =====================================================

    private void loadEvents() {

        eventsContainer.removeAllViews();

        Cursor cursor =
                databaseHelper.getAllEvents();

        // =========================
        // NO EVENTS
        // =========================

        if (cursor == null ||
                cursor.getCount() == 0) {

            if (cursor != null) {
                cursor.close();
            }

            TextView noEvents =
                    new TextView(this);

            noEvents.setText(
                    "No events available."
            );

            noEvents.setTextSize(18);

            noEvents.setTextColor(
                    Color.DKGRAY
            );

            noEvents.setGravity(
                    Gravity.CENTER
            );

            noEvents.setPadding(
                    20,
                    60,
                    20,
                    60
            );

            eventsContainer.addView(
                    noEvents
            );

            return;
        }


        // =========================
        // READ EVENTS
        // =========================

        while (cursor.moveToNext()) {

            int eventIdIndex =
                    cursor.getColumnIndex("event_id");

            int eventNameIndex =
                    cursor.getColumnIndex("event_name");

            int descriptionIndex =
                    cursor.getColumnIndex("description");

            int dateIndex =
                    cursor.getColumnIndex("event_date");

            int timeIndex =
                    cursor.getColumnIndex("event_time");

            int venueIndex =
                    cursor.getColumnIndex("venue");

            int organizerIndex =
                    cursor.getColumnIndex("organizer");

            int maxRegistrationsIndex =
                    cursor.getColumnIndex("max_registrations");


            int eventId =
                    eventIdIndex != -1
                            ? cursor.getInt(eventIdIndex)
                            : -1;

            String eventName =
                    eventNameIndex != -1
                            ? cursor.getString(eventNameIndex)
                            : "Unknown Event";

            String description =
                    descriptionIndex != -1
                            ? cursor.getString(descriptionIndex)
                            : "";

            String eventDate =
                    dateIndex != -1
                            ? cursor.getString(dateIndex)
                            : "";

            String eventTime =
                    timeIndex != -1
                            ? cursor.getString(timeIndex)
                            : "";

            String venue =
                    venueIndex != -1
                            ? cursor.getString(venueIndex)
                            : "";

            String organizer =
                    organizerIndex != -1
                            ? cursor.getString(organizerIndex)
                            : "";

            int maxRegistrations =
                    maxRegistrationsIndex != -1
                            ? cursor.getInt(maxRegistrationsIndex)
                            : 50;


            // =========================
            // GET REGISTRATION COUNT
            // =========================

            int registeredCount =
                    databaseHelper.getEventRegistrationCount(
                            eventId
                    );


            addEventCard(
                    eventId,
                    eventName,
                    description,
                    eventDate,
                    eventTime,
                    venue,
                    organizer,
                    registeredCount,
                    maxRegistrations
            );
        }


        // =========================
        // CLOSE CURSOR
        // =========================

        cursor.close();
    }


    // =====================================================
    // ADD EVENT CARD
    // =====================================================

    private void addEventCard(
            int eventId,
            String eventName,
            String description,
            String eventDate,
            String eventTime,
            String venue,
            String organizer,
            int registeredCount,
            int maxRegistrations) {


        // =========================
        // CARD
        // =========================

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                20,
                20,
                20
        );


        // =========================
        // CARD BACKGROUND
        // =========================

        GradientDrawable cardBackground =
                new GradientDrawable();

        cardBackground.setColor(
                Color.WHITE
        );

        cardBackground.setCornerRadius(
                20
        );

        cardBackground.setStroke(
                1,
                Color.LTGRAY
        );

        card.setBackground(
                cardBackground
        );


        // =========================
        // CARD MARGINS
        // =========================

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                16,
                10,
                16,
                10
        );

        card.setLayoutParams(
                cardParams
        );


        // =========================
        // EVENT NAME
        // =========================

        TextView eventNameText =
                new TextView(this);

        eventNameText.setText(
                eventName
        );

        eventNameText.setTextSize(
                20
        );

        eventNameText.setTextColor(
                Color.rgb(
                        107,
                        77,
                        181
                )
        );

        eventNameText.setTypeface(
                null,
                Typeface.BOLD
        );

        eventNameText.setPadding(
                0,
                0,
                0,
                8
        );


        // =========================
        // DESCRIPTION
        // =========================

        TextView descriptionText =
                new TextView(this);

        descriptionText.setText(
                "Description: " + description
        );

        descriptionText.setTextSize(
                15
        );

        descriptionText.setTextColor(
                Color.DKGRAY
        );

        descriptionText.setPadding(
                0,
                4,
                0,
                4
        );


        // =========================
        // DATE
        // =========================

        TextView dateText =
                new TextView(this);

        dateText.setText(
                "Date: " + eventDate
        );

        dateText.setTextSize(
                15
        );

        dateText.setTextColor(
                Color.DKGRAY
        );

        dateText.setPadding(
                0,
                4,
                0,
                4
        );


        // =========================
        // TIME
        // =========================

        TextView timeText =
                new TextView(this);

        timeText.setText(
                "Time: " + eventTime
        );

        timeText.setTextSize(
                15
        );

        timeText.setTextColor(
                Color.DKGRAY
        );

        timeText.setPadding(
                0,
                4,
                0,
                4
        );


        // =========================
        // VENUE
        // =========================

        TextView venueText =
                new TextView(this);

        venueText.setText(
                "Venue: " + venue
        );

        venueText.setTextSize(
                15
        );

        venueText.setTextColor(
                Color.DKGRAY
        );

        venueText.setPadding(
                0,
                4,
                0,
                4
        );


        // =========================
        // ORGANIZER
        // =========================

        TextView organizerText =
                new TextView(this);

        organizerText.setText(
                "Organizer: " + organizer
        );

        organizerText.setTextSize(
                15
        );

        organizerText.setTextColor(
                Color.DKGRAY
        );

        organizerText.setPadding(
                0,
                4,
                0,
                4
        );


        // =========================
        // REGISTRATION CAPACITY
        // =========================

        TextView registrationText =
                new TextView(this);

        registrationText.setText(
                "Registered: " +
                        registeredCount +
                        " / " +
                        maxRegistrations
        );

        registrationText.setTextSize(
                16
        );

        registrationText.setTypeface(
                null,
                Typeface.BOLD
        );

        registrationText.setTextColor(
                Color.rgb(
                        70,
                        70,
                        70
                )
        );

        registrationText.setPadding(
                0,
                8,
                0,
                4
        );


        // =========================
        // EVENT STATUS
        // =========================

        TextView statusText =
                new TextView(this);

        boolean completed =
                isEventCompleted(
                        eventDate,
                        eventTime
                );


        if (completed) {

            statusText.setText(
                    "COMPLETED"
            );

            statusText.setTextColor(
                    Color.rgb(
                            200,
                            60,
                            60
                    )
            );

        } else {

            statusText.setText(
                    "UPCOMING"
            );

            statusText.setTextColor(
                    Color.rgb(
                            50,
                            150,
                            80
                    )
            );
        }


        statusText.setTextSize(
                14
        );

        statusText.setTypeface(
                null,
                Typeface.BOLD
        );

        statusText.setPadding(
                0,
                4,
                0,
                8
        );


        // =====================================================
        // EDIT BUTTON
        // =====================================================

        Button editButton =
                new Button(this);

        editButton.setText(
                "EDIT"
        );

        editButton.setTextColor(
                Color.WHITE
        );

        editButton.setTextSize(
                14
        );

        editButton.setAllCaps(
                false
        );

        editButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        Color.rgb(
                                106,
                                27,
                                154
                        )
                )
        );

        LinearLayout.LayoutParams editButtonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        editButtonParams.setMargins(
                0,
                10,
                0,
                0
        );

        editButton.setLayoutParams(
                editButtonParams
        );


        // =====================================================
        // OPEN EDIT EVENT SCREEN
        // =====================================================

        editButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ManageEventsActivity.this,
                            EditEventActivity.class
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
                    "EVENT_DESCRIPTION",
                    description
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
                    "EVENT_VENUE",
                    venue
            );

            intent.putExtra(
                    "EVENT_ORGANIZER",
                    organizer
            );

            intent.putExtra(
                    "EVENT_MAX_REGISTRATIONS",
                    maxRegistrations
            );

            startActivity(intent);
        });


        // =========================
        // ADD VIEWS TO CARD
        // =========================

        card.addView(
                eventNameText
        );

        card.addView(
                descriptionText
        );

        card.addView(
                dateText
        );

        card.addView(
                timeText
        );

        card.addView(
                venueText
        );

        card.addView(
                organizerText
        );

        card.addView(
                registrationText
        );

        card.addView(
                statusText
        );

        card.addView(
                editButton
        );


        // =========================
        // ADD CARD TO CONTAINER
        // =========================

        eventsContainer.addView(
                card
        );
    }


    // =====================================================
    // CHECK EVENT STATUS
    // =====================================================

    private boolean isEventCompleted(
            String eventDate,
            String eventTime) {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "d/M/yyyy HH:mm",
                        Locale.getDefault()
                );

        format.setLenient(false);


        try {

            Date eventDateTime =
                    format.parse(
                            eventDate +
                                    " " +
                                    eventTime
                    );

            if (eventDateTime == null) {
                return false;
            }

            return new Date().after(
                    eventDateTime
            );

        } catch (ParseException e) {

            return false;
        }
    }


    // =====================================================
    // REFRESH WHEN RETURNING
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                eventsContainer != null) {

            loadEvents();
        }
    }
}