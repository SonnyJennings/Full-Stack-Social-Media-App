package com.example.anteaterconnectapp;


import android.view.KeyEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.anteaterconnectapp.adapter.SearchUserRecyclerAdapter;
import com.example.anteaterconnectapp.model.UserModel;
import com.example.anteaterconnectapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.Query;

public class SearchUser extends AppCompatActivity {

    private EditText searchInput;
    private ConstraintLayout Back_Button ;
    private RecyclerView recyclerView;

    private String searchTerm;
    SearchUserRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        Back_Button = findViewById(R.id.backBtn);

        Back_Button.setOnClickListener((v)->{
            onBackPressed();
        });

        searchInput = findViewById(R.id.search_username_input);
        recyclerView = findViewById(R.id.search_user_recycler_view);
        searchInput.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchTerm = searchInput.getText().toString();
                    if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                        searchInput.setError("Invalid Username");
                    }
                    searchUser(searchTerm);
                    return true;
                }
                return false;
            }
        });
    }

    void searchUser(String username){
        String searchTerm = searchInput.getText().toString();
        if(searchTerm.isEmpty()){
            searchInput.setError("Invalid Username");
            return;
        }
        setupSearchRecyclerView(searchTerm);
    }

    void setupSearchRecyclerView(String searchTerm){
        Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchTerm)
                .whereLessThanOrEqualTo("username",searchTerm+'\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }
}
