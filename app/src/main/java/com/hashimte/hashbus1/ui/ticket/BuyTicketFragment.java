package com.hashimte.hashbus1.ui.ticket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.model.Journey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuyTicketFragment extends Fragment {


    private RecyclerView recyclerView;
    private List<Journey> journeys;

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
        UserServicesImp.getInstance().getAllJourneys().enqueue(new Callback<List<Journey>>() {
            @Override
            public void onResponse(@NonNull Call<List<Journey>> call, @NonNull Response<List<Journey>> response) {
                if (response.isSuccessful()) {
                    journeys = response.body();
                    BuyTicketAdapter buyTicketAdapter = new BuyTicketAdapter(journeys, getContext());
                    recyclerView.setAdapter(buyTicketAdapter);
                } else {
                    //TODO, handle error
                }
            }

            @Override
            public void onFailure(Call<List<Journey>> call, Throwable t) {
                //TODO, handle error
            }
        });
    }
}