package com.example.carrentalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static EditText username;
    private EditText password;
    private Button loginButton;
    TextView signupTextView;
    SpannableString spannableString;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // find widgets in layout
        username = findViewById(R.id.username_editText);
        password = findViewById(R.id.password_editText);
        loginButton = findViewById(R.id.login_button);

        // set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // searching username (email) and password in Firebase database
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if(TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty((enteredPassword)))
                {
                    Toast.makeText(LoginActivity.this,
                            "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else if (enteredPassword.length() < 6)
                {
                    Toast.makeText(LoginActivity.this,
                            "Password too short", Toast.LENGTH_SHORT).show();
                } else
                {
                    login(enteredUsername, enteredPassword);
                }
            }
        });

        signupTextView = findViewById(R.id.signup_textView);
        spannableString = new SpannableString("Sign Up");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline from the link
            }
        };
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupTextView.setText(spannableString);
        signupTextView.setMovementMethod(LinkMovementMethod.getInstance()); // Make the link clickable
    }// end of onCreate


    @Override
    protected void onStart() {
        super.onStart();
        // test if current user is still login, if so, bypass the login process
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail;
        if (user != null)
        {
            // obtain email directly from Firebase user object
            userEmail = user.getEmail();
            if (userEmail.endsWith("car.com")) {
                // bring the user to MainActivity if the current user is an admin
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                // bring the user to CustomerActivity if the current user is a customer
                startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
            }
        }
    }

    private void login(String username, String password) {
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(
            LoginActivity.this,
            new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // check if the username ends with "car.com"
                    if (username.endsWith("car.com")) {
                        // bring the user to MainActivity (admin)
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        // bring the user to CustomerActivity
                        startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
                    }
                    Toast.makeText(LoginActivity.this,
                            "Login is successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}