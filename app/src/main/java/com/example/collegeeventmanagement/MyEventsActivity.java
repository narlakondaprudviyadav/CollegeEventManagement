package com.example.collegeeventmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
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

        // =========================
        // FIND VIEWS
        // =========================

        eventsContainer = findViewById(R.id.eventsContainer);
        backButton = findViewById(R.id.backButton);

        // =========================
        // DATABASE
        // =========================

        databaseHelper = new DatabaseHelper(this);

        // =========================
        // GET STUDENT DETAILS
        // =========================

        studentEmail =
                getIntent().getStringExtra("email");

        studentName =
                getIntent().getStringExtra("USER_NAME");

        // =========================
        // BACK BUTTON
        // =========================

        backButton.setOnClickListener(v -> {

            goToStudentDashboard();

        });

        // =========================
        // PHONE BACK BUTTON
        // =========================

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {

                        goToStudentDashboard();

                    }
                }
        );

        // =========================
        // LOAD MY EVENTS
        // =========================

        loadMyEvents();
    }

    // =====================================================
    // REFRESH EVENTS
    // =====================================================

    @Override
    protected void onResume() {
        super.onResume();

        if (eventsContainer != null &&
                databaseHelper != null) {

            loadMyEvents();
        }
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
    // LOAD REGISTERED EVENTS
    // =====================================================

    private void loadMyEvents() {

        eventsContainer.removeAllViews();

        // =========================
        // CHECK EMAIL
        // =========================

        if (studentEmail == null ||
                studentEmail.trim().isEmpty()) {

            showNoEvents(
                    "Unable to find student account."
            );

            return;
        }

        // =========================
        // GET REGISTERED EVENTS
        // =========================

        Cursor cursor =
                databaseHelper.getRegisteredEvents(
                        studentEmail
                );

        // =========================
        // NO REGISTERED EVENTS
        // =========================

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

        // =========================
        // READ REGISTERED EVENTS
        // =========================

        while (cursor.moveToNext()) {

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

            // =========================
            // CREATE EVENT CARD
            // =========================

            LinearLayout card =
                    new LinearLayout(this);

            card.setOrientation(
                    LinearLayout.VERTICAL
            );

            card.setPadding(
                    14,
                    14,
                    14,
                    14
            );

            // =========================
            // CARD BACKGROUND
            // =========================

            GradientDrawable background =
                    new GradientDrawable();

            background.setColor(
                    Color.WHITE
            );

            background.setCornerRadius(
                    18
            );

            card.setBackground(
                    background
            );

            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            cardParams.setMargins(
                    0,
                    0,
                    0,
                    10
            );

            card.setLayoutParams(
                    cardParams
            );

            // =========================
            // EVENT NAME
            // =========================

            TextView nameView =
                    new TextView(this);

            nameView.setText(
                    eventName
            );

            nameView.setTextSize(
                    18
            );

            nameView.setTextColor(
                    Color.rgb(41, 35, 51)
            );

            nameView.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(
                    nameView
            );

            // =====================================================
            // EVENT STATUS
            // =====================================================

            String eventStatus =
                    getEventStatus(date, time);

            // Status horizontal layout
            LinearLayout statusLayout =
                    new LinearLayout(this);

            statusLayout.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            statusLayout.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            LinearLayout.LayoutParams statusLayoutParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            statusLayoutParams.setMargins(
                    0,
                    8,
                    0,
                    8
            );

            statusLayout.setLayoutParams(
                    statusLayoutParams
            );

            // =========================
            // STATUS DOT
            // =========================

            TextView statusDot =
                    new TextView(this);

            statusDot.setText(
                    "●"
            );

            statusDot.setTextSize(
                    14
            );

            statusDot.setGravity(
                    Gravity.CENTER
            );

            LinearLayout.LayoutParams dotParams =
                    new LinearLayout.LayoutParams(
                            22,
                            22
                    );

            statusDot.setLayoutParams(
                    dotParams
            );

            // =========================
            // STATUS TEXT
            // =========================

            TextView statusText =
                    new TextView(this);

            statusText.setText(
                    eventStatus
            );

            statusText.setTextSize(
                    13
            );

            statusText.setTypeface(
                    null,
                    Typeface.BOLD
            );

            LinearLayout.LayoutParams statusTextParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            statusTextParams.setMargins(
                    3,
                    0,
                    0,
                    0
            );

            statusText.setLayoutParams(
                    statusTextParams
            );

            // =========================
            // UPCOMING
            // =========================

            if (eventStatus.equals("UPCOMING")) {

                statusDot.setTextColor(
                        Color.rgb(76, 175, 80)
                );

                statusText.setTextColor(
                        Color.rgb(76, 175, 80)
                );

                // Blinking / pulsing dot
                Animation pulseAnimation =
                        AnimationUtils.loadAnimation(
                                this,
                                R.anim.pulse_dot
                        );

                statusDot.startAnimation(
                        pulseAnimation
                );

            }

            // =========================
            // COMPLETED
            // =========================

            else {

                statusDot.setTextColor(
                        Color.rgb(158, 158, 158)
                );

                statusText.setTextColor(
                        Color.rgb(117, 117, 117)
                );

                // No animation for completed
                statusDot.clearAnimation();
            }

            statusLayout.addView(
                    statusDot
            );

            statusLayout.addView(
                    statusText
            );

            card.addView(
                    statusLayout
            );

            // =========================
            // DESCRIPTION
            // =========================

            TextView descriptionView =
                    new TextView(this);

            descriptionView.setText(
                    description
            );

            descriptionView.setTextSize(
                    14
            );

            descriptionView.setTextColor(
                    Color.DKGRAY
            );

            descriptionView.setPadding(
                    0,
                    5,
                    0,
                    7
            );

            card.addView(
                    descriptionView
            );

            // =========================
            // DATE
            // =========================

            card.addView(
                    createDetailText(
                            "📅  " + date
                    )
            );

            // =========================
            // TIME
            // =========================

            card.addView(
                    createDetailText(
                            "🕐  " + time
                    )
            );

            // =========================
            // VENUE
            // =========================

            card.addView(
                    createDetailText(
                            "📍  " + venue
                    )
            );

            // =========================
            // ORGANIZER
            // =========================

            card.addView(
                    createDetailText(
                            "👤  " + organizer
                    )
            );

            // =====================================================
            // REGISTERED LABEL
            // =====================================================

            TextView registeredText =
                    new TextView(this);

            registeredText.setText(
                    "REGISTERED"
            );

            registeredText.setTextSize(
                    13
            );

            registeredText.setTextColor(
                    Color.WHITE
            );

            registeredText.setTypeface(
                    null,
                    Typeface.BOLD
            );

            registeredText.setGravity(
                    Gravity.CENTER
            );

            registeredText.setPadding(
                    0,
                    8,
                    0,
                    8
            );

            // =========================
            // REGISTERED BACKGROUND
            // =========================

            GradientDrawable registeredBackground =
                    new GradientDrawable();

            registeredBackground.setColor(
                    Color.rgb(76, 175, 80)
            );

            registeredBackground.setCornerRadius(
                    10
            );

            registeredText.setBackground(
                    registeredBackground
            );

            LinearLayout.LayoutParams registeredParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            registeredParams.setMargins(
                    0,
                    8,
                    0,
                    0
            );

            registeredText.setLayoutParams(
                    registeredParams
            );

            card.addView(
                    registeredText
            );

            // =========================
            // ADD CARD
            // =========================

            eventsContainer.addView(
                    card
            );
        }

        cursor.close();
    }

    // =====================================================
    // GET EVENT STATUS
    // =====================================================

    private String getEventStatus(
            String eventDate,
            String eventTime) {

        try {

            /*
             * Date format:
             * dd/MM/yyyy
             *
             * Time format:
             * HH:mm
             */

            String eventDateTime =
                    eventDate + " " + eventTime;

            SimpleDateFormat dateFormat =
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault()
                    );

            dateFormat.setLenient(false);

            Date eventDateTimeObject =
                    dateFormat.parse(eventDateTime);

            Date currentDateTime =
                    new Date();

            if (eventDateTimeObject != null &&
                    currentDateTime.before(
                            eventDateTimeObject
                    )) {

                return "UPCOMING";

            } else {

                return "COMPLETED";
            }

        } catch (ParseException e) {

            return "UPCOMING";
        }
    }

    // =====================================================
    // NO EVENTS MESSAGE
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
    }

    // =====================================================
    // DETAIL TEXT
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
                Color.DKGRAY
        );

        textView.setPadding(
                0,
                2,
                0,
                2
        );

        return textView;
    }
}