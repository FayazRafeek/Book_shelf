package com.kavya.mybooks.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kavya.mybooks.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isLoggedIn())
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        },1500);
    }

    Boolean isLoggedIn(){
        SharedPreferences sharedPref = getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        return sharedPref.getBoolean("IS_LOGIN",false);
    }
}
