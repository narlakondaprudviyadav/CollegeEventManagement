package com.example.collegeeventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;

    RadioGroup roleRadioGroup;
    RadioButton studentRadioButton;
    RadioButton adminRadioButton;

    Button createAccountButton;
    TextView loginTextView;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        // Initialize database
        databaseHelper = new DatabaseHelper(this);

        // Connect XML views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        roleRadioGroup = findViewById(R.id.roleRadioGroup);

        studentRadioButton = findViewById(R.id.studentRadioButton);
        adminRadioButton = findViewById(R.id.adminRadioButton);

        createAccountButton = findViewById(R.id.createAccountButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Create account
        createAccountButton.setOnClickListener(v -> createAccount());

        // Go back to login
        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SignUpActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });
    }

    private void createAccount() {

        // Get values
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword =
                confirmPasswordEditText.getText().toString().trim();

        // =========================
        // CHECK EMPTY FIELDS
        // =========================

        if (name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =========================
        // CHECK EMAIL
        // =========================

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            Toast.makeText(
                    this,
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =========================
        // CHECK PASSWORD LENGTH
        // =========================

        if (password.length() < 6) {

            Toast.makeText(
                    this,
                    "Password must contain at least 6 characters",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =========================
        // CHECK PASSWORD MATCH
        // =========================

        if (!password.equals(confirmPassword)) {

            Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =========================
        // GET ROLE
        // =========================

        String role;

        if (studentRadioButton.isChecked()) {

            role = "Student";

        } else if (adminRadioButton.isChecked()) {

            role = "Admin";

        } else {

            Toast.makeText(
                    this,
                    "Please select Student or Admin",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =========================
        // REGISTER USER
        // =========================

        boolean success = databaseHelper.registerUser(
                name,
                email,
                password,
                role
        );

        // =========================
        // SUCCESS
        // =========================

        if (success) {

            Toast.makeText(
                    this,
                    "Account created successfully",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    SignUpActivity.this,
                    MainActivity.class
            );

            // Put email in login page
            intent.putExtra("email", email);

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Email already exists",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}