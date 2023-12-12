package com.hashimte.hashbus1.ui.ticket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hashimte.hashbus1.R;


public class BuyTicketFragment extends Fragment {


    private RecyclerView recyclerView;
    private BuyTicketData[] buyTicketData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_buy_ticket, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.buyTicketRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        buyTicketData = new BuyTicketData[]{
                new BuyTicketData("bus1",   22.),
                new BuyTicketData("bus1",   22.1),
                new BuyTicketData("bus1",  22.2),
                new BuyTicketData("bus1",   22.4),
                new BuyTicketData("bus1",   22.5),


        };

       BuyTicketAdapter buyTicketAdapter = new BuyTicketAdapter(buyTicketData, getContext());
        recyclerView.setAdapter(buyTicketAdapter);


    }
}