package com.hashimte.hashbus1.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hashimte.hashbus1.MainActivity;
import com.hashimte.hashbus1.R;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;

    private TextView newAccount, forgotPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        username = findViewById(R.id.usereditbox);
        password = findViewById(R.id.passeditbox);
        newAccount = findViewById(R.id.createnewaccount);
        forgotPassword = findViewById(R.id.forgetPassword);
//        Visibility.visibility(password);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            editor.putBoolean("isLoggedIn", true);
            editor.apply();
            startActivity(intent);
            finish();
        });


        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(LoginActivity.this, SignUpActivity.class)
                );
                finish();
            }
        });

        ForgetPasswordActivity forgetPasswordActivity = new ForgetPasswordActivity();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(forgot);
            }
        });
    }

}