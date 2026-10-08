package com.example.collegeeventmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationsActivity extends AppCompatActivity {

    // =====================================================
    // HEADER
    // =====================================================

    private ImageButton backButton;

    private View clearAllTextView;


    // =====================================================
    // NOTIFICATION AREA
    // =====================================================

    private LinearLayout notificationsContainer;

    /*
     * IMPORTANT:
     * emptyNotificationView is a LinearLayout in the XML.
     * We only need setVisibility(), so View is safest.
     */
    private View emptyNotificationView;


    // =====================================================
    // BOTTOM NAVIGATION
    // =====================================================

    private LinearLayout navHome;

    private LinearLayout navEvents;

    private LinearLayout navMyEvents;

    private LinearLayout navProfile;


    // =====================================================
    // USER DETAILS
    // =====================================================

    private String userName;

    private String email;


    // =====================================================
    // DATABASE
    // =====================================================

    private DatabaseHelper databaseHelper;


    // =====================================================
    // DATE FORMAT
    // =====================================================

    private final SimpleDateFormat notificationDateFormat =
            new SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
            );


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_notifications
        );


        // =================================================
        // GET USER DETAILS
        // =================================================

        userName =
                getIntent().getStringExtra(
                        "USER_NAME"
                );

        email =
                getIntent().getStringExtra(
                        "email"
                );


        if (userName == null ||
                userName.trim().isEmpty()) {

            userName = "Student";
        }


        if (email == null) {

            email = "";
        }


        // =================================================
        // FIND VIEWS
        // =================================================

        backButton =
                findViewById(
                        R.id.backButton
                );


        /*
         * This is a LinearLayout in your XML,
         * therefore use View.
         */
        clearAllTextView =
                findViewById(
                        R.id.clearAllTextView
                );


        notificationsContainer =
                findViewById(
                        R.id.notificationsContainer
                );


        /*
         * This is also a LinearLayout in your XML,
         * therefore use View.
         */
        emptyNotificationView =
                findViewById(
                        R.id.emptyNotificationView
                );


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


        // =================================================
        // DATABASE
        // =================================================

        databaseHelper =
                new DatabaseHelper(
                        this
                );


        // =================================================
        // BACK BUTTON
        // =================================================

        if (backButton != null) {

            backButton.setOnClickListener(
                    v -> finish()
            );
        }


        // =================================================
        // CLEAR ALL
        // =================================================

        if (clearAllTextView != null) {

            clearAllTextView.setOnClickListener(
                    v -> {

                        databaseHelper
                                .clearAllNotifications();

                        loadNotifications();
                    }
            );
        }


        // =================================================
        // STUDENT BOTTOM NAVIGATION
        // =================================================

        StudentBottomNavHelper.setup(
                this,
                navHome,
                navEvents,
                navMyEvents,
                navProfile,
                userName,
                email
        );


        // =================================================
        // LOAD NOTIFICATIONS
        // =================================================

        loadNotifications();


        // =================================================
        // SYSTEM BACK BUTTON
        // =================================================

        getOnBackPressedDispatcher()
                .addCallback(
                        this,
                        new OnBackPressedCallback(true) {

                            @Override
                            public void handleOnBackPressed() {

                                finish();
                            }
                        }
                );
    }


    // =====================================================
    // LOAD NOTIFICATIONS
    // =====================================================

    private void loadNotifications() {

        // -------------------------------------------------
        // CLEAR EXISTING CARDS
        // -------------------------------------------------

        notificationsContainer.removeAllViews();


        // -------------------------------------------------
        // GET DATABASE NOTIFICATIONS
        // -------------------------------------------------

        Cursor cursor =
                databaseHelper
                        .getAllNotifications();


        // -------------------------------------------------
        // NO NOTIFICATIONS
        // -------------------------------------------------

        if (cursor == null ||
                cursor.getCount() == 0) {

            if (cursor != null) {

                cursor.close();
            }

            showEmptyState();

            return;
        }


        // -------------------------------------------------
        // NOTIFICATIONS EXIST
        // -------------------------------------------------

        emptyNotificationView.setVisibility(
                View.GONE
        );


        if (clearAllTextView != null) {

            clearAllTextView.setVisibility(
                    View.VISIBLE
            );
        }


        // -------------------------------------------------
        // READ EVERY NOTIFICATION
        // -------------------------------------------------

        while (cursor.moveToNext()) {

            int notificationId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "notification_id"
                            )
                    );


            String title =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "title"
                            )
                    );


            String message =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "message"
                            )
                    );


            long timestamp =
                    cursor.getLong(
                            cursor.getColumnIndexOrThrow(
                                    "notification_timestamp"
                            )
                    );


            int isRead =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "is_read"
                            )
                    );


            addNotificationCard(
                    notificationId,
                    title,
                    message,
                    timestamp,
                    isRead
            );
        }


        cursor.close();
    }


    // =====================================================
    // EMPTY STATE
    // =====================================================

    private void showEmptyState() {

        emptyNotificationView.setVisibility(
                View.VISIBLE
        );


        if (clearAllTextView != null) {

            clearAllTextView.setVisibility(
                    View.GONE
            );
        }
    }


    // =====================================================
    // CREATE NOTIFICATION CARD
    // =====================================================

    private void addNotificationCard(
            int notificationId,
            String title,
            String message,
            long timestamp,
            int isRead) {


        // -------------------------------------------------
        // CARD
        // -------------------------------------------------

        LinearLayout card =
                new LinearLayout(
                        this
                );


        card.setOrientation(
                LinearLayout.VERTICAL
        );


        card.setPadding(
                dp(18),
                dp(16),
                dp(18),
                dp(16)
        );


        // -------------------------------------------------
        // CARD BACKGROUND
        // -------------------------------------------------

        GradientDrawable cardBackground =
                new GradientDrawable();


        cardBackground.setColor(
                Color.WHITE
        );


        cardBackground.setCornerRadius(
                dp(16)
        );


        cardBackground.setStroke(
                dp(1),
                Color.parseColor(
                        "#E8E2F2"
                )
        );


        card.setBackground(
                cardBackground
        );


        // -------------------------------------------------
        // CARD MARGINS
        // -------------------------------------------------

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );


        cardParams.setMargins(
                dp(16),
                dp(8),
                dp(16),
                dp(8)
        );


        card.setLayoutParams(
                cardParams
        );


        // =================================================
        // TITLE
        // =================================================

        TextView titleTextView =
                new TextView(
                        this
                );


        titleTextView.setText(
                title
        );


        titleTextView.setTextSize(
                17
        );


        titleTextView.setTextColor(
                Color.parseColor(
                        "#5B2A86"
                )
        );


        titleTextView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );


        titleTextView.setMaxLines(
                2
        );


        // =================================================
        // MESSAGE
        // =================================================

        TextView messageTextView =
                new TextView(
                        this
                );


        messageTextView.setText(
                message
        );


        messageTextView.setTextSize(
                14
        );


        messageTextView.setTextColor(
                Color.parseColor(
                        "#444444"
                )
        );


        messageTextView.setLineSpacing(
                0,
                1.15f
        );


        messageTextView.setPadding(
                0,
                dp(8),
                0,
                dp(8)
        );


        // =================================================
        // TIME
        // =================================================

        TextView timeTextView =
                new TextView(
                        this
                );


        String formattedTime =
                notificationDateFormat.format(
                        new Date(timestamp)
                );


        timeTextView.setText(
                formattedTime
        );


        timeTextView.setTextSize(
                12
        );


        timeTextView.setTextColor(
                Color.parseColor(
                        "#888888"
                )
        );


        // =================================================
        // ADD CONTENT
        // =================================================

        card.addView(
                titleTextView
        );


        card.addView(
                messageTextView
        );


        card.addView(
                timeTextView
        );


        // =================================================
        // READ / UNREAD
        // =================================================

        if (isRead == 1) {

            card.setAlpha(
                    0.72f
            );
        }


        // =================================================
        // CLICK CARD
        // =================================================

        card.setOnClickListener(
                v -> {

                    databaseHelper
                            .markNotificationAsRead(
                                    notificationId
                            );

                    card.setAlpha(
                            0.72f
                    );
                }
        );


        // =================================================
        // ADD CARD TO CONTAINER
        // =================================================

        notificationsContainer.addView(
                card
        );
    }


    // =====================================================
    // DP CONVERSION
    // =====================================================

    private int dp(int value) {

        float density =
                getResources()
                        .getDisplayMetrics()
                        .density;

        return Math.round(
                value * density
        );
    }


    // =====================================================
    // ON DESTROY
    // =====================================================

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {

            databaseHelper.close();
        }

        super.onDestroy();
    }
}