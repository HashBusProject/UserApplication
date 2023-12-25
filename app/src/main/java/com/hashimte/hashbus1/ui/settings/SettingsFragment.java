package com.hashimte.hashbus1.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hashimte.hashbus1.LauncherActivity;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentSettingsBinding.inflate(inflater,container,false);
         return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.logout.setOnClickListener(view1 -> {
            getActivity().getSharedPreferences("app_prefs",Context.MODE_PRIVATE).edit()
                    .clear()
                    .apply();
            Intent intent=new Intent(getContext(), LauncherActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        binding.changepass.setOnClickListener(view1 -> {
    Intent intent =new Intent(getContext(), ChangePasswordActivity.class);
     startActivity(intent);

          
        });

        binding.changeemail.setOnClickListener(view1 -> {
            Intent intent =new Intent(getContext(), ChangeEmailActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}