package com.example.collegeeventmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    EditText eventNameEditText;
    EditText eventDescriptionEditText;
    EditText eventDateEditText;
    EditText eventTimeEditText;
    EditText eventVenueEditText;
    EditText eventOrganizerEditText;
    EditText maxRegistrationsEditText;

    Button createEventButton;
    ImageButton backButton;

    DatabaseHelper databaseHelper;

    String userName;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper = new DatabaseHelper(this);


        // =====================================================
        // GET VIEWS
        // =====================================================

        eventNameEditText =
                findViewById(R.id.eventNameEditText);

        eventDescriptionEditText =
                findViewById(R.id.eventDescriptionEditText);

        eventDateEditText =
                findViewById(R.id.eventDateEditText);

        eventTimeEditText =
                findViewById(R.id.eventTimeEditText);

        eventVenueEditText =
                findViewById(R.id.eventVenueEditText);

        eventOrganizerEditText =
                findViewById(R.id.eventOrganizerEditText);

        maxRegistrationsEditText =
                findViewById(R.id.maxRegistrationsEditText);

        createEventButton =
                findViewById(R.id.createEventButton);

        backButton =
                findViewById(R.id.backButton);


        // =====================================================
        // GET ADMIN DETAILS
        // =====================================================

        userName =
                getIntent().getStringExtra("USER_NAME");

        email =
                getIntent().getStringExtra("email");


        // =====================================================
        // BACK ARROW BUTTON
        // =====================================================

        backButton.setOnClickListener(v -> {
            finish();
        });


        // =====================================================
        // DATE PICKER
        // =====================================================

        eventDateEditText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year =
                    calendar.get(Calendar.YEAR);

            int month =
                    calendar.get(Calendar.MONTH);

            int day =
                    calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            AddEventActivity.this,

                            (view, selectedYear,
                             selectedMonth, selectedDay) -> {

                                String selectedDate =
                                        selectedDay + "/" +
                                                (selectedMonth + 1) + "/" +
                                                selectedYear;

                                eventDateEditText.setText(
                                        selectedDate
                                );
                            },

                            year,
                            month,
                            day
                    );

            datePickerDialog.show();
        });


        // =====================================================
        // TIME PICKER
        // =====================================================

        eventTimeEditText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int hour =
                    calendar.get(Calendar.HOUR_OF_DAY);

            int minute =
                    calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog =
                    new TimePickerDialog(
                            AddEventActivity.this,

                            (view, selectedHour,
                             selectedMinute) -> {

                                String selectedTime =
                                        String.format(
                                                Locale.getDefault(),
                                                "%02d:%02d",
                                                selectedHour,
                                                selectedMinute
                                        );

                                eventTimeEditText.setText(
                                        selectedTime
                                );
                            },

                            hour,
                            minute,
                            true
                    );

            timePickerDialog.show();
        });


        // =====================================================
        // CREATE EVENT BUTTON
        // =====================================================

        createEventButton.setOnClickListener(v -> {

            String eventName =
                    eventNameEditText
                            .getText()
                            .toString()
                            .trim();

            String description =
                    eventDescriptionEditText
                            .getText()
                            .toString()
                            .trim();

            String date =
                    eventDateEditText
                            .getText()
                            .toString()
                            .trim();

            String time =
                    eventTimeEditText
                            .getText()
                            .toString()
                            .trim();

            String venue =
                    eventVenueEditText
                            .getText()
                            .toString()
                            .trim();

            String organizer =
                    eventOrganizerEditText
                            .getText()
                            .toString()
                            .trim();

            String maxRegistrationsText =
                    maxRegistrationsEditText
                            .getText()
                            .toString()
                            .trim();


            // =================================================
            // VALIDATION
            // =================================================

            if (eventName.isEmpty()) {

                eventNameEditText.setError(
                        "Enter event name"
                );

                eventNameEditText.requestFocus();

                return;
            }


            if (description.isEmpty()) {

                eventDescriptionEditText.setError(
                        "Enter event description"
                );

                eventDescriptionEditText.requestFocus();

                return;
            }


            if (date.isEmpty()) {

                eventDateEditText.setError(
                        "Select event date"
                );

                eventDateEditText.requestFocus();

                return;
            }


            if (time.isEmpty()) {

                eventTimeEditText.setError(
                        "Select event time"
                );

                eventTimeEditText.requestFocus();

                return;
            }


            if (venue.isEmpty()) {

                eventVenueEditText.setError(
                        "Enter venue"
                );

                eventVenueEditText.requestFocus();

                return;
            }


            if (organizer.isEmpty()) {

                eventOrganizerEditText.setError(
                        "Enter organizer name"
                );

                eventOrganizerEditText.requestFocus();

                return;
            }


            if (maxRegistrationsText.isEmpty()) {

                maxRegistrationsEditText.setError(
                        "Enter maximum registrations"
                );

                maxRegistrationsEditText.requestFocus();

                return;
            }


            // =================================================
            // CONVERT CAPACITY TO INTEGER
            // =================================================

            int maxRegistrations;

            try {

                maxRegistrations =
                        Integer.parseInt(
                                maxRegistrationsText
                        );

            } catch (NumberFormatException e) {

                maxRegistrationsEditText.setError(
                        "Enter a valid number"
                );

                maxRegistrationsEditText.requestFocus();

                return;
            }


            // =================================================
            // CHECK CAPACITY
            // =================================================

            if (maxRegistrations <= 0) {

                maxRegistrationsEditText.setError(
                        "Maximum registrations must be greater than 0"
                );

                maxRegistrationsEditText.requestFocus();

                return;
            }


            // =================================================
            // SAVE EVENT
            // =================================================

            boolean inserted =
                    databaseHelper.insertEvent(
                            eventName,
                            description,
                            date,
                            time,
                            venue,
                            organizer,
                            maxRegistrations
                    );


            // =================================================
            // SUCCESS
            // =================================================

            if (inserted) {

                Toast.makeText(
                        AddEventActivity.this,
                        "Event created successfully",
                        Toast.LENGTH_SHORT
                ).show();


                // =============================================
                // RETURN TO ADMIN DASHBOARD
                // =============================================

                Intent intent =
                        new Intent(
                                AddEventActivity.this,
                                AdminDashboardActivity.class
                        );

                intent.putExtra(
                        "USER_NAME",
                        userName
                );

                intent.putExtra(
                        "email",
                        email
                );

                intent.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP
                );

                startActivity(intent);

                finish();

            } else {

                Toast.makeText(
                        AddEventActivity.this,
                        "Failed to create event",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        // =====================================================
        // PHONE BACK BUTTON
        // =====================================================

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                }
        );
    }
}