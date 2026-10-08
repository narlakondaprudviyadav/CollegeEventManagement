package com.example.collegeeventmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationsActivity extends AppCompatActivity {

    private ImageButton backButton;

    private TextView clearAllTextView;

    private ScrollView notificationsScrollView;
    private LinearLayout notificationsContainer;
    private LinearLayout emptyNotificationView;

    private LinearLayout navHome;
    private LinearLayout navEvents;
    private LinearLayout navMyEvents;
    private LinearLayout navProfile;

    private String userName;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notifications);

        // Get student details
        userName = getIntent().getStringExtra("USER_NAME");
        email = getIntent().getStringExtra("email");

        if (userName == null || userName.trim().isEmpty()) {
            userName = "Student";
        }

        if (email == null) {
            email = "";
        }

        // Find views
        backButton = findViewById(R.id.backButton);
        clearAllTextView = findViewById(R.id.clearAllTextView);

        notificationsScrollView =
                findViewById(R.id.notificationsScrollView);

        notificationsContainer =
                findViewById(R.id.notificationsContainer);

        emptyNotificationView =
                findViewById(R.id.emptyNotificationView);

        navHome = findViewById(R.id.navHome);
        navEvents = findViewById(R.id.navEvents);
        navMyEvents = findViewById(R.id.navMyEvents);
        navProfile = findViewById(R.id.navProfile);

        // -------------------------------------------------
        // BACK BUTTON
        // -------------------------------------------------

        backButton.setOnClickListener(v -> finish());

        // Android system back
        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                }
        );

        // -------------------------------------------------
        // CLEAR ALL NOTIFICATIONS
        // -------------------------------------------------

        clearAllTextView.setOnClickListener(v -> {

            if (notificationsContainer.getChildCount() == 0) {

                Toast.makeText(
                        NotificationsActivity.this,
                        "No notifications to clear",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Remove all notification cards
            notificationsContainer.removeAllViews();

            // Hide notification list
            notificationsScrollView.setVisibility(View.GONE);

            // Show empty state
            emptyNotificationView.setVisibility(View.VISIBLE);

            Toast.makeText(
                    NotificationsActivity.this,
                    "All notifications cleared",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -------------------------------------------------
        // STUDENT BOTTOM NAVIGATION
        // -------------------------------------------------

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
}