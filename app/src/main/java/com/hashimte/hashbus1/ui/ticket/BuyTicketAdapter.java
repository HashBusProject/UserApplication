package com.hashimte.hashbus1.ui.ticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.model.Journey;
import com.hashimte.hashbus1.model.User;

import java.text.DecimalFormat;
import java.util.List;

public class BuyTicketAdapter extends RecyclerView.Adapter<BuyTicketAdapter.ViewHolder> {

    private List<Journey> journeyList;
    private Context context;

    public BuyTicketAdapter(List<Journey> journeyList, Context context) {
        this.journeyList = journeyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.buy_ticket_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Journey journey = journeyList.get(position);
        Log.i("Journey", journey.toString());
        holder.buyJourneyName.setText(journey.getName());
        DecimalFormat decimalFormat1 = new DecimalFormat("#.##");
        holder.buyPrice.setText(decimalFormat1.format(journey.getPrice()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ShortestPath.class);
            Bundle extras = new Bundle();
            String data = new Gson().toJson(journey);
            extras.putString("data", data);
            extras.putString("user", context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).getString("userInfo", null));
            intent.putExtras(extras);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return journeyList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView buyJourneyName, buyPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buyJourneyName = itemView.findViewById(R.id.BuyJourneyName);
            buyPrice = itemView.findViewById(R.id.BuyPrice);
        }
    }
}