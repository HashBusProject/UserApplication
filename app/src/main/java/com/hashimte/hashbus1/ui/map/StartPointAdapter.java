package com.hashimte.hashbus1.ui.map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.model.Point;

import java.util.List;

import lombok.NonNull;

public class StartPointAdapter extends RecyclerView.Adapter<StartPointAdapter.ViewHolder> {

    private Context context;
    private List<Point> data;

    public StartPointAdapter(Context context, List<Point> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<Point> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.point_search_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Point point = data.get(position);
        holder.pointTextView.setText(point.getPointName());
        holder.itemView.setOnClickListener(v -> {
            String pointType = ((Activity) context).getIntent().getExtras().getString("pointType", null);
            SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            String jsonString = new Gson().toJson(point);
            sharedPreferences.edit().putString(pointType + "Point", jsonString).apply();
            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pointTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pointTextView = itemView.findViewById(R.id.point_name);
        }
    }
}
