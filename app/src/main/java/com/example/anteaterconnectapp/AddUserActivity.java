package com.example.anteaterconnectapp;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.anteaterconnectapp.R;
import com.example.anteaterconnectapp.model.UserModel;
import com.example.anteaterconnectapp.utils.AndroidUtil;
import com.example.anteaterconnectapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {
    UserModel otherUser;
    ConstraintLayout Back_Button;
    Button Add_Friend_Button;

    TextView username;
    private ImageView profilePicture;
    @Override
    public void onStart() {
        super.onStart();
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        FirebaseUtil.getOtherProfilePicStorageRef(AndroidUtil.getUserModelFromIntent(getIntent()).getUserId()).getDownloadUrl()
                .addOnCompleteListener(p -> {
                    if(p.isSuccessful()){
                        Uri uri  = p.getResult();
                        AndroidUtil.setProfilePic(getBaseContext(),uri,profilePicture);
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());


        username = findViewById(R.id.username_text);
        username.setText(otherUser.getUsername());


        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(p -> {
                    if(p.isSuccessful()){
                        Uri uri  = p.getResult();
                        AndroidUtil.setProfilePic(getBaseContext(),uri,profilePicture);
                    }
                });

        Add_Friend_Button= (Button)findViewById(R.id.add_friend_button);

        Back_Button = findViewById(R.id.backBtn);



        Back_Button.setOnClickListener((v)->{
            onBackPressed();
        });
        Add_Friend_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(otherUser);
            }
        });
    }


    private void addFriend(UserModel otherUser) {

        FirebaseUtil.allUserCollectionReference()
                .document(FirebaseUtil.currentUserId())
                .collection("Friends")
                .document(otherUser.getUserId())
                .set(otherUser).addOnSuccessListener(aVoid -> {
                    // Handle success, e.g., show a success message or update UI
                    AndroidUtil.showToast(this, "Successfully added friend!");
                })
                .addOnFailureListener(e -> {
                    // Handle failure, e.g., show an error message
                });


    }
}