package com.kavya.mybooks.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.kavya.mybooks.Models.Book;
import com.kavya.mybooks.Models.User;
import com.kavya.mybooks.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {


    User user;
    ActivityUserBinding binding;
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        pref = getSharedPreferences("MyPref", MODE_PRIVATE);

        String userSt = pref.getString("USER", "");
        if (!userSt.equals("")) {
            Gson gson = new Gson();
            user = gson.fromJson(userSt, User.class);

            binding.userName.setText(user.getName());
            binding.userEmail.setText(user.getEmail());
        }

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogout();
            }
        });
    }

    FirebaseAuth mAuth;
    void handleLogout(){

        mAuth = FirebaseAuth.getInstance();
        Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGIN", false);
        editor.putString("U_EMAIL", null);
        editor.putString("U_UID", null);
        editor.apply();
        mAuth.signOut();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        },800);

    }


}