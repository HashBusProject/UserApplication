package com.hashimte.hashbus1.ui.reserve;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Conference;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.maps.model.LatLng;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.databinding.ActivityJourneyReserveViewBinding;
import com.hashimte.hashbus1.map.DirectionsTask;
import com.hashimte.hashbus1.model.Bus;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.model.Ticket;
import com.hashimte.hashbus1.model.User;
import com.hashimte.hashbus1.ui.ride.ConfirmRideActivity;
import com.hashimte.hashbus1.ui.ticket.ShortestPath;

import java.util.List;
import java.util.Objects;
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
    private Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJourneyReserveViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        gson = new Gson();
        journeyPrefs = getSharedPreferences("journey_prefs", MODE_PRIVATE);
        data = getIntent().getExtras();
        schedule = gson.fromJson(
                data.getString("searchData", "{}"),
                SearchDataSchedule.class
        );
        getSupportActionBar().setTitle(schedule.getJourney().getName());
        if (!data.getBoolean("isSame", true)) {
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
        binding.imgTicket.setImageResource(R.drawable.destination);
        binding.txtTicket.setText("" + pick.getPointName() + " to " + end.getPointName());
        binding.btnResserve.setText(R.string.confirm_ride);
        binding.btnResserve.setOnClickListener(v -> {
            Intent intent = new Intent(JourneyReserveView.this, ConfirmRideActivity.class);
            intent.putExtras(data);
            startActivity(intent);
        });
        binding.btnCancelOrder.setOnClickListener(v -> {
            journeyPrefs.edit().clear().apply();
            setContentOfView();
        });
        if (schedule.getSchedule().getNextPoint() > 0) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    UserServicesImp.getInstance().getBusById(schedule.getBus().getId()).enqueue(
                            new Callback<Bus>() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onResponse(Call<Bus> call, Response<Bus> response) {
                                    if (response.isSuccessful()) {
                                        Log.i("Bus is :", response.body().toString());
                                        bus = response.body();
                                        new DirectionsTask(new LatLng(bus.getX(), bus.getY()), new LatLng(pick.getX(), pick.getY())) {
                                            @Override
                                            protected void onPostExecute(String s) {
                                                super.onPostExecute(s);
                                                timeToArrive = s;
                                                binding.txtTime.setText(getString(R.string.bus_time_result_min, s, start.getPointName()));
                                            }
                                        }.execute();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Bus> call, Throwable t) {

                                }
                            }
                    );
                }
            });
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
        binding.imgTicket.setImageResource(R.drawable.ticket1);
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

    public SharedPreferences getJourneyPrefs() {
        return journeyPrefs;
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
                            Toast.makeText(JourneyReserveView.this, "RESPONED", Toast.LENGTH_LONG).show();
                            binding.txtTicket.setText(getString(R.string.ticket_result_price, schedule.getJourney().getPrice()));
                            binding.btnBuyTicket.setVisibility(View.VISIBLE);
                            binding.btnBuyTicket.setEnabled(true);
                            binding.btnResserve.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Ticket>> call, Throwable t) {
                        Toast.makeText(JourneyReserveView.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        binding.txtTicket.setText(getString(R.string.ticket_result_price, schedule.getJourney().getPrice()));
                        binding.btnBuyTicket.setVisibility(View.VISIBLE);
                        binding.btnBuyTicket.setEnabled(true);
                        binding.btnResserve.setEnabled(false);
                    }
                }
        );
    }
}