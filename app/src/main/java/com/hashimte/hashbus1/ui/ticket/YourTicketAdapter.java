package com.hashimte.hashbus1.ui.ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.model.Ticket;

import java.text.DecimalFormat;
import java.util.List;

public class YourTicketAdapter extends RecyclerView.Adapter<YourTicketAdapter.ViewHolder> {

    private List<Ticket> tickets;
    private Context context;

    public YourTicketAdapter(List<Ticket> tickets, Context context) {
        this.tickets = tickets;
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
        final Ticket ticket = tickets.get(position);
        holder.journeyName.setText(ticket.getJourney().getName());
        holder.ticketId.setText(Integer.toString(ticket.getId()));

        DecimalFormat decimalFormat = new DecimalFormat("#.## JD");

        holder.price.setText(decimalFormat.format(ticket.getJourney().getPrice()));

        holder.itemView.setOnClickListener(view -> {
            // Toast.makeText(context, searchData1.getStartLocation(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return tickets.size();
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