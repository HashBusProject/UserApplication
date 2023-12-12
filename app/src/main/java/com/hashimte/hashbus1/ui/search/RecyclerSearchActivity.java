package com.hashimte.hashbus1.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hashimte.hashbus1.MainActivity;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.model.Journey;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerSearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchData[] searchData;
    private List<SearchDataSchedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_search);
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Journey name, sourcePoint,
        Point startPoint = new Gson().fromJson(getIntent().getExtras().getString("StartPoint"), Point.class);
        Point endPoint = new Gson().fromJson(getIntent().getExtras().getString("EndPoint"), Point.class);
        String time = getIntent().getExtras().getString("Time");
        UserServicesImp service = UserServicesImp.getInstance();
        service.getSearchDataSchedule(startPoint.getId(), endPoint.getId(), time).enqueue(new Callback<List<SearchDataSchedule>>() {
            @Override
            public void onResponse(Call<List<SearchDataSchedule>> call, Response<List<SearchDataSchedule>> response) {
                if (response.isSuccessful()){
                    schedules = response.body();
                    if(schedules.isEmpty()){
//                        Snackbar.make(RecyclerSearchActivity.this.getCurrentFocus(), "Replace with your own action", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                        Toast.makeText(RecyclerSearchActivity.this, "There Is No Journey For this Points after this time.", Toast.LENGTH_SHORT).show();
                        getIntent().getExtras().putBoolean("Error", true);
                        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().putBoolean("error", true).apply();
                        finish();
                        return;
                    }
                    SearchAdapter searchAdapter = new SearchAdapter(schedules, RecyclerSearchActivity.this, startPoint);
                    recyclerView.setAdapter(searchAdapter);
                }
                else {
                    Log.e("Error on Response", "error");
                }
            }

            @Override
            public void onFailure(Call<List<SearchDataSchedule>> call, Throwable t) {
                Log.e("Error onFailure", t.getMessage());

            }
        });

    }
}