package com.hashimte.hashbus1.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
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

import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.model.Point;

public class MapsFragment extends Fragment {
    private final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myMap;
    private Button timer;
    private TextView startPointTxt;
    private TextView endPointTxt;

    private MarkerOptions startMarker = null;
    private MarkerOptions endMarker = null;
    private Point startPoint = null;
    private Point endPoint = null;
    private Bundle data;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            myMap = googleMap;

            //currentLocation.getLatitude(), currentLocation.getLongitude()
            LatLng currentLatLng = new LatLng(31.8783807, 36.0174577);
            myMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
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
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
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
        startPointTxt = view.findViewById(R.id.startPoint);
        endPointTxt = view.findViewById(R.id.endPoint);
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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
//        startPoint = new Gson().fromJson(sharedPreferences.getString("StartPoint", ""), Point.class);
//        endPoint = new Gson().fromJson(sharedPreferences.getString("EndPoint", ""), Point.class);
//        sharedPreferences.edit().remove("StartPoint").remove("EndPoint").apply();
//        if (startPoint != null) {
//            startPointTxt.setText(startPoint.getPointName());
//            LatLng currentLatLng = new LatLng(startPoint.getX(), startPoint.getY());
//            startMarker = new MarkerOptions().position(currentLatLng).title(startPoint.getPointName());
//            myMap.addMarker(startMarker);
//            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
//            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
//            if (startPoint.getId() != 1) {
//                endPoint = new Gson().fromJson(
//                        sharedPreferences.getString("HashPoint", null),
//                        Point.class
//                );
//                endPointTxt.setEnabled(false);
//            } else
//                endPointTxt.setEnabled(true);
//        }
//        if (endPoint != null) {
//            endPointTxt.setText(endPoint.getPointName());
//            LatLng currentLatLng = new LatLng(endPoint.getX(), endPoint.getY());
//            endMarker = new MarkerOptions().position(currentLatLng).title(endPoint.getPointName());
//            myMap.addMarker(endMarker);
//            myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
//            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
//            if (endPoint.getId() != 1) {
//                startPoint = new Gson().fromJson(
//                        sharedPreferences.getString("HashPoint", null),
//                        Point.class
//                );
//                startPointTxt.setEnabled(false);
//            } else
//                startPointTxt.setEnabled(true);
//        }
//        if (startPoint != null && endPoint != null) {
//            PolylineOptions lineOptions = new PolylineOptions()
//                    .add(new LatLng(startPoint.getX(), startPoint.getY()))
//                    .add(new LatLng(endPoint.getX(), endPoint.getY()))
//                    .color(Color.RED) // Set desired color
//                    .width(5); // Set desired width
//
//            // Add the polyline to the map
//            Polyline line = myMap.addPolyline(lineOptions);
//            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//            boundsBuilder.include(new LatLng(startPoint.getX(), startPoint.getY()));
//            boundsBuilder.include(new LatLng(endPoint.getX(), endPoint.getY()));
//            LatLngBounds bounds = boundsBuilder.build();
//
//            int padding = 100; // Adjust padding as needed
//            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//            myMap.animateCamera(cu);
//        }
//    }


    //TODO, Solve NullPointerException Error When we restart The;
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        startPoint = new Gson().fromJson(sharedPreferences.getString("StartPoint", ""), Point.class);
        endPoint = new Gson().fromJson(sharedPreferences.getString("EndPoint", ""), Point.class);
//        sharedPreferences.edit().remove("StartPoint").remove("EndPoint").apply();

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

            if (startPoint.getId() != 1) {
                endPoint = new Gson().fromJson(sharedPreferences.getString("HashPoint", null), Point.class);
                endPointTxt.setEnabled(false);
            } else
                endPointTxt.setEnabled(true);
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

            if (endPoint.getId() != 1) {
                startPoint = new Gson().fromJson(sharedPreferences.getString("HashPoint", null), Point.class);
                startPointTxt.setEnabled(false);
            } else
                startPointTxt.setEnabled(true);
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