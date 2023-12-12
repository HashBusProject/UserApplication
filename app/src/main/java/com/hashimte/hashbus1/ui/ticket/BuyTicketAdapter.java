package com.hashimte.hashbus1.ui.ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hashimte.hashbus1.R;

import java.text.DecimalFormat;

public class BuyTicketAdapter extends RecyclerView.Adapter<BuyTicketAdapter.ViewHolder> {

    private BuyTicketData [] buyTicketData;
    private Context context;

    public BuyTicketAdapter(BuyTicketData[] buyTicketData, Context context) {
        this.buyTicketData = buyTicketData;
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
        final BuyTicketData buyTicketData1= buyTicketData[position];
        holder.buyJourneyName.setText(buyTicketData1.getBuyYourneyName());


        DecimalFormat decimalFormat1=new DecimalFormat("#.##");

        holder.buyPrice.setText(decimalFormat1.format(buyTicketData1.getBuyTicketPrice()));

    }

    @Override
    public int getItemCount() {
        return buyTicketData.length;
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