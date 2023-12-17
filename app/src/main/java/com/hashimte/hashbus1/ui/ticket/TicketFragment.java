package com.hashimte.hashbus1.ui.ticket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.databinding.FragmentTicketBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment {

    private FragmentTicketBinding binding;

    public TicketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        binding = FragmentTicketBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cardBuyTicket.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            // Define the destination ID of the other fragment
            int destinationId = R.id.buyTicketFragment;

            // Navigate to the other fragment
            navController.navigate(destinationId);
        });
        binding.cardShowYourTickets.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            // Define the destination ID of the other fragment
            int destinationId = R.id.showYourTicketFragment;

            // Navigate to the other fragment
            navController.navigate(destinationId);
        });
    }
}