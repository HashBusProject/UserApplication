package com.hashimte.hashbus1.ui.ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.ui.search.SearchAdapter;
import com.hashimte.hashbus1.ui.search.SearchData;

import java.text.DecimalFormat;

public class YourTicketAdapter extends RecyclerView.Adapter<YourTicketAdapter.ViewHolder> {

    private YourTikectData[] yourTikectData;
    private Context context;

    public YourTicketAdapter(YourTikectData[] yourTikectData, Context context) {
        this.yourTikectData = yourTikectData;
        this.context = context;
    }

    @NonNull
    @Override
    public YourTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.show_ticket_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourTicketAdapter.ViewHolder holder, int position) {
        final YourTikectData yourTikectData1= yourTikectData[position];
        holder.journeyName.setText(yourTikectData1.getJourneyName());
        holder.ticketId.setText(Integer.toString(yourTikectData1.getTicketId()));

        DecimalFormat decimalFormat=new DecimalFormat("#.##");

        holder.price.setText(decimalFormat.format(yourTikectData1.getPrice()));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context, searchData1.getStartLocation(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return yourTikectData.length;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView journeyName, ticketId, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            journeyName = itemView.findViewById(R.id.journeyName);
            ticketId = itemView.findViewById(R.id.ticketId);
            price = itemView.findViewById(R.id.price);

        }
    }
}