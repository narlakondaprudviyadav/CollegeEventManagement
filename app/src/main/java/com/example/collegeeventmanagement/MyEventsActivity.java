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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyEventsActivity extends AppCompatActivity {

    LinearLayout eventsContainer;
    ImageButton backButton;

    DatabaseHelper databaseHelper;

    String studentEmail;
    String studentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_events);


        // =====================================================
        // FIND VIEWS
        // =====================================================

        eventsContainer =
                findViewById(R.id.eventsContainer);

        backButton =
                findViewById(R.id.backButton);


        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =====================================================
        // GET STUDENT DETAILS
        // =====================================================

        studentEmail =
                getIntent().getStringExtra("email");

        studentName =
                getIntent().getStringExtra("USER_NAME");


        if (studentName == null ||
                studentName.trim().isEmpty()) {

            studentName = "Student";
        }


        if (studentEmail == null) {

            studentEmail = "";
        }


        // =====================================================
        // BACK BUTTON
        // =====================================================

        backButton.setOnClickListener(v -> {

            goToStudentDashboard();

        });


        // =====================================================
        // PHONE BACK BUTTON
        // =====================================================

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {

                        goToStudentDashboard();

                    }
                }
        );


        // =====================================================
        // LOAD EVENTS
        // =====================================================

        loadMyEvents();
    }


    // =====================================================
    // GO TO STUDENT DASHBOARD
    // =====================================================

    private void goToStudentDashboard() {

        Intent intent =
                new Intent(
                        MyEventsActivity.this,
                        StudentDashboardActivity.class
                );


        intent.putExtra(
                "USER_NAME",
                studentName
        );


        intent.putExtra(
                "email",
                studentEmail
        );


        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );


        startActivity(intent);

        finish();
    }


    // =====================================================
    // LOAD MY EVENTS
    // =====================================================

    private void loadMyEvents() {

        eventsContainer.removeAllViews();


        // =====================================================
        // CHECK EMAIL
        // =====================================================

        if (studentEmail == null ||
                studentEmail.trim().isEmpty()) {

            showNoEvents(
                    "Unable to find student account."
            );

            return;
        }


        // =====================================================
        // GET REGISTERED EVENTS
        // =====================================================

        Cursor cursor =
                databaseHelper.getRegisteredEvents(
                        studentEmail
                );


        // =====================================================
        // NO EVENTS
        // =====================================================

        if (cursor == null ||
                cursor.getCount() == 0) {

            showNoEvents(
                    "You have not registered for any events yet."
            );

            if (cursor != null) {

                cursor.close();
            }

            return;
        }


        // =====================================================
        // READ EVENTS
        // =====================================================

        while (cursor.moveToNext()) {


            // =================================================
            // EVENT ID
            // =================================================

            int eventId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "event_id"
                            )
                    );


            // =================================================
            // EVENT DETAILS
            // =================================================

            String eventName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "event_name"
                            )
                    );


            String description =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "description"
                            )
                    );


            String date =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "event_date"
                            )
                    );


            String time =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "event_time"
                            )
                    );


            String venue =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "venue"
                            )
                    );


            String organizer =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "organizer"
                            )
                    );


            // =================================================
            // CHECK UPCOMING / COMPLETED
            // =================================================

            boolean isUpcoming =
                    isEventUpcoming(
                            date,
                            time
                    );


            // =================================================
            // CREATE CARD
            // =================================================

            LinearLayout card =
                    new LinearLayout(this);

            card.setOrientation(
                    LinearLayout.VERTICAL
            );


            card.setPadding(
                    dp(18),
                    dp(17),
                    dp(18),
                    dp(17)
            );


            // =================================================
            // CARD BACKGROUND
            // =================================================

            GradientDrawable cardBackground =
                    new GradientDrawable();

            cardBackground.setColor(
                    Color.WHITE
            );

            cardBackground.setCornerRadius(
                    dp(18)
            );

            cardBackground.setStroke(
                    dp(1),
                    Color.rgb(235, 231, 242)
            );

            card.setBackground(
                    cardBackground
            );


            // =================================================
            // CARD ELEVATION
            // =================================================

            card.setElevation(
                    dp(4)
            );


            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            cardParams.setMargins(
                    dp(10),
                    dp(8),
                    dp(10),
                    dp(8)
            );


            card.setLayoutParams(
                    cardParams
            );


            // =================================================
            // EVENT NAME
            // =================================================

            TextView nameView =
                    new TextView(this);

            nameView.setText(
                    eventName
            );

            nameView.setTextSize(
                    20
            );

            nameView.setTextColor(
                    Color.rgb(107, 77, 181)
            );

            nameView.setTypeface(
                    null,
                    Typeface.BOLD
            );

            nameView.setPadding(
                    0,
                    0,
                    0,
                    dp(3)
            );


            card.addView(
                    nameView
            );


            // =================================================
            // DESCRIPTION
            // =================================================

            TextView descriptionView =
                    new TextView(this);

            descriptionView.setText(
                    description
            );

            descriptionView.setTextSize(
                    14
            );

            descriptionView.setTextColor(
                    Color.rgb(90, 90, 90)
            );

            descriptionView.setPadding(
                    0,
                    dp(2),
                    0,
                    dp(12)
            );


            card.addView(
                    descriptionView
            );


            // =================================================
            // DETAILS CONTAINER
            // =================================================

            LinearLayout detailsLayout =
                    new LinearLayout(this);

            detailsLayout.setOrientation(
                    LinearLayout.VERTICAL
            );


            // =================================================
            // DATE
            // =================================================

            detailsLayout.addView(
                    createDetailText(
                            "📅  " + date
                    )
            );


            // =================================================
            // TIME
            // =================================================

            detailsLayout.addView(
                    createDetailText(
                            "🕐  " + time
                    )
            );


            // =================================================
            // VENUE
            // =================================================

            detailsLayout.addView(
                    createDetailText(
                            "📍  " + venue
                    )
            );


            // =================================================
            // ORGANIZER
            // =================================================

            detailsLayout.addView(
                    createDetailText(
                            "👤  " + organizer
                    )
            );


            card.addView(
                    detailsLayout
            );


            // =================================================
            // STATUS ROW
            // =================================================

            LinearLayout statusRow =
                    new LinearLayout(this);

            statusRow.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            statusRow.setGravity(
                    Gravity.CENTER_VERTICAL
            );


            LinearLayout.LayoutParams statusRowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            statusRowParams.setMargins(
                    0,
                    dp(12),
                    0,
                    0
            );


            statusRow.setLayoutParams(
                    statusRowParams
            );


            // =================================================
            // REGISTERED BADGE
            // =================================================

            TextView registeredText =
                    new TextView(this);

            registeredText.setText(
                    "✓  REGISTERED"
            );

            registeredText.setTextSize(
                    12
            );

            registeredText.setTextColor(
                    Color.rgb(46, 125, 50)
            );

            registeredText.setTypeface(
                    null,
                    Typeface.BOLD
            );

            registeredText.setGravity(
                    Gravity.CENTER
            );

            registeredText.setPadding(
                    dp(12),
                    dp(7),
                    dp(12),
                    dp(7)
            );


            // =================================================
            // REGISTERED BADGE BACKGROUND
            // =================================================

            GradientDrawable registeredBackground =
                    new GradientDrawable();

            registeredBackground.setColor(
                    Color.rgb(232, 245, 233)
            );

            registeredBackground.setCornerRadius(
                    dp(20)
            );

            registeredText.setBackground(
                    registeredBackground
            );


            LinearLayout.LayoutParams registeredParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            registeredText.setLayoutParams(
                    registeredParams
            );


            statusRow.addView(
                    registeredText
            );


            card.addView(
                    statusRow
            );


            // =================================================
            // DEREGISTER BUTTON
            // ONLY FOR UPCOMING EVENTS
            // =================================================

            if (isUpcoming) {

                Button deregisterButton =
                        new Button(this);

                deregisterButton.setText(
                        "Deregister"
                );

                deregisterButton.setTextSize(
                        14
                );

                deregisterButton.setAllCaps(
                        false
                );

                deregisterButton.setTextColor(
                        Color.rgb(198, 40, 40)
                );

                deregisterButton.setTypeface(
                        null,
                        Typeface.BOLD
                );

                deregisterButton.setGravity(
                        Gravity.CENTER
                );

                deregisterButton.setPadding(
                        dp(10),
                        0,
                        dp(10),
                        0
                );


                deregisterButton.setMinHeight(
                        0
                );

                deregisterButton.setMinimumHeight(
                        0
                );


                // =================================================
                // DEREGISTER BUTTON BACKGROUND
                // =================================================

                GradientDrawable deregisterBackground =
                        new GradientDrawable();

                deregisterBackground.setColor(
                        Color.rgb(255, 245, 245)
                );

                deregisterBackground.setCornerRadius(
                        dp(12)
                );

                deregisterBackground.setStroke(
                        dp(1),
                        Color.rgb(211, 47, 47)
                );


                deregisterButton.setBackground(
                        deregisterBackground
                );


                LinearLayout.LayoutParams buttonParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                dp(44)
                        );


                buttonParams.setMargins(
                        0,
                        dp(12),
                        0,
                        0
                );


                deregisterButton.setLayoutParams(
                        buttonParams
                );


                // =================================================
                // DEREGISTER CLICK
                // =================================================

                deregisterButton.setOnClickListener(v -> {

                    showDeregisterConfirmation(
                            eventId,
                            eventName
                    );

                });


                card.addView(
                        deregisterButton
                );
            }


            // =================================================
            // ADD CARD
            // =================================================

            eventsContainer.addView(
                    card
            );
        }


        cursor.close();
    }


    // =====================================================
    // CHECK IF EVENT IS UPCOMING
    // =====================================================

    private boolean isEventUpcoming(
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


            return eventDateTime.getTime()
                    > System.currentTimeMillis();


        } catch (ParseException e) {

            return false;
        }
    }


    // =====================================================
    // DEREGISTER CONFIRMATION
    // =====================================================

    private void showDeregisterConfirmation(
            int eventId,
            String eventName) {


        new AlertDialog.Builder(this)

                .setTitle(
                        "Deregister from Event?"
                )

                .setMessage(
                        "Are you sure you want to deregister from \"" +
                                eventName +
                                "\"?"
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Deregister",
                        (dialog, which) -> {

                            deregisterFromEvent(
                                    eventId,
                                    eventName
                            );

                        }
                )

                .show();
    }


    // =====================================================
    // DEREGISTER EVENT
    // =====================================================

    private void deregisterFromEvent(
            int eventId,
            String eventName) {


        boolean success =
                databaseHelper.deregisterFromEvent(
                        eventId,
                        studentEmail
                );


        if (success) {


            // =================================================
            // CANCEL REMINDERS
            // =================================================

            EventReminderScheduler.cancelReminders(
                    this,
                    eventId
            );


            Toast.makeText(
                    this,
                    "Deregistered from " +
                            eventName,
                    Toast.LENGTH_SHORT
            ).show();


            // =================================================
            // REFRESH MY EVENTS
            // =================================================

            loadMyEvents();


        } else {

            Toast.makeText(
                    this,
                    "Deregistration failed",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =====================================================
    // CREATE DETAIL TEXT
    // =====================================================

    private TextView createDetailText(
            String text) {

        TextView textView =
                new TextView(this);

        textView.setText(
                text
        );

        textView.setTextSize(
                14
        );

        textView.setTextColor(
                Color.rgb(80, 80, 80)
        );

        textView.setPadding(
                0,
                dp(3),
                0,
                dp(3)
        );

        return textView;
    }


    // =====================================================
    // NO EVENTS
    // =====================================================

    private void showNoEvents(
            String message) {

        TextView noEvents =
                new TextView(this);

        noEvents.setText(
                message
        );

        noEvents.setTextSize(
                16
        );

        noEvents.setTextColor(
                Color.rgb(90, 90, 90)
        );

        noEvents.setGravity(
                Gravity.CENTER
        );

        noEvents.setPadding(
                dp(20),
                dp(70),
                dp(20),
                dp(70)
        );


        eventsContainer.addView(
                noEvents
        );
    }


    // =====================================================
    // DP HELPER
    // =====================================================

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }
}