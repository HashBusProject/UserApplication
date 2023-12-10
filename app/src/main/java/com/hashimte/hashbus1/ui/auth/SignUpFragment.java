package com.hashimte.hashbus1.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hashimte.hashbus1.MainActivity;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.AuthServicesImp;
import com.hashimte.hashbus1.databinding.FragmentSignUpBinding;
import com.hashimte.hashbus1.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText username = binding.txtInputUsername;
        TextInputEditText password1 = binding.txtInputPassword;
        TextInputEditText password2 = binding.txtInputPassword2;
        TextInputEditText firstName = binding.txtInputFirstname;
        TextInputEditText email = binding.txtInputEmail;
        TextInputLayout userLayout = binding.txtInputUsernameLayout;
        TextInputLayout nameLayout = binding.txtInputFirstnameLayout;
        TextInputLayout passwordLayout1 = binding.txtInputPasswordLayout;
        TextInputLayout passwordLayout2 = binding.txtInputPasswordLayout2;
        TextInputLayout emailLayout = binding.txtInputEmailLayout;
        Button signUp = binding.signupbutton;
        signUp.setOnClickListener(v -> {
            v.setEnabled(false);
            boolean x = false;
            if (!password1.getText().toString().equals(password2.getText().toString())) {
                passwordLayout2.setError("The Password Should be The Same!");
                x = true;
            }
            if (email.getText().toString().isEmpty() || email.getText() == null) {
                emailLayout.setError("Fill the Email Please!!");
                x = true;
            }
            if (password1.getText().toString().isEmpty() || password1.getText() == null) {
                x = true;
                passwordLayout1.setError("The Password Is Empty!!");
            }
            if (password2.getText().toString().isEmpty() || password2.getText() == null) {
                x = true;
                passwordLayout2.setError("The Confirm Password Is Empty!!");
            }
            if (username.getText().toString().isEmpty() || username.getText() == null) {
                x = true;
                userLayout.setError("Username Is Empty!!");
            }
            if (firstName.getText().toString().isEmpty() || firstName.getText() == null) {
                x = true;
                nameLayout.setError("name Is Empty!!");
            }
            if (x) return;
            AuthServicesImp service = AuthServicesImp.getInstance();
            User user = new User();
            user.setUsername(username.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(password2.getText().toString());
            user.setName(firstName.getText().toString());

            // TODO, Add messages for the errors.
            service.SignUp(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        startActivity(new Intent(view.getContext(), MainActivity.class));
                        getActivity().finishAffinity();
                        getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                .edit().putBoolean("isLoggedIn", true).apply();
                        getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                .edit().putString("username", response.body().getUsername()).apply();
                        getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                .edit().putString("email", response.body().getEmail()).apply();
                        getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                .edit().putString("name", response.body().getName()).apply();
                    }
                    else {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        Log.e("Error", response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
            });
        });
    }
}