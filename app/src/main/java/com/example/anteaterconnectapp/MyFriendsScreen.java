package com.example.anteaterconnectapp;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.anteaterconnectapp.adapter.FriendsListRecyclerAdapter;
import com.example.anteaterconnectapp.adapter.RecentChatRecyclerAdapter;
import com.example.anteaterconnectapp.adapter.SearchUserRecyclerAdapter;
import com.example.anteaterconnectapp.model.ChatroomModel;
import com.example.anteaterconnectapp.model.UserModel;
import com.example.anteaterconnectapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class MyFriendsScreen extends AppCompatActivity {

    private ConstraintLayout Back_Button ;
    private RecyclerView recyclerView;

    FriendsListRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriends_screen);

        Back_Button = findViewById(R.id.backBtn);

        Back_Button.setOnClickListener((v)->{
            onBackPressed();
        });

        recyclerView = findViewById(R.id.friends_list_recycler_view);

        setupRecyclerView();
    }

    void setupRecyclerView(){
        Query query = FirebaseUtil.allUserCollectionReference()
                .document(FirebaseUtil.currentUserId())
                .collection("Friends");


        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new FriendsListRecyclerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}