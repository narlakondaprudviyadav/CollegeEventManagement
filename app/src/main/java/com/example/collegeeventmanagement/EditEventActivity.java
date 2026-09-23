package com.example.collegeeventmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText venueEditText;
    private EditText organizerEditText;
    private EditText maxRegistrationsEditText;

    private Button saveChangesButton;
    private ImageButton backButton;

    private DatabaseHelper databaseHelper;

    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // -------------------------------------------------
        // INITIALIZE DATABASE
        // -------------------------------------------------

        databaseHelper = new DatabaseHelper(this);

        // -------------------------------------------------
        // CONNECT XML VIEWS
        // -------------------------------------------------

        eventNameEditText = findViewById(R.id.eventNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        venueEditText = findViewById(R.id.venueEditText);
        organizerEditText = findViewById(R.id.organizerEditText);
        maxRegistrationsEditText =
                findViewById(R.id.maxRegistrationsEditText);

        saveChangesButton =
                findViewById(R.id.saveChangesButton);

        backButton =
                findViewById(R.id.backButton);

        // -------------------------------------------------
        // GET EVENT DETAILS FROM MANAGE EVENTS
        // -------------------------------------------------

        Intent intent = getIntent();

        eventId = intent.getIntExtra("EVENT_ID", -1);

        String eventName =
                intent.getStringExtra("EVENT_NAME");

        String description =
                intent.getStringExtra("EVENT_DESCRIPTION");

        String date =
                intent.getStringExtra("EVENT_DATE");

        String time =
                intent.getStringExtra("EVENT_TIME");

        String venue =
                intent.getStringExtra("EVENT_VENUE");

        String organizer =
                intent.getStringExtra("EVENT_ORGANIZER");

        int maxRegistrations =
                intent.getIntExtra(
                        "EVENT_MAX_REGISTRATIONS",
                        50
                );

        // -------------------------------------------------
        // DISPLAY EXISTING EVENT DETAILS
        // -------------------------------------------------

        eventNameEditText.setText(
                eventName == null ? "" : eventName
        );

        descriptionEditText.setText(
                description == null ? "" : description
        );

        dateEditText.setText(
                date == null ? "" : date
        );

        timeEditText.setText(
                time == null ? "" : time
        );

        venueEditText.setText(
                venue == null ? "" : venue
        );

        organizerEditText.setText(
                organizer == null ? "" : organizer
        );

        maxRegistrationsEditText.setText(
                String.valueOf(maxRegistrations)
        );

        // -------------------------------------------------
        // DATE PICKER
        // -------------------------------------------------

        dateEditText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            EditEventActivity.this,

                            (view, year, month, dayOfMonth) -> {

                                String selectedDate =
                                        dayOfMonth +
                                                "/" +
                                                (month + 1) +
                                                "/" +
                                                year;

                                dateEditText.setText(
                                        selectedDate
                                );
                            },

                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

            datePickerDialog.show();
        });

        // -------------------------------------------------
        // TIME PICKER
        // -------------------------------------------------

        timeEditText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            TimePickerDialog timePickerDialog =
                    new TimePickerDialog(
                            EditEventActivity.this,

                            (view, hourOfDay, minute) -> {

                                String selectedTime =
                                        String.format(
                                                Locale.getDefault(),
                                                "%02d:%02d",
                                                hourOfDay,
                                                minute
                                        );

                                timeEditText.setText(
                                        selectedTime
                                );
                            },

                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );

            timePickerDialog.show();
        });

        // -------------------------------------------------
        // SAVE CHANGES
        // -------------------------------------------------

        saveChangesButton.setOnClickListener(v ->
                saveEventChanges()
        );

        // -------------------------------------------------
        // BACK BUTTON
        // -------------------------------------------------

        backButton.setOnClickListener(v ->
                finish()
        );
    }

    // =====================================================
    // SAVE EVENT CHANGES
    // =====================================================

    private void saveEventChanges() {

        String eventName =
                eventNameEditText
                        .getText()
                        .toString()
                        .trim();

        String description =
                descriptionEditText
                        .getText()
                        .toString()
                        .trim();

        String date =
                dateEditText
                        .getText()
                        .toString()
                        .trim();

        String time =
                timeEditText
                        .getText()
                        .toString()
                        .trim();

        String venue =
                venueEditText
                        .getText()
                        .toString()
                        .trim();

        String organizer =
                organizerEditText
                        .getText()
                        .toString()
                        .trim();

        String maxText =
                maxRegistrationsEditText
                        .getText()
                        .toString()
                        .trim();

        // -------------------------------------------------
        // VALIDATION
        // -------------------------------------------------

        if (eventName.isEmpty()) {
            eventNameEditText.setError(
                    "Enter event name"
            );
            eventNameEditText.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            descriptionEditText.setError(
                    "Enter description"
            );
            descriptionEditText.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            dateEditText.setError(
                    "Select event date"
            );
            return;
        }

        if (time.isEmpty()) {
            timeEditText.setError(
                    "Select event time"
            );
            return;
        }

        if (venue.isEmpty()) {
            venueEditText.setError(
                    "Enter venue"
            );
            venueEditText.requestFocus();
            return;
        }

        if (organizer.isEmpty()) {
            organizerEditText.setError(
                    "Enter organizer"
            );
            organizerEditText.requestFocus();
            return;
        }

        if (maxText.isEmpty()) {
            maxRegistrationsEditText.setError(
                    "Enter maximum registrations"
            );
            maxRegistrationsEditText.requestFocus();
            return;
        }

        int maxRegistrations;

        try {

            maxRegistrations =
                    Integer.parseInt(maxText);

        } catch (NumberFormatException e) {

            maxRegistrationsEditText.setError(
                    "Enter a valid number"
            );

            maxRegistrationsEditText.requestFocus();

            return;
        }

        if (maxRegistrations <= 0) {

            maxRegistrationsEditText.setError(
                    "Capacity must be greater than 0"
            );

            maxRegistrationsEditText.requestFocus();

            return;
        }

        // -------------------------------------------------
        // CHECK EVENT ID
        // -------------------------------------------------

        if (eventId == -1) {

            Toast.makeText(
                    this,
                    "Invalid event",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // -------------------------------------------------
        // UPDATE DATABASE
        // -------------------------------------------------

        boolean updated =
                databaseHelper.updateEvent(
                        eventId,
                        eventName,
                        description,
                        date,
                        time,
                        venue,
                        organizer,
                        maxRegistrations
                );

        // -------------------------------------------------
        // RESULT
        // -------------------------------------------------

        if (updated) {

            Toast.makeText(
                    this,
                    "Event updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to update event",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}