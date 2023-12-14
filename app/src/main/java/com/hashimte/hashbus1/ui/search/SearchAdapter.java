package com.hashimte.hashbus1.ui.search;


import android.content.Context;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.android.material.textview.MaterialTextView;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.google.maps.model.TravelMode;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
//    private SearchData[] searchData;
    private List<SearchDataSchedule> searchData;
    private Context context;

    private Point startPoint;


    public SearchAdapter(List<SearchDataSchedule> searchData, Context context, Point startPoint) {
        this.searchData = searchData;
        this.context = context;
        this.startPoint = startPoint;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_card, parent, false);
        return new ViewHolder(view);
    }

    interface Fun {
        void apply();
    }

    private void runOnUiThread(Fun fun) {
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        final SearchDataSchedule searchData1 = searchData.get(position);
        Thread thread = new Thread(() -> {
            try {
                DirectionsResult directionsResult = DirectionsApi.newRequest(getGeoContext())
                        .mode(TravelMode.DRIVING)
                        .origin(new LatLng(32.103113, 36.180002))
                        .destination(new LatLng(32.058997, 36.066677))
                        .await();

                runOnUiThread(() -> {
                    if (directionsResult != null) {
                        // Update UI with directions information
                        DirectionsRoute route = directionsResult.routes[0];
                        DirectionsLeg leg = route.legs[0];
                        Duration duration = leg.duration;
                        String min = duration.humanReadable;
                        holder.waitTime.setText(min);
                    } else {
                        // Handle failure to get directions
                        Toast.makeText(context, "Error getting directions", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException | InterruptedException | IOException e) {
                Toast.makeText(context, "Error Handel", Toast.LENGTH_SHORT).show();
            }
        });
        thread.start();
        holder.startLocation.setText(searchData1.getJourney().getName());
        holder.endLocation.setText(searchData1.getSchedule().getTime().toString());
        holder.waitMinTime.setText("min");
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, searchData1.getStartLocation(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey("AIzaSyDyZbg_i9y9T5_tdCRX3evZdD89rKPMWqk")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView startLocation, endLocation, waitMinTime, waitTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            waitTime = itemView.findViewById(R.id.waitTime);
            waitMinTime = itemView.findViewById(R.id.waitMinTime);
        }
    }
}
