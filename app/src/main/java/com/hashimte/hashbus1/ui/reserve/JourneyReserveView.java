package com.hashimte.hashbus1.ui.reserve;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.databinding.ActivityJourneyReserveViewBinding;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.model.Ticket;
import com.hashimte.hashbus1.model.User;
import com.hashimte.hashbus1.ui.ticket.ShortestPath;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JourneyReserveView extends AppCompatActivity {
    private ActivityJourneyReserveViewBinding binding;
    private SearchDataSchedule schedule;
    private Point start;
    private Point pick;
    private Point end;
    private List<Ticket> userTickets;
    private Bundle data;
    private User user;
    private Gson gson;
    private String timeToArrive;
    private SharedPreferences journeyPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJourneyReserveViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gson = new Gson();
        journeyPrefs = getSharedPreferences("journey_prefs", MODE_PRIVATE);
        data = getIntent().getExtras();
        schedule = gson.fromJson(
                data.getString("searchData"),
                SearchDataSchedule.class
        );
        getSupportActionBar().setTitle(schedule.getJourney().getName());
        if (!data.getBoolean("isSame", false)) {
            pick = gson.fromJson(data.getString("pickPoint"), Point.class);
        }
        start = gson.fromJson(data.getString("startPointData"), Point.class);
        end = gson.fromJson(data.getString("endPointData"), Point.class);
        timeToArrive = data.getString("timeToArrive");
        user = gson.fromJson(getSharedPreferences("app_prefs", MODE_PRIVATE).getString("userInfo", null), User.class);
        if (!journeyPrefs.contains("track"))
            setContentOfView();
        else
            setContentIfReserve();
    }

    private void setContentIfReserve() {
        refreshTime();
        binding.imgTicket.setImageResource(R.drawable.destination);
        binding.txtTicket.setText("");
        binding.btnResserve.setText(R.string.confirm_ride);
        binding.btnCancelOrder.setOnClickListener(v -> {
            journeyPrefs.edit().clear().apply();
//            onResume();
            setContentOfView();
        });
        binding.btnResserve.setOnClickListener(v -> {

        });
    }

    private void refreshTime() {

    }

    private void setContentOfView() {
        if (schedule.getSchedule().getNextPoint() > 0) {
            binding.txtTime.setText(getString(R.string.bus_time_result_min, timeToArrive, start.getPointName()));
        } else {

            UserServicesImp.getInstance().getPointByID(schedule.getJourney().getSourcePoint()).enqueue(
                    new Callback<Point>() {
                        @Override
                        public void onResponse(Call<Point> call, Response<Point> response) {
                            if (response.isSuccessful()) {
                                binding.imgTime.setImageResource(R.drawable.schedule);
                                binding.txtTime.setText(getString(R.string.bus_time_result_start, response.body().getPointName(),
                                        schedule.getSchedule().getTime()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Point> call, Throwable t) {

                        }
                    }
            );
        }
        getTicketsForUser();
        binding.btnResserve.setText(R.string.reserve_journey);
        binding.txtDriverName.setText(schedule.getBus().getDriver().getName());
        binding.btnResserve.setOnClickListener(v -> {
            //TODO, after reserve!!
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(getApplicationContext(), "you choose yes action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("AlertDialogExample");
            alert.show();

            journeyPrefs.edit().putBoolean("track", true).apply();
            setContentIfReserve();
        });
        binding.btnBuyTicket.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ShortestPath.class);
            String journeyData = gson.toJson(schedule.getJourney());
            data.putString("data", journeyData);
            data.putString("user", gson.toJson(user));
            intent.putExtras(data);
            startActivity(intent);
        });

        binding.btnCancelOrder.setOnClickListener(v -> {
            finish();
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        getTicketsForUser();
        if (!journeyPrefs.contains("track"))
            setContentOfView();
        else
            setContentIfReserve();
    }

    private void getTicketsForUser() {
        UserServicesImp.getInstance().getTicketsByUserId(user.getUserID()).enqueue(
                new Callback<List<Ticket>>() {
                    @Override
                    public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                        if (response.isSuccessful()) {
                            userTickets = response.body().stream().filter(ticket ->
                                    ticket.getJourney().getId() == schedule.getJourney().getId()
                            ).collect(Collectors.toList());
                            if (userTickets.isEmpty()) {
                                binding.txtTicket.setText(getString(R.string.ticket_result_price, schedule.getJourney().getPrice()));
                                binding.btnBuyTicket.setVisibility(View.VISIBLE);
                                binding.btnBuyTicket.setEnabled(true);
                                binding.btnResserve.setEnabled(false);
                            } else {
                                binding.txtTicket.setText(getString(R.string.ticket_result_having, userTickets.size()));
                                binding.btnBuyTicket.setVisibility(View.INVISIBLE);
                                binding.btnBuyTicket.setEnabled(false);
                                binding.btnResserve.setEnabled(true);
                            }
                        } else {
                            binding.txtTicket.setText(getString(R.string.ticket_result_price, schedule.getJourney().getPrice()));
                            binding.btnBuyTicket.setVisibility(View.VISIBLE);
                            binding.btnBuyTicket.setEnabled(true);
                            binding.btnResserve.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Ticket>> call, Throwable t) {
                        binding.txtTicket.setText(getString(R.string.ticket_result_price, schedule.getJourney().getPrice()));
                        binding.btnBuyTicket.setVisibility(View.VISIBLE);
                        binding.btnBuyTicket.setEnabled(true);
                        binding.btnResserve.setEnabled(false);
                    }
                }
        );
    }
}