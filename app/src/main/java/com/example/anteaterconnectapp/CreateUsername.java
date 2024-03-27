package com.example.anteaterconnectapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.anteaterconnectapp.utils.FirebaseUtil;
import com.example.anteaterconnectapp.model.UserModel;
import java.util.HashMap;
import java.util.Map;

public class CreateUsername extends AppCompatActivity {
    private EditText usernameEditText;
    private Button submitButton;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_username);

        String userId = getIntent().getStringExtra("USER_ID");

        usernameEditText = findViewById(R.id.username);
        submitButton = findViewById(R.id.username_submit_button);


        submitButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            if (!username.isEmpty()) {
                setUsername(username);
            } else {
                Toast.makeText(CreateUsername.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setUsername(String username) {

        userModel = new UserModel(username, Timestamp.now(),FirebaseUtil.currentUserId());


        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(CreateUsername.this, home_screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);
                }
            }
        });

    }

    private void openHomeScreen(){
        Intent intent = new Intent(this, home_screen.class);
        startActivity(intent);
    }
}
