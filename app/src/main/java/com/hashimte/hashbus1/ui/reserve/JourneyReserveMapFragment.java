package com.hashimte.hashbus1.ui.reserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Routing;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServices;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.map.MyRoutingListener;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.ui.ticket.ShortestPath;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JourneyReserveMapFragment extends Fragment {

    private GoogleMap mMap;
    private Location myLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest locationRequest;
    private List<Point> pointForAJourney;
    private Point pick;
    private SearchDataSchedule schedule;
    private Point start;
    private Point end;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                    .PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationChangeListener(location -> myLocation = location);
                start = ((JourneyReserveView) getActivity()).getStart();
                end = ((JourneyReserveView) getActivity()).getEnd();
                pick = ((JourneyReserveView) getActivity()).getPick();
                schedule = ((JourneyReserveView) getActivity()).getSchedule();
                UserServicesImp.getInstance().getAllPointByJourneyId(schedule.getJourney().getId()).enqueue(
                        new Callback<List<Point>>() {
                            @Override
                            public void onResponse(Call<List<Point>> call, Response<List<Point>> response) {
                                if (response.isSuccessful()) {
                                    pointForAJourney = response.body();
                                    findRoutes(pointForAJourney);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Point>> call, Throwable t) {

                            }
                        }
                );
            }
        }

        public void findRoutes(List<Point> points) {
            if (points.get(0) == null || points.get(points.size() - 1) == null) {
                Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_LONG).show();
            } else {
                List<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(points.get(0).getX(), points.get(0).getY()));
                mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title("Start Point"));
                boolean x = pick != null;
                for (int i = 1; i < points.size() - 1; i++) {
                    LatLng latLng = new LatLng(points.get(i).getX(), points.get(i).getY());
                    latLngs.add(latLng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng).title(points.get(i).getPointName());
                    mMap.addMarker(markerOptions);
                }
                latLngs.add(new LatLng(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY()));
                mMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).title("End Point"));
                MyRoutingListener myRoutingListener = new MyRoutingListener(mMap, 6, getResources());
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(myRoutingListener)
                        .alternativeRoutes(true)
                        .waypoints(latLngs)
                        .key("AIzaSyBH4sMITOfvKIIq5Sa7HuGq7oikYEujTYs")
                        .build();
                routing.execute();
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journey_reserve_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_reserve);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(500);
            locationRequest.setFastestInterval(500);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("onLocationResult: ", locationResult.getLastLocation().toString());
        }
    };

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void endLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Hey!!!", "HEYYYYYYYYYYYYYYYYYYYYYY!!");
            return;
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onStart() {
        super.onStart();
        startLocationUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
        endLocationUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume() :::", "RESUMEEEEEEEEEEEEEEE");
    }

    private void content(boolean x){
//        if(!x || );
//        else {
//            refresh(1000);
//        }
    }

    private void refresh(int millisecond){
        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            content(true);
        };
        handler.postDelayed(runnable, millisecond);

    }
}