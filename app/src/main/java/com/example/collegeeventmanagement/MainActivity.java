package com.example.collegeeventmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button loginButton;

    private RadioGroup roleRadioGroup;
    private RadioButton studentRadioButton;
    private RadioButton adminRadioButton;

    private TextView signupTextView;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // =========================
        // NOTIFICATION CHANNEL
        // =========================

        NotificationHelper.createNotificationChannel(this);

        // =========================
        // SET LAYOUT
        // =========================

        setContentView(R.layout.activity_main);


        // =========================
        // DATABASE
        // =========================

        databaseHelper =
                new DatabaseHelper(this);


        // =========================
        // CONNECT XML VIEWS
        // =========================

        emailEditText =
                findViewById(R.id.emailEditText);

        passwordEditText =
                findViewById(R.id.passwordEditText);

        loginButton =
                findViewById(R.id.loginButton);

        roleRadioGroup =
                findViewById(R.id.roleRadioGroup);

        studentRadioButton =
                findViewById(R.id.studentRadioButton);

        adminRadioButton =
                findViewById(R.id.adminRadioButton);

        signupTextView =
                findViewById(R.id.signupTextView);


        // =========================
        // REQUEST NOTIFICATION
        // PERMISSION
        // =========================

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        100
                );
            }
        }


        // =========================
        // LOGIN BUTTON
        // =========================

        loginButton.setOnClickListener(v -> {

            // -------------------------
            // GET EMAIL
            // -------------------------

            String email =
                    emailEditText.getText()
                            .toString()
                            .trim()
                            .toLowerCase(Locale.ROOT);


            // -------------------------
            // GET PASSWORD
            // -------------------------

            String password =
                    passwordEditText.getText()
                            .toString()
                            .trim();


            // =========================
            // CHECK EMAIL
            // =========================

            if (email.isEmpty()) {

                emailEditText.setError(
                        "Enter email"
                );

                emailEditText.requestFocus();

                return;
            }


            // =========================
            // CHECK PASSWORD
            // =========================

            if (password.isEmpty()) {

                passwordEditText.setError(
                        "Enter password"
                );

                passwordEditText.requestFocus();

                return;
            }


            // =========================
            // GET SELECTED ROLE
            // =========================

            String role;


            if (studentRadioButton.isChecked()) {

                role = "Student";

            } else if (adminRadioButton.isChecked()) {

                role = "Admin";

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "Please select Student or Admin",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            // =========================
            // CHECK LOGIN
            // =========================

            boolean validUser =
                    databaseHelper.checkUser(
                            email,
                            password,
                            role
                    );


            // =========================
            // LOGIN SUCCESS
            // =========================

            if (validUser) {

                String userName =
                        databaseHelper.getUserName(
                                email
                        );


                // -------------------------
                // SAFETY CHECK
                // -------------------------

                if (userName == null ||
                        userName.trim().isEmpty()) {

                    userName = "User";
                }


                // =========================
                // SUCCESS MESSAGE
                // =========================

                Toast.makeText(
                        MainActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                ).show();


                // =========================
                // STUDENT LOGIN
                // =========================

                if (role.equalsIgnoreCase("Student")) {

                    Intent intent =
                            new Intent(
                                    MainActivity.this,
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


                    startActivity(intent);
                }


                // =========================
                // ADMIN LOGIN
                // =========================

                else if (role.equalsIgnoreCase("Admin")) {

                    Intent intent =
                            new Intent(
                                    MainActivity.this,
                                    AdminDashboardActivity.class
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
                }


                // =========================
                // CLOSE LOGIN ACTIVITY
                // =========================

                finish();
            }


            // =========================
            // LOGIN FAILED
            // =========================

            else {

                Toast.makeText(
                        MainActivity.this,
                        "Invalid email, password or role",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        // =========================
        // SIGN UP
        // =========================

        signupTextView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SignUpActivity.class
                    );

            startActivity(intent);
        });
    }


    // =====================================================
    // HANDLE NOTIFICATION PERMISSION RESULT
    // =====================================================

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );
    }
}