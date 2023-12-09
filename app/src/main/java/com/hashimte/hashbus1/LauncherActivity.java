package com.hashimte.hashbus1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.hashimte.hashbus1.ui.auth.SignInMethod;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isLoggedIn", false)){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            finish();
            startActivity(new Intent(this, SignInMethod.class));
        }
    }
}