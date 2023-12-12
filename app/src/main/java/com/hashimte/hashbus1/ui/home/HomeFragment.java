package com.hashimte.hashbus1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.databinding.FragmentHomeBinding;
import com.hashimte.hashbus1.ui.map.MapsFragment;
import com.hashimte.hashbus1.ui.ticket.BuyTicketFragment;
import com.hashimte.hashbus1.ui.ticket.ShowYourTicketFragment;
import com.hashimte.hashbus1.ui.ticket.TicketFragment;

public class HomeFragment extends ShowYourTicketFragment{
    //MapsFragment

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_show_your_ticket, container, false);
//fragment_maps

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}