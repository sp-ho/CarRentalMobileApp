package com.example.carrentalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button signupButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize Firebase auth
        auth = FirebaseAuth.getInstance();

        // initialize EditText fields
        username = (EditText) findViewById(R.id.register_username_editText);
        password = (EditText) findViewById(R.id.register_password_editText);

        // register an user
        signupButton = (Button) findViewById(R.id.register_confirm_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adding username (email) and password to Firebase database
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if(TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty((enteredPassword)))
                {
                    Toast.makeText(RegisterActivity.this,
                            "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else if (enteredPassword.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this,
                            "Password too short", Toast.LENGTH_SHORT).show();
                } else
                {
                    register(enteredUsername, enteredPassword);
                }
            }
        });
    }

    private void register(String username, String password){
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(
            RegisterActivity.this,
            new OnCompleteListener<AuthResult>(){

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // send verification email to the registered user
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> emailTask) {
                                    if (emailTask.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,
                                                "Registration is successful. Please check your email for verification.",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this,
                                                "Failed to send verification email.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}