package com.example.anteaterconnectapp;

import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.example.anteaterconnectapp.model.UserModel;
import com.example.anteaterconnectapp.utils.AndroidUtil;
import com.example.anteaterconnectapp.utils.FirebaseUtil;


public class home_screen extends AppCompatActivity {
    private Button add_friends;
    private Button add_classes;
    private Button add_groups;
    private LinearLayout my_friends;
    private LinearLayout my_groups;
    private LinearLayout my_classes;
    private TextView username_welcome;

    private ImageView chat_list;
    private ImageView profilePicture;
    private static final String HOME_TAG = "Homescreen Activity";
    private UserModel model;


    @Override
    public void onStart() {
        super.onStart();
        profilePicture = (ImageView) findViewById(R.id.imageView3);
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
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
        setContentView(R.layout.activity_home_screen);

        add_friends = (Button)findViewById(R.id.add_friends);
        add_friends.setOnClickListener(v -> openSearchUserScreen());

        add_classes = (Button)findViewById(R.id.add_classes);
        add_classes.setOnClickListener(v -> openSearchClassesScreen());

        add_groups = (Button)findViewById(R.id.add_groups);
        add_groups.setOnClickListener(v -> openSearchGroupScreen());

        my_friends = findViewById(R.id.My_friends);
        my_friends.setOnClickListener(v -> openMyFriendsScreen());

        my_groups = findViewById(R.id.My_groups);
        my_groups.setOnClickListener(v -> openMyGroupScreen());

        my_classes = findViewById(R.id.My_classes);
        my_classes.setOnClickListener(v -> openMyClassesScreen());

        chat_list = findViewById(R.id.chatListButton);
        chat_list.setOnClickListener(v -> openChatListScreen());

        profilePicture = (ImageView) findViewById(R.id.imageView3);
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(p -> {
                    if(p.isSuccessful()){
                        Uri uri  = p.getResult();
                        AndroidUtil.setProfilePic(getBaseContext(),uri,profilePicture);
                    }
                });


        username_welcome = findViewById(R.id.username_welcome);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                model = task.getResult().toObject(UserModel.class);
                username_welcome.append(model.getUsername()); // add another if-else for retreiving username (if null)
            } else {
                // Handle case where user details could not be fetched
                Log.e(HOME_TAG, "Error fetching user details");
                Toast.makeText(home_screen.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigation();

    }

    private void BottomNavigation() {
        LinearLayout profileBtn=findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> startActivity(new Intent(home_screen.this, ProfileActivity.class)));
    }

    private void openSearchUserScreen(){
        Intent intent = new Intent(this, SearchUser.class);
        startActivity(intent);
    }

    private void openMyClassesScreen(){
        Intent intent = new Intent(this, MyClassesScreen.class);
        startActivity(intent);
    }

    private void openMyGroupScreen(){
        Intent intent = new Intent(this, MyGroupScreen.class);
        startActivity(intent);
    }

    private void openMyFriendsScreen(){
        Intent intent = new Intent(this, MyFriendsScreen.class);
        startActivity(intent);
    }

    private void openSearchClassesScreen(){
        Intent intent = new Intent(this, SearchClasses.class);
        startActivity(intent);
    }

    private void openSearchGroupScreen(){
        Intent intent = new Intent(this, SearchGroups.class);
        startActivity(intent);
    }

    private void openChatListScreen(){
        Intent intent = new Intent(this, ChatList.class);
        startActivity(intent);
    }

}