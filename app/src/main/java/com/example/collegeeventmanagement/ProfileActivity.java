package com.example.collegeeventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private Button logoutButton;
    private ImageButton backButton;

    // =====================================================
    // STUDENT BOTTOM NAVIGATION
    // =====================================================

    private LinearLayout navHome;
    private LinearLayout navEvents;
    private LinearLayout navMyEvents;
    private LinearLayout navProfile;

    private String userName;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_profile
        );


        // =====================================================
        // FIND VIEWS
        // =====================================================

        profileNameTextView =
                findViewById(
                        R.id.profileNameTextView
                );

        profileEmailTextView =
                findViewById(
                        R.id.profileEmailTextView
                );

        logoutButton =
                findViewById(
                        R.id.logoutButton
                );

        backButton =
                findViewById(
                        R.id.backButton
                );


        // =====================================================
        // BOTTOM NAVIGATION VIEWS
        // =====================================================

        navHome =
                findViewById(
                        R.id.navHome
                );

        navEvents =
                findViewById(
                        R.id.navEvents
                );

        navMyEvents =
                findViewById(
                        R.id.navMyEvents
                );

        navProfile =
                findViewById(
                        R.id.navProfile
                );


        // =====================================================
        // GET STUDENT DETAILS
        // =====================================================

        userName =
                getIntent().getStringExtra(
                        "USER_NAME"
                );

        email =
                getIntent().getStringExtra(
                        "email"
                );


        // =====================================================
        // DEFAULT VALUES
        // =====================================================

        if (userName == null ||
                userName.trim().isEmpty()) {

            userName = "Student";
        }


        if (email == null ||
                email.trim().isEmpty()) {

            email = "Email not available";
        }


        // =====================================================
        // DISPLAY DETAILS
        // =====================================================

        profileNameTextView.setText(
                userName
        );

        profileEmailTextView.setText(
                email
        );


        // =====================================================
        // SETUP STUDENT BOTTOM NAVIGATION
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


        // =====================================================
        // TOP BACK BUTTON
        // =====================================================

        backButton.setOnClickListener(v ->
                goToStudentDashboard()
        );


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
        // LOGOUT
        // =====================================================

        logoutButton.setOnClickListener(v ->
                logout()
        );
    }


    // =====================================================
    // GO TO STUDENT DASHBOARD
    // =====================================================

    private void goToStudentDashboard() {

        Intent intent =
                new Intent(
                        ProfileActivity.this,
                        StudentDashboardActivity.class
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
    }


    // =====================================================
    // LOGOUT
    // =====================================================

    private void logout() {

        Intent intent =
                new Intent(
                        ProfileActivity.this,
                        MainActivity.class
                );


        /*
         * Completely remove all previous activities.
         *
         * After logout:
         *
         * Profile
         * Student Dashboard
         * My Events
         * Browse Events
         *
         * cannot be returned to using Back.
         */

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );


        startActivity(intent);

        finish();
    }
}