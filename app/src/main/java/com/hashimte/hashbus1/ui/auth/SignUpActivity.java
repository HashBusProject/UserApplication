package com.hashimte.hashbus1.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hashimte.hashbus1.MainActivity;
import com.hashimte.hashbus1.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText passwordSingUp, confirmPasswordEditText;
    private TextView forLoginPage;
    private Button signupButton;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        passwordSingUp = findViewById(R.id.passwordSignUp);
        confirmPasswordEditText = findViewById(R.id.confirmPass);
        signupButton = findViewById(R.id.signupbutton);
        signupButton.setOnClickListener(view -> {
            // add username and email here.
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });

        minNumPass();
        forLoginPage = findViewById(R.id.lginLink);

        forLoginPage.setOnClickListener(view -> {
            Intent intentSignUp = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intentSignUp);
        });

//        Visibility.visibility(passwordSingUp);
//        Visibility.visibility(confirmPasswordEditText);

    }

    public void minNumPass() {
        Integer inputText = passwordSingUp.getText().toString().length();
        if (inputText < 4) {
            passwordSingUp.setError("4 char minimum!");
        } else {
            passwordSingUp.setError(null);
        }
    }
}