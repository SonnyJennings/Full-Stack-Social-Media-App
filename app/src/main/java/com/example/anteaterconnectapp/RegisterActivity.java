package com.example.anteaterconnectapp;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.Button;
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button registerButton;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {
            Log.d(TAG, "Register button clicked");
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();


            if(TextUtils.isEmpty(email)){
                Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            }

            else if(TextUtils.isEmpty(password)){
                Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            }

            else createAccount(email, password);
        });

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "Trying to create account");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {// Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(RegisterActivity.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        openCreateUsernameScreen();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }
    private void openLoginScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openCreateUsernameScreen(){
        Intent intent = new Intent(this, CreateUsername.class);
        startActivity(intent);
    }

}

