package com.example.collegeeventmanagement;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Reusable bottom navigation for all Student screens.
 *
 * Navigation:
 * Home      -> StudentDashboardActivity
 * Events    -> BrowseEventsActivity
 * My Events -> MyEventsActivity
 * Profile   -> ProfileActivity
 */
public class StudentBottomNavHelper {

    private StudentBottomNavHelper() {
        // Prevent creating objects of this helper class.
    }

    public static void setup(
            Activity activity,
            View navHome,
            View navEvents,
            View navMyEvents,
            View navProfile,
            String studentName,
            String studentEmail
    ) {

        // =====================================================
        // HOME
        // =====================================================

        navHome.setOnClickListener(v -> {

            if (activity instanceof StudentDashboardActivity) {
                return;
            }

            Intent intent = new Intent(
                    activity,
                    StudentDashboardActivity.class
            );

            putStudentDetails(
                    intent,
                    studentName,
                    studentEmail
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            activity.startActivity(intent);
        });


        // =====================================================
        // EVENTS
        // =====================================================

        navEvents.setOnClickListener(v -> {

            if (activity instanceof BrowseEventsActivity) {
                return;
            }

            Intent intent = new Intent(
                    activity,
                    BrowseEventsActivity.class
            );

            putStudentDetails(
                    intent,
                    studentName,
                    studentEmail
            );

            activity.startActivity(intent);
        });


        // =====================================================
        // MY EVENTS
        // =====================================================

        navMyEvents.setOnClickListener(v -> {

            if (activity instanceof MyEventsActivity) {
                return;
            }

            Intent intent = new Intent(
                    activity,
                    MyEventsActivity.class
            );

            putStudentDetails(
                    intent,
                    studentName,
                    studentEmail
            );

            activity.startActivity(intent);
        });


        // =====================================================
        // PROFILE
        // =====================================================

        navProfile.setOnClickListener(v -> {

            if (activity instanceof ProfileActivity) {
                return;
            }

            Intent intent = new Intent(
                    activity,
                    ProfileActivity.class
            );

            putStudentDetails(
                    intent,
                    studentName,
                    studentEmail
            );

            activity.startActivity(intent);
        });
    }


    // =====================================================
    // PASS STUDENT DETAILS
    // =====================================================

    private static void putStudentDetails(
            Intent intent,
            String studentName,
            String studentEmail
    ) {

        intent.putExtra(
                "USER_NAME",
                studentName
        );

        intent.putExtra(
                "email",
                studentEmail
        );
    }
}