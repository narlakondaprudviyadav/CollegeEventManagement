package com.example.collegeeventmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private Button logoutButton;
    private ImageButton backButton;
    private ImageView profileImageView;

    // =====================================================
    // STUDENT NAVIGATION
    // =====================================================

    private LinearLayout studentNavHome;
    private LinearLayout studentNavEvents;
    private LinearLayout studentNavMyEvents;
    private LinearLayout studentNavProfile;

    private LinearLayout studentBottomNavigation;

    // =====================================================
    // ADMIN NAVIGATION
    // =====================================================

    private LinearLayout adminNavHome;
    private LinearLayout adminNavEvents;
    private LinearLayout adminNavRegistrations;
    private LinearLayout adminNavProfile;

    private LinearLayout adminBottomNavigation;

    // =====================================================
    // USER DETAILS
    // =====================================================

    private String userName;
    private String email;
    private String userRole;

    private SharedPreferences preferences;

    // =====================================================
    // IMAGE PICKER
    // =====================================================

    private ActivityResultLauncher<String[]> imagePickerLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


        // =====================================================
        // FIND PROFILE VIEWS
        // =====================================================

        profileNameTextView =
                findViewById(R.id.profileNameTextView);

        profileEmailTextView =
                findViewById(R.id.profileEmailTextView);

        logoutButton =
                findViewById(R.id.logoutButton);

        backButton =
                findViewById(R.id.backButton);

        profileImageView =
                findViewById(R.id.profileImageView);


        // =====================================================
        // FIND STUDENT NAVIGATION
        // =====================================================

        studentBottomNavigation =
                findViewById(R.id.studentBottomNavigation);

        studentNavHome =
                findViewById(R.id.studentNavHome);

        studentNavEvents =
                findViewById(R.id.studentNavEvents);

        studentNavMyEvents =
                findViewById(R.id.studentNavMyEvents);

        studentNavProfile =
                findViewById(R.id.studentNavProfile);


        // =====================================================
        // FIND ADMIN NAVIGATION
        // =====================================================

        adminBottomNavigation =
                findViewById(R.id.adminBottomNavigation);

        adminNavHome =
                findViewById(R.id.adminNavHome);

        adminNavEvents =
                findViewById(R.id.adminNavEvents);

        adminNavRegistrations =
                findViewById(R.id.adminNavRegistrations);

        adminNavProfile =
                findViewById(R.id.adminNavProfile);


        // =====================================================
        // GET USER DETAILS
        // =====================================================

        userName =
                getIntent().getStringExtra("USER_NAME");

        email =
                getIntent().getStringExtra("email");

        userRole =
                getIntent().getStringExtra("USER_ROLE");


        // =====================================================
        // DETERMINE ROLE
        // =====================================================

        if (userRole == null ||
                userRole.trim().isEmpty()) {

            userRole = "Student";
        }


        // =====================================================
        // DEFAULT USER NAME
        // =====================================================

        if (userName == null ||
                userName.trim().isEmpty()) {

            if (userRole.equalsIgnoreCase("Admin")) {

                userName = "Admin";

            } else {

                userName = "Student";
            }
        }


        // =====================================================
        // DEFAULT EMAIL
        // =====================================================

        if (email == null ||
                email.trim().isEmpty()) {

            email = "Email not available";
        }


        // =====================================================
        // DISPLAY USER DETAILS
        // =====================================================

        profileNameTextView.setText(userName);

        profileEmailTextView.setText(email);


        // =====================================================
        // SHOW CORRECT NAVIGATION
        // =====================================================

        if (userRole.equalsIgnoreCase("Admin")) {

            studentBottomNavigation.setVisibility(
                    View.GONE
            );

            adminBottomNavigation.setVisibility(
                    View.VISIBLE
            );

        } else {

            studentBottomNavigation.setVisibility(
                    View.VISIBLE
            );

            adminBottomNavigation.setVisibility(
                    View.GONE
            );
        }


        // =====================================================
        // SHARED PREFERENCES
        // =====================================================

        preferences =
                getSharedPreferences(
                        "CollegeEventProfile",
                        MODE_PRIVATE
                );


        // =====================================================
        // IMAGE PICKER
        // =====================================================

        imagePickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.OpenDocument(),
                        (Uri uri) -> {

                            if (uri != null) {

                                saveProfilePhoto(uri);
                            }
                        }
                );


        // =====================================================
        // LOAD PROFILE PHOTO
        // =====================================================

        loadProfilePhoto();


        // =====================================================
        // PROFILE PHOTO CLICK
        // =====================================================

        profileImageView.setOnClickListener(v -> {

            imagePickerLauncher.launch(
                    new String[]{"image/*"}
            );

        });


        // =====================================================
        // SET CORRECT NAVIGATION
        // =====================================================

        if (userRole.equalsIgnoreCase("Admin")) {

            setupAdminNavigation();

        } else {

            setupStudentNavigation();
        }


        // =====================================================
        // BACK BUTTON
        // =====================================================

        backButton.setOnClickListener(v ->
                goToDashboard()
        );


        // =====================================================
        // PHONE BACK BUTTON
        // =====================================================

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {

                        goToDashboard();
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
    // STUDENT NAVIGATION
    // =====================================================

    private void setupStudentNavigation() {

        StudentBottomNavHelper.setup(
                this,
                studentNavHome,
                studentNavEvents,
                studentNavMyEvents,
                studentNavProfile,
                userName,
                email
        );
    }


    // =====================================================
    // ADMIN NAVIGATION
    // =====================================================

    private void setupAdminNavigation() {

        AdminBottomNavHelper.setup(
                this,
                adminNavHome,
                adminNavEvents,
                adminNavRegistrations,
                adminNavProfile,
                userName,
                email
        );
    }


    // =====================================================
    // SAVE PROFILE PHOTO
    // =====================================================

    private void saveProfilePhoto(Uri uri) {

        try {

            getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

        } catch (SecurityException e) {

            // Some providers do not support
            // persistable permissions.
        }


        // =====================================================
        // SAVE PHOTO FOR THIS USER
        // =====================================================

        String key =
                getProfilePhotoKey();


        preferences.edit()
                .putString(
                        key,
                        uri.toString()
                )
                .apply();


        // =====================================================
        // DISPLAY PHOTO
        // =====================================================

        profileImageView.setImageURI(uri);

        profileImageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );


        Toast.makeText(
                this,
                "Profile photo updated",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =====================================================
    // LOAD PROFILE PHOTO
    // =====================================================

    private void loadProfilePhoto() {

        String key =
                getProfilePhotoKey();


        String savedUri =
                preferences.getString(
                        key,
                        null
                );


        if (savedUri != null &&
                !savedUri.isEmpty()) {

            try {

                Uri uri =
                        Uri.parse(savedUri);

                profileImageView.setImageURI(uri);

                profileImageView.setScaleType(
                        ImageView.ScaleType.CENTER_CROP
                );

            } catch (Exception e) {

                showDefaultProfileIcon();
            }

        } else {

            showDefaultProfileIcon();
        }
    }


    // =====================================================
    // PROFILE PHOTO KEY
    // =====================================================

    private String getProfilePhotoKey() {

        String normalizedEmail =
                email.trim()
                        .toLowerCase(Locale.ROOT);


        return "profile_photo_" +
                normalizedEmail;
    }


    // =====================================================
    // DEFAULT PROFILE ICON
    // =====================================================

    private void showDefaultProfileIcon() {

        profileImageView.setImageResource(
                R.drawable.ic_person
        );

        profileImageView.setScaleType(
                ImageView.ScaleType.CENTER
        );
    }


    // =====================================================
    // GO TO CORRECT DASHBOARD
    // =====================================================

    private void goToDashboard() {

        Intent intent;


        if (userRole.equalsIgnoreCase("Admin")) {

            intent =
                    new Intent(
                            ProfileActivity.this,
                            AdminDashboardActivity.class
                    );

        } else {

            intent =
                    new Intent(
                            ProfileActivity.this,
                            StudentDashboardActivity.class
                    );
        }


        intent.putExtra(
                "USER_NAME",
                userName
        );

        intent.putExtra(
                "email",
                email
        );

        intent.putExtra(
                "USER_ROLE",
                userRole
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


        // Completely clear authenticated screens.

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );


        startActivity(intent);

        finish();
    }
}