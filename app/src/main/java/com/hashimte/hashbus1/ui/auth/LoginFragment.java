package com.hashimte.hashbus1.ui.auth;

import androidx.fragment.app.FragmentManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hashimte.hashbus1.MainActivity;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.AuthServicesImp;
import com.hashimte.hashbus1.databinding.FragmentLoginBinding;
import com.hashimte.hashbus1.model.User;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText usernameEditText = binding.txtInputUsernameOrEmail;
        final EditText passwordEditText = binding.txtInputPassword;
        final Button loginButton = binding.loginButton;
        final TextView forgetPassword = binding.forgetPassword;
        final TextView createAccount = binding.createnewaccount;
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                AuthServicesImp authServicesImp = AuthServicesImp.getInstance();
                Log.e("User", "I'm Here");
                User authUser = new User();
                authUser.setUsername(usernameEditText.getText().toString());
                authUser.setPassword(passwordEditText.getText().toString());
                authServicesImp.login(authUser).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
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

                        } else {
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.errorBody().toString());
                            usernameEditText.setText(null);
                            passwordEditText.setText(null);
                            loginButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("Error: ", t.getMessage());
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(true);
                    }
                });
            }
        });

        forgetPassword.setOnClickListener((View v) -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, ForgetPasswordFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();
        });

        createAccount.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, SignUpFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}