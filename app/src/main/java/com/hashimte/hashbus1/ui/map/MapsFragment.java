package com.hashimte.hashbus1.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import com.hashimte.hashbus1.R;

public class MapsFragment extends Fragment {
    private final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myMap;
    private Button timer;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            myMap = googleMap;

                    //currentLocation.getLatitude(), currentLocation.getLongitude()
            LatLng currentLatLng = new LatLng( 31.8783807, 36.0174577);
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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}