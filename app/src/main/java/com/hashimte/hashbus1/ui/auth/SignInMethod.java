package com.hashimte.hashbus1.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.hashimte.hashbus1.R;

public class SignInMethod extends AppCompatActivity {
    private Button login, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_method);
        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signbutton);
        login.setOnClickListener(view -> {
            Intent intentLogin = new Intent(SignInMethod.this, PagesViewActivity.class);
            startActivity(intentLogin);
        });
        signUp.setOnClickListener(view -> {
            Intent intentSignUp = new Intent(SignInMethod.this, PagesViewActivity.class);
            intentSignUp.putExtra("up", true);
            startActivity(intentSignUp);
        });
    }
}