package com.hashimte.hashbus1.ui.reserve;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.databinding.ActivityJourneyReserveViewBinding;
import com.hashimte.hashbus1.databinding.ActivityMainBinding;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;

import lombok.NonNull;

public class JourneyReserveView extends AppCompatActivity {
    private ActivityJourneyReserveViewBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout layout;
    private SearchDataSchedule schedule;
    private Point start;
    private Point pick;
    private Point end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_reserve_view);
        binding = ActivityJourneyReserveViewBinding.inflate(getLayoutInflater());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle data = getIntent().getExtras();
        Gson gson = new Gson();
        schedule = gson.fromJson(
                data.getString("searchData"),
                SearchDataSchedule.class
        );
        getSupportActionBar().setTitle(schedule.getJourney().getName());
        if (!data.getBoolean("isSame", false)) {
            pick = gson.fromJson(data.getString("pickPoint"), Point.class);
        }
        start = gson.fromJson(data.getString("startPoint"), Point.class);
        end = gson.fromJson(data.getString("endPoint"), Point.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public SearchDataSchedule getSchedule() {
        Log.d("data here: ", schedule.getJourney().toString());
        return schedule;
    }

    public Point getStart() {
        return start;
    }

    public Point getPick() {
        return pick;
    }

    public Point getEnd() {
        return end;
    }
}