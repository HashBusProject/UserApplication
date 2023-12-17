package com.hashimte.hashbus1.ui.ticket;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Routing;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.map.MyRoutingListener;
import com.hashimte.hashbus1.model.Journey;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.Ticket;
import com.hashimte.hashbus1.model.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShortestPath extends FragmentActivity implements OnMapReadyCallback {
    //google map objectc
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 1;
    boolean locationPermission = false;
    private List<Polyline> polylines = null;
    private List<Point> points;
    private MaterialTextView journeyName;
    private MaterialTextView ticketPrice;
    private MaterialTextView startPointName;
    private MaterialTextView endPointName;
    private Button buy;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortest_path);
        //request location permission.
//        getActionBar().setCustomView(R.layout.app_bar_main);
        requestPermision();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_reserve);
        mapFragment.getMapAsync(this);
        journeyName = findViewById(R.id.txt_journey_name);
        ticketPrice = findViewById(R.id.txt_ticket_price);
        startPointName = findViewById(R.id.txt_start_name);
        endPointName = findViewById(R.id.txt_end_name);
        buy = findViewById(R.id.btn_buy_ticket);
        cancel = findViewById(R.id.btn_cancel);
        User user = new Gson().fromJson(getSharedPreferences("app_prefs", MODE_PRIVATE).getString("userInfo", null), User.class);
        Bundle extras = getIntent().getExtras();
        if (locationPermission) {
            Journey journey = new Gson().fromJson(
                    extras.getString("data", null),
                    Journey.class
            );
            journeyName.setText(journey.getName());
            ticketPrice.setText(new DecimalFormat("##.## JD").format(journey.getPrice()));
            UserServicesImp.getInstance().getAllPointByJourneyId(journey.getId()).enqueue(
                    new Callback<List<Point>>() {
                        @Override
                        public void onResponse(Call<List<Point>> call, Response<List<Point>> response) {
                            if (response.isSuccessful()) {
                                points = response.body();
                                findRoutes(points);
                                startPointName.setText(points.get(0).getPointName());
                                endPointName.setText(points.get(points.size() - 1).getPointName());
                                Log.i("Points", points.toString());
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<List<Point>> call, Throwable t) {
                            Log.e("error 167", t.getMessage());
                        }
                    }
            );
            buy.setOnClickListener(v -> {
                // TODO, buy a ticket from here
                UserServicesImp.getInstance().buyATicket(user.getUserID(), journey.getId()).enqueue(
                        new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.isSuccessful()) {
                                    finish();
                                }
                                else Log.e("Error :", response.message());
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("onFaliure :", t.getMessage());
                            }
                        }
                );
            });
            cancel.setOnClickListener(v -> {
                finish();
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
//            startLocation
        }
    }

    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    //to get user location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermision();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(location -> myLocation = location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // function to find Routes.
    public void findRoutes(List<Point> points) {
        if (points.get(0) == null || points.get(points.size() - 1) == null) {
            Toast.makeText(ShortestPath.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {
            List<LatLng> latLngs = new ArrayList<>();
            latLngs.add(new LatLng(points.get(0).getX(), points.get(0).getY()));
            mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title("Start Point"));
            for (int i = 1; i < points.size(); i++) {
                LatLng latLng = new LatLng(points.get(i).getX(), points.get(i).getY());
                latLngs.add(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).title(points.get(i).getPointName());
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop));
                mMap.addMarker(markerOptions);
            }
            latLngs.add(new LatLng(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY()));
            mMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).title("End Point"));
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(new MyRoutingListener(mMap, 4, getResources()))
                    .alternativeRoutes(true)
                    .waypoints(latLngs)
                    .key("AIzaSyBH4sMITOfvKIIq5Sa7HuGq7oikYEujTYs")
                    .build();
            routing.execute();
        }
    }
}