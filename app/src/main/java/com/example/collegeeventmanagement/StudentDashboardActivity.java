package com.example.collegeeventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    TextView studentNameTextView;
    TextView upcomingEventsCount;

    ImageView notificationIcon;

    Button browseEventsButton;
    Button myEventsButton;
    Button notificationsButton;

    LinearLayout navHome;
    LinearLayout navEvents;
    LinearLayout navMyEvents;
    LinearLayout navProfile;

    DatabaseHelper databaseHelper;

    String userName;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_dashboard);


        // =========================
        // DATABASE
        // =========================

        databaseHelper = new DatabaseHelper(this);


        // =========================
        // CONNECT XML VIEWS
        // =========================

        studentNameTextView =
                findViewById(R.id.studentNameTextView);

        upcomingEventsCount =
                findViewById(R.id.upcomingEventsCount);

        notificationIcon =
                findViewById(R.id.notificationIcon);

        browseEventsButton =
                findViewById(R.id.browseEventsButton);

        myEventsButton =
                findViewById(R.id.myEventsButton);

        notificationsButton =
                findViewById(R.id.notificationsButton);

        navHome =
                findViewById(R.id.navHome);

        navEvents =
                findViewById(R.id.navEvents);

        navMyEvents =
                findViewById(R.id.navMyEvents);

        navProfile =
                findViewById(R.id.navProfile);


        // =========================
        // GET STUDENT DETAILS
        // =========================

        userName =
                getIntent().getStringExtra("USER_NAME");

        email =
                getIntent().getStringExtra("email");


        // =========================
        // SHOW STUDENT NAME
        // =========================

        if (userName != null &&
                !userName.isEmpty()) {

            studentNameTextView.setText(userName);

        } else {

            studentNameTextView.setText("Student");
        }


        // =========================
        // LOAD UPCOMING EVENT COUNT
        // =========================

        loadEventCount();


        // =========================
        // BROWSE EVENTS BUTTON
        // =========================

        browseEventsButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            StudentDashboardActivity.this,
                            BrowseEventsActivity.class
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
        // MY EVENTS BUTTON
        // =========================

        myEventsButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            StudentDashboardActivity.this,
                            MyEventsActivity.class
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

            // Notifications page will be connected later.

        });


        // =========================
        // NOTIFICATION ICON
        // =========================

        notificationIcon.setOnClickListener(v -> {

            // Notifications page will be connected later.

        });


        // =====================================================
        // REUSABLE STUDENT BOTTOM NAVIGATION
        // =====================================================

        StudentBottomNavHelper.setup(
                this,
                navHome,
                navEvents,
                navMyEvents,
                navProfile,
                userName,
                email
        );
    }


    // =====================================================
    // LOAD UPCOMING EVENT COUNT
    // =====================================================

    private void loadEventCount() {

        int upcomingCount =
                databaseHelper.getUpcomingEventCount();

        upcomingEventsCount.setText(
                String.valueOf(upcomingCount)
        );
    }


    // =====================================================
    // REFRESH WHEN RETURNING TO DASHBOARD
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                upcomingEventsCount != null) {

            loadEventCount();
        }
    }
}