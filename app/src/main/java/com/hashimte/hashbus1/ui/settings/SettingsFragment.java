package com.hashimte.hashbus1.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hashimte.hashbus1.LauncherActivity;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.databinding.FragmentHomeBinding;
import com.hashimte.hashbus1.databinding.FragmentSettingsBinding;
import com.hashimte.hashbus1.ui.home.HomeViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        button = view.findViewById(R.id.btn_logout);
        button.setOnClickListener(v -> {
            getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn", false).apply();
            Intent intent = new Intent(getActivity(), LauncherActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}