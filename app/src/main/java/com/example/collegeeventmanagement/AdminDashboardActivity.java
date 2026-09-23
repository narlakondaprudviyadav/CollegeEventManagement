package com.example.collegeeventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView adminNameTextView;
    private TextView totalEventsCount;
    private TextView totalRegistrationsCount;

    private ImageView notificationIcon;

    private Button addEventButton;
    private Button manageEventsButton;
    private Button registrationsButton;
    private Button notificationsButton;

    // Using View avoids XML type-casting problems
    private View navHome;
    private View navEvents;
    private View navRegistrations;
    private View navProfile;

    private DatabaseHelper databaseHelper;

    private String userName;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_dashboard);


        // =========================
        // DATABASE
        // =========================

        databaseHelper = new DatabaseHelper(this);


        // =========================
        // FIND VIEWS
        // =========================

        adminNameTextView =
                findViewById(R.id.adminNameTextView);

        totalEventsCount =
                findViewById(R.id.totalEventsCount);

        totalRegistrationsCount =
                findViewById(R.id.totalRegistrationsCount);

        notificationIcon =
                findViewById(R.id.notificationIcon);

        addEventButton =
                findViewById(R.id.addEventButton);

        manageEventsButton =
                findViewById(R.id.manageEventsButton);

        registrationsButton =
                findViewById(R.id.registrationsButton);

        notificationsButton =
                findViewById(R.id.notificationsButton);


        // =========================
        // BOTTOM NAVIGATION
        // =========================

        navHome =
                findViewById(R.id.navHome);

        navEvents =
                findViewById(R.id.navEvents);

        navRegistrations =
                findViewById(R.id.navRegistrations);

        navProfile =
                findViewById(R.id.navProfile);


        // =========================
        // GET ADMIN DETAILS
        // =========================

        userName =
                getIntent().getStringExtra("USER_NAME");

        email =
                getIntent().getStringExtra("email");


        // =========================
        // SHOW ADMIN NAME
        // =========================

        if (userName != null &&
                !userName.trim().isEmpty()) {

            adminNameTextView.setText(userName);

        } else {

            adminNameTextView.setText("Admin");
        }


        // =========================
        // LOAD DASHBOARD COUNTS
        // =========================

        loadDashboardCounts();


        // =========================
        // ADD EVENT
        // =========================

        addEventButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    AddEventActivity.class
            );

            intent.putExtra(
                    "USER_NAME",
                    userName
            );

            intent.putExtra(
                    "email",
                    email
            );

            startActivity(intent);
        });


        // =========================
        // MANAGE EVENTS
        // =========================

        manageEventsButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageEventsActivity.class
            );

            intent.putExtra(
                    "USER_NAME",
                    userName
            );

            intent.putExtra(
                    "email",
                    email
            );

            startActivity(intent);
        });


        // =========================
        // REGISTRATIONS
        // =========================

        registrationsButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    RegistrationsActivity.class
            );

            intent.putExtra(
                    "USER_NAME",
                    userName
            );

            intent.putExtra(
                    "email",
                    email
            );

            startActivity(intent);
        });


        // =========================
        // NOTIFICATIONS BUTTON
        // =========================

        notificationsButton.setOnClickListener(v -> {

            // Notifications page will be connected later

        });


        // =========================
        // NOTIFICATION ICON
        // =========================

        notificationIcon.setOnClickListener(v -> {

            // Notifications page will be connected later

        });


        // =========================
        // HOME
        // =========================

        navHome.setOnClickListener(v -> {

            // Already on Admin Dashboard

        });


        // =========================
        // EVENTS
        // =========================

        navEvents.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageEventsActivity.class
            );

            intent.putExtra(
                    "USER_NAME",
                    userName
            );

            intent.putExtra(
                    "email",
                    email
            );

            startActivity(intent);
        });


        // =========================
        // REGISTRATIONS
        // =========================

        navRegistrations.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    RegistrationsActivity.class
            );

            intent.putExtra(
                    "USER_NAME",
                    userName
            );

            intent.putExtra(
                    "email",
                    email
            );

            startActivity(intent);
        });


        // =========================
        // PROFILE
        // =========================

        navProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ProfileActivity.class
            );

            intent.putExtra(
                    "USER_NAME",
                    userName
            );

            intent.putExtra(
                    "email",
                    email
            );

            startActivity(intent);
        });
    }


    // =====================================================
    // LOAD DASHBOARD COUNTS
    // =====================================================

    private void loadDashboardCounts() {

        if (databaseHelper == null) {
            return;
        }


        // =========================
        // TOTAL EVENTS
        // =========================

        int eventCount =
                databaseHelper.getEventCount();

        totalEventsCount.setText(
                String.valueOf(eventCount)
        );


        // =========================
        // TOTAL REGISTRATIONS
        // =========================

        int registrationCount =
                databaseHelper.getRegistrationCount();

        totalRegistrationsCount.setText(
                String.valueOf(registrationCount)
        );
    }


    // =====================================================
    // REFRESH COUNTS
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                totalEventsCount != null &&
                totalRegistrationsCount != null) {

            loadDashboardCounts();
        }
    }
}