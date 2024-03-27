package com.example.anteaterconnectapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.example.anteaterconnectapp.utils.FirebaseUtil;
import com.example.anteaterconnectapp.model.UserModel;
import com.google.firebase.FirebaseNetworkException;


public class MainActivity extends AppCompatActivity {
    private Button login;
    UserModel userModel;

    boolean isUsername;
    private static final String LOG_TAG = "MainActivity";
    private Button create_account;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                // User is signed in, check if they have a username
                FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserModel userModel = task.getResult().toObject(UserModel.class);
                        if (userModel != null && userModel.getUsername() != null) {
                            openHomeScreen(); // User exists and has a username
                        } else {
                            openCreateUsername(); // User exists but doesn't have a username
                        }
                    } else {
                        // Handle case where user details could not be fetched
                        Log.e(LOG_TAG, "Error fetching user details", task.getException());
                        Toast.makeText(MainActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // User is signed out
                Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                // Maybe stay on the current screen or navigate to a login screen
            }
        };


        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();


        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            signIn(email, password);
        });
        create_account = (Button) findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccountScreen();
            }
        });

    }




    private void signIn(String email, String password) {
        Toast.makeText(MainActivity.this, "Authentication attempting.",
                Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(MainActivity.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle sign-in failure
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(MainActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Log.d(LOG_TAG , "email :" + email);
                            Toast.makeText(MainActivity.this, "invalid password", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(MainActivity.this, "network error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(LOG_TAG, e.getMessage());
                            Toast.makeText(MainActivity.this, "login error", Toast.LENGTH_SHORT).show();
                        }
                        Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
                    }
                });
    }

    private void openHomeScreen(){
        Intent intent = new Intent(MainActivity.this, home_screen.class);
        startActivity(intent);
    }
    private void openCreateUsername(){
        Intent intent = new Intent(MainActivity.this, CreateUsername.class);
        startActivity(intent);
    }
    private void openCreateAccountScreen() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


}


