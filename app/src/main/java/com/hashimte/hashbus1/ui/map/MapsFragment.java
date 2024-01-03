package com.hashimte.hashbus1.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.databinding.FragmentMapsBinding;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.ui.search.RecyclerSearchActivity;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Calendar;

public class MapsFragment extends Fragment {
    private final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myMap;
    private CardView timer;
    private TextView txtTime;
    private Button search;
    private TextView startPointTxt;
    private TextView endPointTxt;
    private MarkerOptions startMarker = null;
    private MarkerOptions endMarker = null;
    private Point startPoint = null;
    private Point endPoint = null;
    private Bundle data;
    private Location myLocation;
    private Calendar myCalender;
    private Time time;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 102);
            }
            myMap = googleMap;
            //currentLocation.getLatitude(), currentLocation.getLongitude()

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                    .PERMISSION_GRANTED) {
                myMap.setMyLocationEnabled(true);
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getLastLocation();

        return rootView;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_reserve);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
                    LatLng currentLatLng = new LatLng(
                            currentLocation.getLatitude(), currentLocation.getLongitude());

                    myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(requireActivity(), "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_reserve);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        startPointTxt = view.findViewById(R.id.startPoint);
        endPointTxt = view.findViewById(R.id.endPoint);
        timer = view.findViewById(R.id.time_card);
        txtTime = view.findViewById(R.id.time_txt);
        search = view.findViewById(R.id.searchJourney);
        data = new Bundle();
        startPointTxt.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StartSearchBarActivity.class);
            data.putString("pointType", "Start");
            intent.putExtras(data);
            startActivity(intent);
        });
        endPointTxt.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StartSearchBarActivity.class);
            data.putString("pointType", "End");
            intent.putExtras(data);
            startActivity(intent);
        });
        timer.setOnClickListener(v -> {
            myCalender = Calendar.getInstance();
            int mHour = myCalender.get(Calendar.HOUR);
            int mMinute = myCalender.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(),
                    R.style.CustomDatePicker,
                    (timePicker, hourOfDay, minutes) -> {
                        DecimalFormat decimalFormat = new DecimalFormat("00");
                        time = Time.valueOf(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minutes) + ":00");
                        txtTime.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minutes));
                    },
                    0, 0, false
            );
            timePickerDialog.show();
        });
        search.setOnClickListener(v -> {
            if (startPoint == null) {
                Snackbar.make(view, "Please Pick The Start Point", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (endPoint == null) {
                Snackbar.make(view, "Please Pick The End Point", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (time == null) {
                Snackbar.make(view, "Please Pick Time", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Intent intent = new Intent(getContext(), RecyclerSearchActivity.class);
                data.putString("StartPoint", new Gson().toJson(startPoint));
                data.putString("EndPoint", new Gson().toJson(endPoint));
                data.putString("Time", time.toString());
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .remove("StartPoint")
                .remove("EndPoint")
                .apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startPoint = null;
        endPoint = null;
        getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .remove("StartPoint")
                .remove("EndPoint")
                .apply();
    }

    //TODO, Solve NullPointerException Error When we restart The;
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("error", false)) {
            Snackbar.make(getView(), "There is No journeys in this way or the time!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            sharedPreferences.edit().remove("error").apply();
        }
        Point temp = new Gson().fromJson(sharedPreferences.getString("StartPoint", ""), Point.class);
        if (temp != null)
            startPoint = temp;
        temp = new Gson().fromJson(sharedPreferences.getString("EndPoint", ""), Point.class);
        if (temp != null)
            endPoint = temp;

        if (myMap == null)
            return;
        // Clear existing markers before adding new ones
        if (myMap != null)
            myMap.clear();

        if (startPoint != null) {
            startPointTxt.setText(startPoint.getPointName());
            LatLng currentLatLng = new LatLng(startPoint.getX(), startPoint.getY());

            startMarker = new MarkerOptions()
                    .position(currentLatLng)
                    .title(startPoint.getPointName());

            myMap.addMarker(startMarker);
            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));

        }

        if (endPoint != null) {
            endPointTxt.setText(endPoint.getPointName());
            LatLng currentLatLng = new LatLng(endPoint.getX(), endPoint.getY());

            endMarker = new MarkerOptions()
                    .position(currentLatLng)
                    .title(endPoint.getPointName());

            myMap.addMarker(startMarker);
            myMap.addMarker(endMarker);
            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
        }

        // Draw line if both points are available
        if (startPoint != null && endPoint != null) {
            PolylineOptions lineOptions = new PolylineOptions()
                    .add(new LatLng(startPoint.getX(), startPoint.getY()))
                    .add(new LatLng(endPoint.getX(), endPoint.getY()))
                    .color(Color.RED)
                    .width(5);
            // Add the polyline to the map
            myMap.addPolyline(lineOptions);
            myMap.addMarker(startMarker);

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.include(new LatLng(startPoint.getX(), startPoint.getY()));
            boundsBuilder.include(new LatLng(endPoint.getX(), endPoint.getY()));
            LatLngBounds bounds = boundsBuilder.build();

            int padding = 100;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            myMap.animateCamera(cu);
        }
    }

}