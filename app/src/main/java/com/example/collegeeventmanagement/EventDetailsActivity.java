package com.example.collegeeventmanagement;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailsActivity extends AppCompatActivity {

    // =====================================================
    // VIEWS
    // =====================================================

    private TextView eventNameTextView;
    private TextView eventStatusTextView;
    private TextView eventDescriptionTextView;
    private TextView eventDateTextView;
    private TextView eventTimeTextView;
    private TextView eventVenueTextView;
    private TextView eventOrganizerTextView;
    private TextView registrationCountTextView;
    private TextView spotsLeftTextView;

    private ProgressBar registrationProgressBar;
    private Button registerButton;

    // BACK BUTTON
    private ImageButton backButton;

    // =====================================================
    // DATABASE
    // =====================================================

    private DatabaseHelper databaseHelper;

    // =====================================================
    // STUDENT DETAILS
    // =====================================================

    private String studentName;
    private String studentEmail;

    // =====================================================
    // EVENT DETAILS
    // =====================================================

    private int eventId;
    private String eventName;
    private String description;
    private String eventDate;
    private String eventTime;
    private String venue;
    private String organizer;
    private int maxRegistrations;

    // =====================================================
    // COLORS
    // =====================================================

    private final int PURPLE =
            Color.rgb(107, 77, 181);

    private final int GREEN =
            Color.rgb(52, 168, 83);

    private final int RED =
            Color.rgb(229, 57, 53);

    private final int GRAY =
            Color.rgb(100, 96, 105);

    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_event_details
        );

        // =====================================================
        // CONNECT VIEWS
        // =====================================================

        eventNameTextView =
                findViewById(
                        R.id.eventNameTextView
                );

        eventStatusTextView =
                findViewById(
                        R.id.eventStatusTextView
                );

        eventDescriptionTextView =
                findViewById(
                        R.id.eventDescriptionTextView
                );

        eventDateTextView =
                findViewById(
                        R.id.eventDateTextView
                );

        eventTimeTextView =
                findViewById(
                        R.id.eventTimeTextView
                );

        eventVenueTextView =
                findViewById(
                        R.id.eventVenueTextView
                );

        eventOrganizerTextView =
                findViewById(
                        R.id.eventOrganizerTextView
                );

        registrationCountTextView =
                findViewById(
                        R.id.registrationCountTextView
                );

        spotsLeftTextView =
                findViewById(
                        R.id.spotsLeftTextView
                );

        registrationProgressBar =
                findViewById(
                        R.id.registrationProgressBar
                );

        registerButton =
                findViewById(
                        R.id.registerButton
                );

        // =====================================================
        // BACK BUTTON
        // =====================================================

        backButton =
                findViewById(
                        R.id.backButton
                );

        backButton.setEnabled(true);
        backButton.setClickable(true);

        backButton.setOnClickListener(v -> {

            finish();

        });

        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);

        // =====================================================
        // GET STUDENT DETAILS
        // =====================================================

        studentName =
                getIntent().getStringExtra(
                        "USER_NAME"
                );

        studentEmail =
                getIntent().getStringExtra(
                        "email"
                );

        if (studentName == null ||
                studentName.trim().isEmpty()) {

            studentName = "Student";
        }

        if (studentEmail == null) {

            studentEmail = "";
        }

        // =====================================================
        // GET EVENT DETAILS
        // =====================================================

        eventId =
                getIntent().getIntExtra(
                        "EVENT_ID",
                        -1
                );

        eventName =
                getIntent().getStringExtra(
                        "EVENT_NAME"
                );

        description =
                getIntent().getStringExtra(
                        "DESCRIPTION"
                );

        eventDate =
                getIntent().getStringExtra(
                        "EVENT_DATE"
                );

        eventTime =
                getIntent().getStringExtra(
                        "EVENT_TIME"
                );

        venue =
                getIntent().getStringExtra(
                        "VENUE"
                );

        organizer =
                getIntent().getStringExtra(
                        "ORGANIZER"
                );

        maxRegistrations =
                getIntent().getIntExtra(
                        "MAX_REGISTRATIONS",
                        50
                );

        // =====================================================
        // VALIDATE EVENT
        // =====================================================

        if (eventId == -1 ||
                eventName == null) {

            Toast.makeText(
                    this,
                    "Unable to load event details",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // =====================================================
        // DISPLAY EVENT
        // =====================================================

        displayEventDetails();

        // =====================================================
        // REGISTER BUTTON
        // =====================================================

        registerButton.setOnClickListener(v ->
                showRegistrationConfirmation()
        );
    }

    // =====================================================
    // DISPLAY EVENT DETAILS
    // =====================================================

    private void displayEventDetails() {

        eventNameTextView.setText(
                eventName
        );

        eventDescriptionTextView.setText(
                description == null ||
                        description.trim().isEmpty()
                        ? "No description available."
                        : description
        );

        eventDateTextView.setText(
                "📅  " + safeValue(eventDate)
        );

        eventTimeTextView.setText(
                "🕐  " + safeValue(eventTime)
        );

        eventVenueTextView.setText(
                "📍  " + safeValue(venue)
        );

        eventOrganizerTextView.setText(
                "👤  " + safeValue(organizer)
        );

        updateEventStatus();

        updateRegistrationInformation();
    }

    // =====================================================
    // UPDATE EVENT STATUS
    // =====================================================

    private void updateEventStatus() {

        String status =
                getEventStatus(
                        eventDate,
                        eventTime
                );

        eventStatusTextView.setText(
                status
        );

        if (status.equals("UPCOMING")) {

            eventStatusTextView.setTextColor(
                    GREEN
            );

            eventStatusTextView.setBackgroundColor(
                    Color.rgb(
                            232,
                            247,
                            235
                    )
            );

        } else {

            eventStatusTextView.setTextColor(
                    Color.rgb(
                            105,
                            105,
                            105
                    )
            );

            eventStatusTextView.setBackgroundColor(
                    Color.rgb(
                            239,
                            239,
                            239
                    )
            );
        }
    }

    // =====================================================
    // UPDATE REGISTRATION INFORMATION
    // =====================================================

    private void updateRegistrationInformation() {

        int registeredCount =
                databaseHelper.getEventRegistrationCount(
                        eventId
                );

        maxRegistrations =
                databaseHelper.getEventMaxRegistrations(
                        eventId
                );

        if (maxRegistrations <= 0) {

            maxRegistrations = 50;
        }

        registrationCountTextView.setText(
                registeredCount +
                        " / " +
                        maxRegistrations
        );

        // =====================================================
        // PROGRESS
        // =====================================================

        int progress = 0;

        if (maxRegistrations > 0) {

            progress =
                    (int) (
                            (registeredCount * 100.0)
                                    / maxRegistrations
                    );

            if (progress > 100) {

                progress = 100;
            }
        }

        registrationProgressBar.setProgress(
                progress
        );

        // =====================================================
        // SPOTS LEFT
        // =====================================================

        int spotsLeft =
                maxRegistrations -
                        registeredCount;

        if (spotsLeft < 0) {

            spotsLeft = 0;
        }

        if (registeredCount >=
                maxRegistrations) {

            spotsLeftTextView.setText(
                    "FULL • No spots left"
            );

            spotsLeftTextView.setTextColor(
                    RED
            );

        } else {

            spotsLeftTextView.setText(
                    spotsLeft +
                            " spot" +
                            (spotsLeft == 1
                                    ? ""
                                    : "s") +
                            " left"
            );

            spotsLeftTextView.setTextColor(
                    GRAY
            );
        }

        // =====================================================
        // BUTTON STATE
        // =====================================================

        updateRegisterButton(
                registeredCount
        );
    }

    // =====================================================
    // UPDATE REGISTER BUTTON
    // =====================================================

    private void updateRegisterButton(
            int registeredCount) {

        String status =
                getEventStatus(
                        eventDate,
                        eventTime
                );

        boolean alreadyRegistered =
                databaseHelper.isStudentRegistered(
                        eventId,
                        studentEmail
                );

        boolean eventFull =
                registeredCount >=
                        maxRegistrations;

        // =====================================================
        // COMPLETED
        // =====================================================

        if (status.equals("COMPLETED")) {

            registerButton.setText(
                    "EVENT ENDED"
            );

            registerButton.setEnabled(
                    false
            );

            registerButton.setTextColor(
                    Color.WHITE
            );

            registerButton.setBackgroundColor(
                    Color.rgb(
                            158,
                            158,
                            158
                    )
            );

            return;
        }

        // =====================================================
        // REGISTERED
        // =====================================================

        if (alreadyRegistered) {

            registerButton.setText(
                    "✓  REGISTERED"
            );

            registerButton.setEnabled(
                    false
            );

            registerButton.setTextColor(
                    Color.WHITE
            );

            registerButton.setBackgroundColor(
                    GREEN
            );

            return;
        }

        // =====================================================
        // FULL
        // =====================================================

        if (eventFull) {

            registerButton.setText(
                    "FULL"
            );

            registerButton.setEnabled(
                    false
            );

            registerButton.setTextColor(
                    Color.WHITE
            );

            registerButton.setBackgroundColor(
                    RED
            );

            return;
        }

        // =====================================================
        // AVAILABLE
        // =====================================================

        registerButton.setText(
                "REGISTER NOW"
        );

        registerButton.setEnabled(
                true
        );

        registerButton.setTextColor(
                Color.WHITE
        );

        registerButton.setBackgroundColor(
                PURPLE
        );

        registerButton.setTypeface(
                null,
                Typeface.BOLD
        );
    }

    // =====================================================
    // REGISTRATION CONFIRMATION
    // =====================================================

    private void showRegistrationConfirmation() {

        // =====================================================
        // RECHECK EVENT STATUS BEFORE SHOWING DIALOG
        // =====================================================

        String currentStatus =
                getEventStatus(
                        eventDate,
                        eventTime
                );

        if (currentStatus.equals(
                "COMPLETED"
        )) {

            updateRegistrationInformation();

            Toast.makeText(
                    this,
                    "This event has ended.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =====================================================
        // RECHECK CAPACITY
        // =====================================================

        int currentCount =
                databaseHelper.getEventRegistrationCount(
                        eventId
                );

        maxRegistrations =
                databaseHelper.getEventMaxRegistrations(
                        eventId
                );

        if (maxRegistrations <= 0) {

            maxRegistrations = 50;
        }

        if (currentCount >=
                maxRegistrations) {

            updateRegistrationInformation();

            Toast.makeText(
                    this,
                    "This event is full.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =====================================================
        // RECHECK EXISTING REGISTRATION
        // =====================================================

        boolean alreadyRegistered =
                databaseHelper.isStudentRegistered(
                        eventId,
                        studentEmail
                );

        if (alreadyRegistered) {

            updateRegistrationInformation();

            Toast.makeText(
                    this,
                    "You are already registered.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =====================================================
        // CONFIRMATION MESSAGE
        // =====================================================

        String message =
                "Are you sure you want to register for this event?"
                        + "\n\n"
                        + "📅  "
                        + safeValue(eventDate)
                        + "\n"
                        + "🕐  "
                        + safeValue(eventTime)
                        + "\n"
                        + "📍  "
                        + safeValue(venue);

        // =====================================================
        // DIALOG
        // =====================================================

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "Confirm Registration"
                        )
                        .setMessage(
                                message
                        )
                        .setNegativeButton(
                                "CANCEL",
                                null
                        )
                        .setPositiveButton(
                                "CONFIRM",
                                (dialogInterface, which) -> {

                                    registerStudent();

                                }
                        )
                        .create();

        dialog.show();

        // =====================================================
        // PURPLE CONFIRM BUTTON
        // =====================================================

        dialog.getButton(
                AlertDialog.BUTTON_POSITIVE
        ).setTextColor(
                PURPLE
        );

        // =====================================================
        // GRAY CANCEL BUTTON
        // =====================================================

        dialog.getButton(
                AlertDialog.BUTTON_NEGATIVE
        ).setTextColor(
                GRAY
        );
    }

    // =====================================================
    // REGISTER STUDENT
    // =====================================================

    private void registerStudent() {

        // =====================================================
        // RECHECK STATUS
        // =====================================================

        String currentStatus =
                getEventStatus(
                        eventDate,
                        eventTime
                );

        if (currentStatus.equals(
                "COMPLETED"
        )) {

            updateRegistrationInformation();

            Toast.makeText(
                    this,
                    "This event has ended.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =====================================================
        // REGISTER
        // =====================================================

        boolean success =
                databaseHelper.registerForEvent(
                        eventId,
                        studentName,
                        studentEmail
                );

        // =====================================================
        // SUCCESS
        // =====================================================

        if (success) {

            // =================================================
            // EXISTING REMINDER SYSTEM
            // =================================================

            EventReminderScheduler.scheduleReminder(
                    this,
                    eventId,
                    eventName,
                    eventDate,
                    eventTime,
                    venue
            );

            Toast.makeText(
                    this,
                    "Registered successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            updateRegistrationInformation();

        }

        // =====================================================
        // FAILED
        // =====================================================

        else {

            boolean nowRegistered =
                    databaseHelper.isStudentRegistered(
                            eventId,
                            studentEmail
                    );

            int currentCount =
                    databaseHelper.getEventRegistrationCount(
                            eventId
                    );

            if (nowRegistered) {

                Toast.makeText(
                        this,
                        "You are already registered.",
                        Toast.LENGTH_SHORT
                ).show();

            } else if (
                    currentCount >=
                            maxRegistrations
            ) {

                Toast.makeText(
                        this,
                        "This event is full.",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        "Registration failed.",
                        Toast.LENGTH_SHORT
                ).show();
            }

            updateRegistrationInformation();
        }
    }

    // =====================================================
    // EVENT STATUS
    // =====================================================

    private String getEventStatus(
            String eventDate,
            String eventTime) {

        try {

            String eventDateTime =
                    eventDate +
                            " " +
                            eventTime;

            java.text.SimpleDateFormat format =
                    new java.text.SimpleDateFormat(
                            "d/M/yyyy HH:mm",
                            java.util.Locale.getDefault()
                    );

            format.setLenient(
                    false
            );

            java.util.Date eventDateTimeObject =
                    format.parse(
                            eventDateTime
                    );

            java.util.Date currentDateTime =
                    new java.util.Date();

            if (eventDateTimeObject != null &&
                    currentDateTime.before(
                            eventDateTimeObject
                    )) {

                return "UPCOMING";

            } else {

                return "COMPLETED";
            }

        } catch (java.text.ParseException e) {

            return "UPCOMING";
        }
    }

    // =====================================================
    // SAFE VALUE
    // =====================================================

    private String safeValue(
            String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    // =====================================================
    // SYSTEM BACK BUTTON
    // =====================================================

    @Override
    public void onBackPressed() {

        finish();

    }
}