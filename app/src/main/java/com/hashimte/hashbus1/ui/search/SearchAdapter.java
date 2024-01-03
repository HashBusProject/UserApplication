package com.hashimte.hashbus1.ui.search;


import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
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
import com.hashimte.hashbus1.map.DirectionsTask;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.ui.reserve.JourneyReserveView;

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
    private String min;


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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        final SearchDataSchedule searchData1 = searchData.get(position);
        if (searchData1.getSchedule().getNextPoint() > 0) {
            LatLng busLatLng = new LatLng(searchData1.getBus().getX(), searchData1.getBus().getY());
            LatLng pickPointLatLng = new LatLng(startPoint.getX(), startPoint.getY());
            new DirectionsTask(busLatLng, pickPointLatLng){
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    holder.waitTime.setText(s);
                    min = s;
                }
            }.execute();
        }
        holder.startLocation.setText(searchData1.getJourney().getName());
        holder.endLocation.setText(searchData1.getSchedule().getTime().toString());
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
                                    data.putString("pickPoint", new Gson().toJson(response.body()));
                                } else {
                                    data.putString("pickPoint", new Gson().toJson(startPoint));
                                    data.putBoolean("isSame", false);
                                }
                                data.putString("searchData", new Gson().toJson(searchData1));
                                data.putString("startPointData", new Gson().toJson(startPoint));
                                data.putString("endPointData", new Gson().toJson(endPoint));
                                data.putString("timeToArrive", min == null ? holder.waitTime.getText().toString() : min);
                                intent.putExtras(data);
                                context.startActivity(intent);
                            } else {
                                Log.e("onResponse :", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Point> call, Throwable t) {
                            Log.e("onFailure :", t.getMessage());
                        }
                    }
            );

        });
    }



    @Override
    public int getItemCount() {
        return searchData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView startLocation, endLocation, waitTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            waitTime = itemView.findViewById(R.id.waitTime);
        }
    }
}
