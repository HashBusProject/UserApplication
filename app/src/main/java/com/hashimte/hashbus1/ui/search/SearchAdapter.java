package com.hashimte.hashbus1.ui.search;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hashimte.hashbus1.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private SearchData[] searchData;
    private Context context;


    public SearchAdapter(SearchData[] searchData, Context context) {
        this.searchData = searchData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        final SearchData searchData1 = searchData[position];
        holder.startLocation.setText(searchData1.getStartLocation());
        holder.endLocation.setText(searchData1.getEndLocation());
        holder.waitTime.setText(Integer.toString(searchData1.getWaitTime()));
        holder.waitMinTime.setText(searchData1.getWaitMinTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, searchData1.getStartLocation(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchData.length;
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
