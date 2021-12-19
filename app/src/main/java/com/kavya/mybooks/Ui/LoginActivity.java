package com.kavya.mybooks.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.kavya.mybooks.Models.User;
import com.kavya.mybooks.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        binding.loginSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verifyInput())
                    startLogin();
            }
        });
    }

    String email, password;
    public Boolean verifyInput(){

        email = binding.loginEmailInp.getText().toString();
        password = binding.loginPassInp.getText().toString();

        if(email != null && email.length() > 0)
            if(password != null && password.length() > 5)
                return true;
        return false;
    }


    FirebaseFirestore mdb;
    void startLogin() {

        showLoading();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoading();
                        if(task.isSuccessful()){

                            getUserDetails();


                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void getUserDetails() {

        mdb = FirebaseFirestore.getInstance();

        mdb.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            Gson gson = new Gson();
                            String objString = gson.toJson(user);
                            SharedPreferences pref = getSharedPreferences("MyPref",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("USER",objString);
                            editor.apply();

                            saveToPref();

                            Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            },600);

                        } else
                            Toast.makeText(LoginActivity.this, "Failed to get user document", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void showLoading() { binding.loginProgress.setVisibility(View.VISIBLE);}
    public void hideLoading() { binding.loginProgress.setVisibility(View.GONE);}

    void saveToPref() {

        SharedPreferences sharedPref = getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGIN", true);
        editor.putString("U_EMAIL", email);
        editor.putString("U_UID", mAuth.getCurrentUser().getUid());
        editor.apply();



    }
}
