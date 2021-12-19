package com.kavya.mybooks.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kavya.mybooks.Models.User;
import com.kavya.mybooks.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {


    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.regLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });


        binding.regSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyInput())
                    startRegister();
                else
                    Toast.makeText(RegisterActivity.this, "Inputs invalid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String name, email, password;
    public Boolean verifyInput(){

        name = binding.regNameInp.getText().toString();
        email = binding.regEmailInp.getText().toString();
        password = binding.regPassInp.getText().toString();

        if(name != null && name.length() > 0)
            if(email != null && email.length() > 0)
                if(password != null && password.length() > 5)
                    return true;

        return false;
    }

    public void startRegister(){

        showLoading();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String uId = mAuth.getCurrentUser().getUid();
                            User u = new User(name,email,password);
                            u.setuId(uId);
                            db.collection("Users")
                                    .document(uId).set(u)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            hideLoading();
                                            if(task.isSuccessful()){

                                                Toast.makeText(RegisterActivity.this, "Register successfull", Toast.LENGTH_SHORT).show();

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                        finish();
                                                    }
                                                },600);

                                            } else {
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        } else {

                            hideLoading();
                            Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            Log.e("#33TT", "Auth Error", task.getException());
                        }
                    }
                });

    }



    public void showLoading() { binding.regProgress.setVisibility(View.VISIBLE);}
    public void hideLoading() { binding.regProgress.setVisibility(View.GONE);}
}


