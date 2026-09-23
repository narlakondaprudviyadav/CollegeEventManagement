package com.example.collegeeventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView profileNameTextView;
    TextView profileEmailTextView;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        profileNameTextView =
                findViewById(R.id.profileNameTextView);

        profileEmailTextView =
                findViewById(R.id.profileEmailTextView);

        logoutButton =
                findViewById(R.id.logoutButton);


        // Get details from Student Dashboard
        String userName =
                getIntent().getStringExtra("USER_NAME");

        String email =
                getIntent().getStringExtra("email");


        // Display details
        if (userName != null && !userName.isEmpty()) {
            profileNameTextView.setText(userName);
        }

        if (email != null && !email.isEmpty()) {
            profileEmailTextView.setText(email);
        }


        // Logout
        logoutButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    MainActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });
    }
}