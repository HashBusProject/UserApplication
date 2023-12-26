package com.hashimte.hashbus1;

import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hashimte.hashbus1.databinding.ActivityMainBinding;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.User;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        LruCache<String, Point> lruCache = new LruCache<>(1000);
        lruCache.put("Hi", new Point());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapsFragment, R.id.ticketFragment, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        TextView usernameDrawer = binding.navView.getHeaderView(0).findViewById(R.id.txt_username_drawer);
        TextView emailDrawer = binding.navView.getHeaderView(0).findViewById(R.id.txt_email_drawer);
        User user = new Gson().fromJson(getSharedPreferences("app_prefs", MODE_PRIVATE).getString("userInfo", null), User.class);
        if(user != null && usernameDrawer != null && emailDrawer != null) {
            usernameDrawer.setText(user.getName());
            emailDrawer.setText(user.getEmail());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void finish() {
        super.finish();
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().remove("StartPoint").apply();
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().remove("EndPoint").apply();
    }
}