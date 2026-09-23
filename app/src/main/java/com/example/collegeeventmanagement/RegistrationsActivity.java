package com.example.collegeeventmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private LinearLayout registrationsContainer;

    private DatabaseHelper databaseHelper;


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_registrations
        );


        // =================================================
        // DATABASE
        // =================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =================================================
        // CONNECT VIEWS
        // =================================================

        backButton =
                findViewById(
                        R.id.backButton
                );

        registrationsContainer =
                findViewById(
                        R.id.registrationsContainer
                );


        // =================================================
        // BACK BUTTON
        // =================================================

        backButton.setOnClickListener(v ->
                finish()
        );


        // =================================================
        // LOAD REGISTRATIONS
        // =================================================

        loadRegistrations();
    }


    // =====================================================
    // LOAD ALL REGISTRATIONS
    // =====================================================

    private void loadRegistrations() {

        registrationsContainer.removeAllViews();


        Cursor cursor =
                databaseHelper.getAllRegistrations();


        // =================================================
        // NO REGISTRATIONS
        // =================================================

        if (cursor == null ||
                cursor.getCount() == 0) {

            if (cursor != null) {
                cursor.close();
            }

            showNoRegistrations();

            return;
        }


        // =================================================
        // READ REGISTRATIONS
        // =================================================

        while (cursor.moveToNext()) {


            // -------------------------------------------------
            // REGISTRATION ID
            // -------------------------------------------------

            int registrationId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "registration_id"
                            )
                    );


            // -------------------------------------------------
            // EVENT ID
            // -------------------------------------------------

            int eventId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "event_id"
                            )
                    );


            // -------------------------------------------------
            // STUDENT NAME
            // -------------------------------------------------

            String studentName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "student_name"
                            )
                    );


            // -------------------------------------------------
            // STUDENT EMAIL
            // -------------------------------------------------

            String studentEmail =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "student_email"
                            )
                    );


            // -------------------------------------------------
            // EVENT NAME
            // -------------------------------------------------

            String eventName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "event_name"
                            )
                    );


            // -------------------------------------------------
            // EVENT DATE
            // -------------------------------------------------

            String eventDate =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "event_date"
                            )
                    );


            // -------------------------------------------------
            // EVENT TIME
            // -------------------------------------------------

            String eventTime =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "event_time"
                            )
                    );


            // -------------------------------------------------
            // VENUE
            // -------------------------------------------------

            String venue =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "venue"
                            )
                    );


            // =================================================
            // ADD REGISTRATION CARD
            // =================================================

            addRegistrationCard(
                    registrationId,
                    eventId,
                    studentName,
                    studentEmail,
                    eventName,
                    eventDate,
                    eventTime,
                    venue
            );
        }


        // =================================================
        // CLOSE CURSOR
        // =================================================

        cursor.close();
    }


    // =====================================================
    // CREATE REGISTRATION CARD
    // =====================================================

    private void addRegistrationCard(
            int registrationId,
            int eventId,
            String studentName,
            String studentEmail,
            String eventName,
            String eventDate,
            String eventTime,
            String venue) {


        // =================================================
        // CARD
        // =================================================

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );


        // =================================================
        // CARD BACKGROUND
        // =================================================

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                Color.WHITE
        );

        background.setCornerRadius(
                dp(16)
        );

        background.setStroke(
                dp(1),
                Color.rgb(
                        230,
                        224,
                        240
                )
        );

        card.setBackground(
                background
        );

        card.setElevation(
                dp(3)
        );


        // =================================================
        // CARD MARGINS
        // =================================================

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                dp(10),
                dp(7),
                dp(10),
                dp(7)
        );

        card.setLayoutParams(
                cardParams
        );


        // =================================================
        // STUDENT NAME
        // =================================================

        TextView studentNameText =
                new TextView(this);

        studentNameText.setText(
                studentName
        );

        studentNameText.setTextSize(
                19
        );

        studentNameText.setTextColor(
                Color.rgb(
                        107,
                        77,
                        181
                )
        );

        studentNameText.setTypeface(
                null,
                Typeface.BOLD
        );

        studentNameText.setPadding(
                0,
                0,
                0,
                dp(6)
        );

        card.addView(
                studentNameText
        );


        // =================================================
        // STUDENT EMAIL
        // =================================================

        TextView emailText =
                createDetailText(
                        "Email: " +
                                studentEmail
                );

        card.addView(
                emailText
        );


        // =================================================
        // EVENT NAME
        // =================================================

        TextView eventText =
                createDetailText(
                        "Event: " +
                                eventName
                );

        eventText.setTextColor(
                Color.rgb(
                        50,
                        45,
                        60
                )
        );

        eventText.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                eventText
        );


        // =================================================
        // REGISTRATION COUNT
        // =================================================

        int registeredCount =
                databaseHelper.getEventRegistrationCount(
                        eventId
                );

        int maxRegistrations =
                databaseHelper.getEventMaxRegistrations(
                        eventId
                );


        TextView registrationCountText =
                createDetailText(
                        "Registrations: " +
                                registeredCount +
                                " / " +
                                maxRegistrations
                );

        registrationCountText.setTextColor(
                Color.rgb(
                        107,
                        77,
                        181
                )
        );

        registrationCountText.setTypeface(
                null,
                Typeface.BOLD
        );

        registrationCountText.setTextSize(
                14
        );

        card.addView(
                registrationCountText
        );


        // =================================================
        // DATE
        // =================================================

        TextView dateText =
                createDetailText(
                        "Date: " +
                                eventDate
                );

        card.addView(
                dateText
        );


        // =================================================
        // TIME
        // =================================================

        TextView timeText =
                createDetailText(
                        "Time: " +
                                eventTime
                );

        card.addView(
                timeText
        );


        // =================================================
        // VENUE
        // =================================================

        TextView venueText =
                createDetailText(
                        "Venue: " +
                                venue
                );

        card.addView(
                venueText
        );


        // =================================================
        // REGISTRATION ID
        // =================================================

        TextView registrationIdText =
                createDetailText(
                        "Registration ID: " +
                                registrationId
                );

        registrationIdText.setTextSize(
                11
        );

        registrationIdText.setTextColor(
                Color.GRAY
        );

        registrationIdText.setPadding(
                0,
                dp(8),
                0,
                0
        );

        card.addView(
                registrationIdText
        );


        // =================================================
        // REGISTERED LABEL
        // =================================================

        TextView registeredLabel =
                new TextView(this);

        registeredLabel.setText(
                "✓  REGISTERED"
        );

        registeredLabel.setTextSize(
                12
        );

        registeredLabel.setTextColor(
                Color.rgb(
                        52,
                        168,
                        83
                )
        );

        registeredLabel.setTypeface(
                null,
                Typeface.BOLD
        );

        registeredLabel.setGravity(
                Gravity.CENTER
        );


        // =================================================
        // REGISTERED LABEL BACKGROUND
        // =================================================

        GradientDrawable registeredBackground =
                new GradientDrawable();

        registeredBackground.setColor(
                Color.rgb(
                        232,
                        247,
                        235
                )
        );

        registeredBackground.setCornerRadius(
                dp(20)
        );

        registeredLabel.setBackground(
                registeredBackground
        );


        // =================================================
        // REGISTERED LABEL SIZE
        // =================================================

        LinearLayout.LayoutParams labelParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        dp(32)
                );

        labelParams.setMargins(
                0,
                dp(10),
                0,
                0
        );

        registeredLabel.setLayoutParams(
                labelParams
        );

        registeredLabel.setPadding(
                dp(12),
                0,
                dp(12),
                0
        );


        card.addView(
                registeredLabel
        );


        // =================================================
        // ADD CARD
        // =================================================

        registrationsContainer.addView(
                card
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
                13
        );

        textView.setTextColor(
                Color.rgb(
                        100,
                        96,
                        105
                )
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
    // NO REGISTRATIONS
    // =====================================================

    private void showNoRegistrations() {

        TextView noRegistrations =
                new TextView(this);

        noRegistrations.setText(
                "📋\n\n" +
                        "No registrations yet.\n" +
                        "Students who register for events " +
                        "will appear here."
        );

        noRegistrations.setTextSize(
                17
        );

        noRegistrations.setTextColor(
                Color.rgb(
                        100,
                        96,
                        105
                )
        );

        noRegistrations.setGravity(
                Gravity.CENTER
        );

        noRegistrations.setPadding(
                dp(25),
                dp(80),
                dp(25),
                dp(30)
        );

        registrationsContainer.addView(
                noRegistrations
        );
    }


    // =====================================================
    // REFRESH WHEN RETURNING
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                registrationsContainer != null) {

            loadRegistrations();
        }
    }


    // =====================================================
    // DP CONVERSION
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