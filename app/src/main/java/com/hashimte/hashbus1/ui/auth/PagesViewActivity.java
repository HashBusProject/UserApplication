package com.hashimte.hashbus1.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;

import com.hashimte.hashbus1.R;

public class PagesViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages_view);
        boolean up = getIntent().getBooleanExtra("up", false);
        if (up) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new SignUpFragment())
                    .commit();
        } else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new LoginFragment())
                    .commit();
        }
    }

}