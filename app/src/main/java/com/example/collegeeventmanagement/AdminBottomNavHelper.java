package com.example.collegeeventmanagement;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Reusable bottom navigation for all Admin screens.
 *
 * Navigation:
 * Home          -> AdminDashboardActivity
 * Events        -> ManageEventsActivity
 * Registrations -> RegistrationsActivity
 * Profile       -> ProfileActivity
 */
public class AdminBottomNavHelper {

    private AdminBottomNavHelper() {
        // Prevent creating objects of this helper class.
    }


    public static void setup(
            Activity activity,
            View navHome,
            View navEvents,
            View navRegistrations,
            View navProfile,
            String adminName,
            String adminEmail
    ) {


        // =====================================================
        // HOME
        // =====================================================

        navHome.setOnClickListener(v -> {

            if (activity instanceof AdminDashboardActivity) {
                return;
            }


            Intent intent =
                    new Intent(
                            activity,
                            AdminDashboardActivity.class
                    );


            putAdminDetails(
                    intent,
                    adminName,
                    adminEmail
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

            if (activity instanceof ManageEventsActivity) {
                return;
            }


            Intent intent =
                    new Intent(
                            activity,
                            ManageEventsActivity.class
                    );


            putAdminDetails(
                    intent,
                    adminName,
                    adminEmail
            );


            activity.startActivity(intent);
        });


        // =====================================================
        // REGISTRATIONS
        // =====================================================

        navRegistrations.setOnClickListener(v -> {

            if (activity instanceof RegistrationsActivity) {
                return;
            }


            Intent intent =
                    new Intent(
                            activity,
                            RegistrationsActivity.class
                    );


            putAdminDetails(
                    intent,
                    adminName,
                    adminEmail
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


            Intent intent =
                    new Intent(
                            activity,
                            ProfileActivity.class
                    );


            putAdminDetails(
                    intent,
                    adminName,
                    adminEmail
            );


            activity.startActivity(intent);
        });
    }


    // =====================================================
    // PASS ADMIN DETAILS
    // =====================================================

    private static void putAdminDetails(
            Intent intent,
            String adminName,
            String adminEmail
    ) {

        intent.putExtra(
                "USER_NAME",
                adminName
        );


        intent.putExtra(
                "email",
                adminEmail
        );


        intent.putExtra(
                "USER_ROLE",
                "Admin"
        );
    }
}