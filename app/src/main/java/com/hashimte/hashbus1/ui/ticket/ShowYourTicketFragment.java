package com.hashimte.hashbus1.ui.ticket;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.model.Ticket;
import com.hashimte.hashbus1.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowYourTicketFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Ticket> tickets;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_your_ticket, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.showTicketRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        User user = new Gson().fromJson(
                getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        .getString("userInfo", null),
                User.class
        );
        UserServicesImp.getInstance().getTicketsByUserId(user.getUserID()).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if(response.isSuccessful()){
                    tickets = response.body();
                    YourTicketAdapter yourTicketAdapter = new YourTicketAdapter(tickets, getContext());
                    recyclerView.setAdapter(yourTicketAdapter);
                }
                else {
                    // TODO Handle this.
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                // TODO Handle this.
                Toast.makeText(getContext(), "Error onFailure!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}