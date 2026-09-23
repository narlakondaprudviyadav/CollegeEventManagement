package com.example.collegeeventmanagement;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BrowseEventsActivity extends AppCompatActivity {

    private LinearLayout eventsContainer;
    private ImageButton backButton;
    private DatabaseHelper databaseHelper;

    private String studentEmail;
    private String studentName;

    // =========================
    // COLORS
    // =========================

    private final int PURPLE = Color.rgb(107, 77, 181);
    private final int GREEN = Color.rgb(52, 168, 83);
    private final int RED = Color.rgb(229, 57, 53);

    private final int DARK_TEXT = Color.rgb(42, 35, 52);
    private final int GRAY_TEXT = Color.rgb(100, 96, 105);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse_events);

        // =========================
        // CONNECT VIEWS
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

        backButton.setImageResource(
                R.drawable.ic_arrow_back
        );

        backButton.setImageTintList(
                ColorStateList.valueOf(Color.WHITE)
        );

        backButton.setVisibility(View.VISIBLE);
        backButton.setAlpha(1.0f);
        backButton.setClickable(true);
        backButton.setFocusable(true);

        backButton.setOnClickListener(v ->
                goToStudentDashboard()
        );

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
        // LOAD EVENTS
        // =========================

        loadEvents();
    }


    // =====================================================
    // GO TO STUDENT DASHBOARD
    // =====================================================

    private void goToStudentDashboard() {

        Intent intent =
                new Intent(
                        BrowseEventsActivity.this,
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
    // REFRESH EVENTS
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (eventsContainer != null &&
                databaseHelper != null) {

            loadEvents();
        }
    }


    // =====================================================
    // LOAD EVENTS
    // =====================================================

    private void loadEvents() {

        eventsContainer.removeAllViews();

        Cursor cursor =
                databaseHelper.getAllEvents();

        if (cursor == null ||
                cursor.getCount() == 0) {

            showNoEvents();

            if (cursor != null) {
                cursor.close();
            }

            return;
        }

        while (cursor.moveToNext()) {

            int eventId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "event_id"
                            )
                    );

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

            String eventDate =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "event_date"
                            )
                    );

            String eventTime =
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

            int registeredCount =
                    databaseHelper.getEventRegistrationCount(
                            eventId
                    );

            int maxRegistrations =
                    databaseHelper.getEventMaxRegistrations(
                            eventId
                    );

            addEventCard(
                    eventId,
                    eventName,
                    description,
                    eventDate,
                    eventTime,
                    venue,
                    organizer,
                    registeredCount,
                    maxRegistrations
            );
        }

        cursor.close();
    }


    // =====================================================
    // CREATE EVENT CARD
    // =====================================================

    private void addEventCard(
            int eventId,
            String eventName,
            String description,
            String eventDate,
            String eventTime,
            String venue,
            String organizer,
            int registeredCount,
            int maxRegistrations) {

        // =========================
        // CARD
        // =========================

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

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
                Color.rgb(232, 226, 243)
        );

        card.setBackground(
                cardBackground
        );

        card.setElevation(
                dp(3)
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                dp(8),
                dp(6),
                dp(8),
                dp(8)
        );

        card.setLayoutParams(
                cardParams
        );


        // =========================
        // HEADER
        // =========================

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView nameView =
                new TextView(this);

        nameView.setText(eventName);
        nameView.setTextSize(18);
        nameView.setTextColor(DARK_TEXT);
        nameView.setTypeface(
                null,
                Typeface.BOLD
        );

        nameView.setMaxLines(2);

        LinearLayout.LayoutParams nameParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        nameView.setLayoutParams(
                nameParams
        );

        header.addView(nameView);


        // =========================
        // STATUS
        // =========================

        String eventStatus =
                getEventStatus(
                        eventDate,
                        eventTime
                );

        LinearLayout statusPill =
                createStatusPill(
                        eventStatus
                );

        header.addView(statusPill);

        card.addView(header);


        // =========================
        // DESCRIPTION
        // =========================

        TextView descriptionView =
                new TextView(this);

        descriptionView.setText(
                description
        );

        descriptionView.setTextSize(13);
        descriptionView.setTextColor(GRAY_TEXT);
        descriptionView.setMaxLines(3);

        descriptionView.setPadding(
                0,
                dp(8),
                0,
                dp(10)
        );

        card.addView(descriptionView);


        // =========================
        // EVENT DETAILS
        // =========================

        LinearLayout detailsLayout =
                new LinearLayout(this);

        detailsLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        detailsLayout.addView(
                createDetailText(
                        "📅  " + eventDate
                )
        );

        detailsLayout.addView(
                createDetailText(
                        "🕐  " + eventTime
                )
        );

        detailsLayout.addView(
                createDetailText(
                        "📍  " + venue
                )
        );

        detailsLayout.addView(
                createDetailText(
                        "👤  " + organizer
                )
        );

        card.addView(detailsLayout);


        // =========================
        // REGISTRATION HEADER
        // =========================

        LinearLayout registrationHeader =
                new LinearLayout(this);

        registrationHeader.setOrientation(
                LinearLayout.HORIZONTAL
        );

        registrationHeader.setGravity(
                Gravity.CENTER_VERTICAL
        );

        LinearLayout.LayoutParams registrationHeaderParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        registrationHeaderParams.setMargins(
                0,
                dp(12),
                0,
                dp(4)
        );

        registrationHeader.setLayoutParams(
                registrationHeaderParams
        );

        TextView registrationTitle =
                new TextView(this);

        registrationTitle.setText(
                "Registration"
        );

        registrationTitle.setTextSize(13);
        registrationTitle.setTextColor(DARK_TEXT);

        registrationTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        registrationTitle.setLayoutParams(
                titleParams
        );

        registrationHeader.addView(
                registrationTitle
        );

        TextView registrationCountText =
                new TextView(this);

        registrationCountText.setText(
                registeredCount +
                        " / " +
                        maxRegistrations
        );

        registrationCountText.setTextSize(13);
        registrationCountText.setTextColor(PURPLE);

        registrationCountText.setTypeface(
                null,
                Typeface.BOLD
        );

        registrationHeader.addView(
                registrationCountText
        );

        card.addView(
                registrationHeader
        );


        // =========================
        // PROGRESS BAR
        // =========================

        LinearLayout progressBackground =
                new LinearLayout(this);

        progressBackground.setOrientation(
                LinearLayout.HORIZONTAL
        );

        GradientDrawable progressBg =
                new GradientDrawable();

        progressBg.setColor(
                Color.rgb(232, 228, 240)
        );

        progressBg.setCornerRadius(
                dp(6)
        );

        progressBackground.setBackground(
                progressBg
        );

        LinearLayout.LayoutParams progressParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(7)
                );

        progressBackground.setLayoutParams(
                progressParams
        );

        float percentage = 0;

        if (maxRegistrations > 0) {

            percentage =
                    (float) registeredCount /
                            maxRegistrations;

            if (percentage > 1) {
                percentage = 1;
            }
        }

        View progressFill =
                new View(this);

        LinearLayout.LayoutParams fillParams;

        if (registeredCount > 0) {

            fillParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            percentage
                    );

        } else {

            fillParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0
                    );
        }

        progressFill.setLayoutParams(
                fillParams
        );

        GradientDrawable fillBackground =
                new GradientDrawable();

        fillBackground.setCornerRadius(
                dp(6)
        );

        if (registeredCount >= maxRegistrations) {

            fillBackground.setColor(RED);

        } else {

            fillBackground.setColor(PURPLE);
        }

        progressFill.setBackground(
                fillBackground
        );

        progressBackground.addView(
                progressFill
        );

        card.addView(
                progressBackground
        );


        // =========================
        // SPOTS LEFT
        // =========================

        TextView spotsText =
                new TextView(this);

        int spotsLeft =
                maxRegistrations -
                        registeredCount;

        if (spotsLeft < 0) {
            spotsLeft = 0;
        }

        if (registeredCount >= maxRegistrations) {

            spotsText.setText(
                    "FULL • No spots left"
            );

            spotsText.setTextColor(RED);

        } else {

            spotsText.setText(
                    spotsLeft +
                            " spot" +
                            (spotsLeft == 1
                                    ? ""
                                    : "s") +
                            " left"
            );

            spotsText.setTextColor(
                    GRAY_TEXT
            );
        }

        spotsText.setTextSize(11);

        spotsText.setGravity(
                Gravity.END
        );

        spotsText.setPadding(
                0,
                dp(3),
                0,
                0
        );

        card.addView(spotsText);


        // =========================
        // REGISTER BUTTON
        // =========================

        Button registerButton =
                new Button(this);

        registerButton.setTextSize(14);
        registerButton.setAllCaps(false);
        registerButton.setTextColor(Color.WHITE);

        registerButton.setTypeface(
                null,
                Typeface.BOLD
        );

        registerButton.setGravity(
                Gravity.CENTER
        );

        registerButton.setPadding(
                dp(10),
                0,
                dp(10),
                0
        );

        registerButton.setMinHeight(0);
        registerButton.setMinimumHeight(0);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(44)
                );

        buttonParams.setMargins(
                0,
                dp(10),
                0,
                0
        );

        registerButton.setLayoutParams(
                buttonParams
        );

        registerButton.setBackgroundTintList(null);
        registerButton.setStateListAnimator(null);


        // =========================
        // CHECK REGISTRATION
        // =========================

        boolean alreadyRegistered =
                databaseHelper.isStudentRegistered(
                        eventId,
                        studentEmail
                );

        boolean eventFull =
                registeredCount >= maxRegistrations;


        // =========================
        // COMPLETED EVENT
        // =========================

        if (eventStatus.equals("COMPLETED")) {

            registerButton.setText(
                    "EVENT ENDED"
            );

            setButtonBackground(
                    registerButton,
                    "#9E9E9E"
            );

            registerButton.setEnabled(false);
            registerButton.setClickable(false);
        }


        // =========================
        // ALREADY REGISTERED
        // =========================

        else if (alreadyRegistered) {

            registerButton.setText(
                    "✓  REGISTERED"
            );

            setButtonBackground(
                    registerButton,
                    "#34A853"
            );

            registerButton.setEnabled(false);
        }


        // =========================
        // FULL
        // =========================

        else if (eventFull) {

            registerButton.setText(
                    "FULL"
            );

            setButtonBackground(
                    registerButton,
                    "#E53935"
            );

            registerButton.setEnabled(false);
        }


        // =========================
        // AVAILABLE
        // =========================

        else {

            registerButton.setText(
                    "REGISTER NOW"
            );

            setButtonBackground(
                    registerButton,
                    "#6B4DB5"
            );

            registerButton.setEnabled(true);
            registerButton.setClickable(true);


            registerButton.setOnClickListener(v -> {

                // =========================
                // RECHECK EVENT STATUS
                // =========================

                String currentStatus =
                        getEventStatus(
                                eventDate,
                                eventTime
                        );

                if (currentStatus.equals(
                        "COMPLETED"
                )) {

                    registerButton.setText(
                            "EVENT ENDED"
                    );

                    setButtonBackground(
                            registerButton,
                            "#9E9E9E"
                    );

                    registerButton.setEnabled(false);
                    registerButton.setClickable(false);

                    return;
                }


                // =========================
                // REGISTER STUDENT
                // =========================

                boolean success =
                        databaseHelper.registerForEvent(
                                eventId,
                                studentName,
                                studentEmail
                        );


                // =========================
                // SUCCESS
                // =========================

                if (success) {

                    // =========================
                    // SCHEDULE REMINDER
                    // =========================

                    EventReminderScheduler.scheduleReminder(
                            BrowseEventsActivity.this,
                            eventId,
                            eventName,
                            eventDate,
                            eventTime,
                            venue
                    );


                    // =========================
                    // UPDATE COUNT
                    // =========================

                    int updatedCount =
                            databaseHelper
                                    .getEventRegistrationCount(
                                            eventId
                                    );

                    registrationCountText.setText(
                            updatedCount +
                                    " / " +
                                    maxRegistrations
                    );


                    int updatedSpots =
                            maxRegistrations -
                                    updatedCount;

                    if (updatedSpots < 0) {
                        updatedSpots = 0;
                    }


                    // =========================
                    // CAPACITY REACHED
                    // =========================

                    if (updatedCount >=
                            maxRegistrations) {

                        registerButton.setText(
                                "FULL"
                        );

                        setButtonBackground(
                                registerButton,
                                "#E53935"
                        );

                        registerButton.setEnabled(
                                false
                        );

                        spotsText.setText(
                                "FULL • No spots left"
                        );

                        spotsText.setTextColor(
                                RED
                        );

                    }


                    // =========================
                    // REGISTERED
                    // =========================

                    else {

                        registerButton.setText(
                                "✓  REGISTERED"
                        );

                        setButtonBackground(
                                registerButton,
                                "#34A853"
                        );

                        registerButton.setEnabled(
                                false
                        );

                        spotsText.setText(
                                updatedSpots +
                                        " spot" +
                                        (updatedSpots == 1
                                                ? ""
                                                : "s") +
                                        " left"
                        );
                    }
                }


                // =========================
                // REGISTRATION FAILED
                // =========================

                else {

                    boolean nowRegistered =
                            databaseHelper
                                    .isStudentRegistered(
                                            eventId,
                                            studentEmail
                                    );

                    int currentCount =
                            databaseHelper
                                    .getEventRegistrationCount(
                                            eventId
                                    );

                    registrationCountText.setText(
                            currentCount +
                                    " / " +
                                    maxRegistrations
                    );


                    if (nowRegistered) {

                        registerButton.setText(
                                "✓  REGISTERED"
                        );

                        setButtonBackground(
                                registerButton,
                                "#34A853"
                        );

                        registerButton.setEnabled(
                                false
                        );

                    } else if (
                            currentCount >=
                                    maxRegistrations
                    ) {

                        registerButton.setText(
                                "FULL"
                        );

                        setButtonBackground(
                                registerButton,
                                "#E53935"
                        );

                        registerButton.setEnabled(
                                false
                        );
                    }
                }
            });
        }


        // =========================
        // ADD BUTTON
        // =========================

        card.addView(
                registerButton
        );


        // =========================
        // ADD CARD
        // =========================

        eventsContainer.addView(
                card
        );
    }


    // =====================================================
    // STATUS PILL
    // =====================================================

    private LinearLayout createStatusPill(
            String status) {

        LinearLayout pill =
                new LinearLayout(this);

        pill.setOrientation(
                LinearLayout.HORIZONTAL
        );

        pill.setGravity(
                Gravity.CENTER_VERTICAL
        );

        pill.setPadding(
                dp(8),
                dp(4),
                dp(8),
                dp(4)
        );


        GradientDrawable pillBackground =
                new GradientDrawable();

        pillBackground.setCornerRadius(
                dp(20)
        );


        // =========================
        // DOT
        // =========================

        View dot =
                new View(this);

        LinearLayout.LayoutParams dotParams =
                new LinearLayout.LayoutParams(
                        dp(7),
                        dp(7)
                );

        dotParams.setMargins(
                0,
                0,
                dp(5),
                0
        );

        dot.setLayoutParams(
                dotParams
        );


        GradientDrawable dotBackground =
                new GradientDrawable();

        dotBackground.setShape(
                GradientDrawable.OVAL
        );


        // =========================
        // STATUS TEXT
        // =========================

        TextView text =
                new TextView(this);

        text.setText(status);
        text.setTextSize(10);

        text.setTypeface(
                null,
                Typeface.BOLD
        );


        // =========================
        // UPCOMING
        // =========================

        if (status.equals("UPCOMING")) {

            pillBackground.setColor(
                    Color.rgb(
                            232,
                            247,
                            235
                    )
            );

            dotBackground.setColor(
                    GREEN
            );

            text.setTextColor(
                    GREEN
            );

            try {

                Animation pulseAnimation =
                        AnimationUtils.loadAnimation(
                                this,
                                R.anim.pulse_dot
                        );

                dot.startAnimation(
                        pulseAnimation
                );

            } catch (Exception e) {

                // Animation is optional
            }
        }


        // =========================
        // COMPLETED
        // =========================

        else {

            pillBackground.setColor(
                    Color.rgb(
                            239,
                            239,
                            239
                    )
            );

            dotBackground.setColor(
                    Color.rgb(
                            145,
                            145,
                            145
                    )
            );

            text.setTextColor(
                    Color.rgb(
                            105,
                            105,
                            105
                    )
            );

            dot.clearAnimation();
        }


        // =========================
        // FINISH STATUS PILL
        // =========================

        dot.setBackground(
                dotBackground
        );

        pill.setBackground(
                pillBackground
        );

        pill.addView(
                dot
        );

        pill.addView(
                text
        );

        return pill;
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

            SimpleDateFormat format =
                    new SimpleDateFormat(
                            "d/M/yyyy HH:mm",
                            Locale.getDefault()
                    );

            format.setLenient(false);

            Date eventDateTimeObject =
                    format.parse(
                            eventDateTime
                    );

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
    // DETAIL TEXT
    // =====================================================

    private TextView createDetailText(
            String text) {

        TextView textView =
                new TextView(this);

        textView.setText(text);

        textView.setTextSize(12);

        textView.setTextColor(
                GRAY_TEXT
        );

        textView.setPadding(
                0,
                dp(2),
                0,
                dp(2)
        );

        return textView;
    }


    // =====================================================
    // BUTTON BACKGROUND
    // =====================================================

    private void setButtonBackground(
            Button button,
            String color) {

        GradientDrawable background =
                new GradientDrawable();

        background.setShape(
                GradientDrawable.RECTANGLE
        );

        background.setColor(
                Color.parseColor(color)
        );

        background.setCornerRadius(
                dp(10)
        );

        button.setBackground(
                background
        );

        button.setBackgroundTintList(
                null
        );

        button.setTextColor(
                Color.WHITE
        );

        button.setTextSize(14);

        button.setTypeface(
                null,
                Typeface.BOLD
        );

        button.setGravity(
                Gravity.CENTER
        );

        button.setAlpha(1.0f);

        button.setElevation(
                dp(2)
        );
    }


    // =====================================================
    // NO EVENTS
    // =====================================================

    private void showNoEvents() {

        TextView noEvents =
                new TextView(this);

        noEvents.setText(
                "🎉\n\nNo events available yet.\nCheck back soon!"
        );

        noEvents.setTextSize(17);

        noEvents.setTextColor(
                GRAY_TEXT
        );

        noEvents.setGravity(
                Gravity.CENTER
        );

        noEvents.setPadding(
                dp(20),
                dp(80),
                dp(20),
                dp(20)
        );

        eventsContainer.addView(
                noEvents
        );
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