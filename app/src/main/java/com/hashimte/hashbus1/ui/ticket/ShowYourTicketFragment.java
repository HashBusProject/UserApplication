package com.hashimte.hashbus1.ui.ticket;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hashimte.hashbus1.R;



public class ShowYourTicketFragment extends Fragment {
    private RecyclerView recyclerView;
    private YourTikectData[] yourTikectData;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState ){


        View view=inflater.inflate(R.layout.fragment_show_your_ticket, container, false);


      return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.showTicketRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        yourTikectData = new YourTikectData[]{
                new YourTikectData("bus1",  22, 22),
                new YourTikectData("bus1",  22, 22.1),
                new YourTikectData("bus1",  22, 22.2),
                new YourTikectData("bus1",  22, 22.4),
                new YourTikectData("bus1",  22, 22.5),
                new YourTikectData("bus1",  22, 22.6),
                new YourTikectData("bus1",  22, 22.9),

        };

        YourTicketAdapter yourTicketAdapter= new YourTicketAdapter(yourTikectData, getContext());
        recyclerView.setAdapter(yourTicketAdapter);


    }
}