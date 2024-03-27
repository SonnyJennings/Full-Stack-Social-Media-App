package com.example.anteaterconnectapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.Glide;
import com.example.anteaterconnectapp.R;
import com.example.anteaterconnectapp.model.UserModel;
import com.example.anteaterconnectapp.utils.AndroidUtil;
import com.example.anteaterconnectapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.Firebase;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileActivity extends AppCompatActivity {
    private TextView logoutButton;
    private static final String LOG_TAG = "Profile Activity";
    UserModel userModel;
    private TextView username_display;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    ImageView profilePicture;
    Button updateProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ConstraintLayout backBtn = findViewById(R.id.backBtn);

        // Initialize UI elements
        logoutButton = findViewById(R.id.logout_button); // Replace with your button ID

        username_display = findViewById(R.id.user_profile_name);
        updateProfileBtn = findViewById(R.id.profile_update_button);

        getUserData();

        // URI of image taken by user if data is not null
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getBaseContext(), selectedImageUri, profilePicture);
                        }
                    }
                });

        profilePicture = (ImageView) findViewById(R.id.imageView9);
        profilePicture.setOnClickListener((v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).createIntent(new Function1<Intent, Unit>() {
                @Override
                public Unit invoke(Intent intent) {
                    imagePickLauncher.launch(intent);
                    return null;
                }
            });
        });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userModel = task.getResult().toObject(UserModel.class);
                assert userModel != null;
                username_display.setText(userModel.getUsername());
            } else {
                // Handle case where user details could not be fetched
                Log.e(LOG_TAG, "Error fetching user details");
                Toast.makeText(ProfileActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
            }
        });

        updateProfileBtn.setOnClickListener((v -> {
            if(selectedImageUri != null){
                FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                        .addOnCompleteListener(task -> {
                            updateToFireStore();
                        });
            }else{
                updateToFireStore();
            }
        }));

        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase
                FirebaseUtil.logout();

                // Navigate back to the HomePage (or LoginActivity)
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class); // Replace HomePageActivity with your home or login activity class
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
                startActivity(intent);
                finish(); // Close current activity

            }
        });

    }

    void updateToFireStore(){
        FirebaseUtil.currentUserDetails().set(userModel)
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    AndroidUtil.showToast(getBaseContext(), "Updated successfully.");
                } else{
                    AndroidUtil.showToast(getBaseContext(), "Update failed.");
                }
            });
    }

    void getUserData(){
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePic(getBaseContext(), uri, profilePicture);
                    }
                });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            userModel = task.getResult().toObject(UserModel.class);
            if(userModel != null && userModel.getUsername() != null){
                username_display.setText(userModel.getUsername());
            }
        });
    }

}