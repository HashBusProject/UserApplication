package com.hashimte.hashbus1.ui.search;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.android.material.textview.MaterialTextView;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.ui.reserve.JourneyReserveView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchDataSchedule> searchData;
    private Context context;
    private Point startPoint;
    private Point endPoint;


    public SearchAdapter(List<SearchDataSchedule> searchData, Context context, Point startPoint, Point endPoint) {
        this.searchData = searchData;
        this.context = context;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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
                        //TODO,
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
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, JourneyReserveView.class);
            Bundle data = new Bundle();
            UserServicesImp.getInstance().getPointByID(searchData1.getJourney().getSourcePoint()).enqueue(
                    new Callback<Point>() {
                        @Override
                        public void onResponse(Call<Point> call, Response<Point> response) {
                            if (response.isSuccessful()) {
                                if (searchData1.getJourney().getSourcePoint() == startPoint.getId()) {
                                    data.putBoolean("isSame", true);
                                } else {
                                    data.putString("pickPoint", new Gson().toJson(response.body()));
                                    data.putBoolean("isSame", false);
                                }
                                data.putString("searchData", new Gson().toJson(searchData1));
                                data.putString("startPointData", new Gson().toJson(startPoint));
                                data.putString("endPointData", new Gson().toJson(startPoint));
                                intent.putExtras(data);
                                context.startActivity(intent);
                            } else {
                                Log.e("onSe :", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Point> call, Throwable t) {
                            Log.e("onFailuer :", t.getMessage());
                        }
                    }
            );

        });
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
