package com.hashimte.hashbus1.ui.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.model.Point;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartSearchBarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Point> points;
    private EditText editText;
    private StartPointAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_search_bar);
        ImageButton button = findViewById(R.id.startButtPoint);
        editText = findViewById(R.id.search_text);
        button.setOnClickListener(v -> {
            finish();
        });
        // Initialize recyclerview and its adapter
        recyclerView = findViewById(R.id.fromRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSampleData();
        editText.setHint("Select Your " + getIntent().getExtras().getString("pointType") + " Point");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Point> filteredData = new ArrayList<>();
                for (Point point : points) {
                    if (point.getPointName().toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredData.add(point);
                    }
                }
                adapter.setData(filteredData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    // Sample data for demonstration
    private void getSampleData() {
        UserServicesImp.getInstance().getAllPoints().enqueue(new Callback<List<Point>>() {
            @Override
            public void onResponse(Call<List<Point>> call, Response<List<Point>> response) {
                if (response.isSuccessful()) {
                    points = response.body();
                    adapter = new StartPointAdapter(StartSearchBarActivity.this, points); // Replace with your actual data source
                    recyclerView.setAdapter(adapter);
                    getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            .edit().putString("HashPoint", new Gson().toJson(points.get(0)))
                            .apply();
                } else
                    Log.e("Error response", "Error is " + response.message());
            }

            @Override
            public void onFailure(Call<List<Point>> call, Throwable t) {
                Log.e("Error", "Error is " + t.getMessage());
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        Log.e("Search", "" + getIntent().getExtras());

    }
}