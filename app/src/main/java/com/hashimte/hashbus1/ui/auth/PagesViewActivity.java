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
        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
    }

}