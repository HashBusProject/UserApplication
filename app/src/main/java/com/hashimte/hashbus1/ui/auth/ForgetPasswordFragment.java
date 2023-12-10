package com.hashimte.hashbus1.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.AuthServicesImp;
import com.hashimte.hashbus1.databinding.FragmentForgetPasswordBinding;
import com.hashimte.hashbus1.databinding.FragmentLoginBinding;
import com.hashimte.hashbus1.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment {
    private FragmentForgetPasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText email = binding.txtInputEmail;
        TextInputLayout passwordLayout1 = binding.txtInputPasswordLayoutForgetPassword;
        TextInputLayout passwordLayout2 = binding.txtInputPasswordLayoutForgetPassword2;
        TextInputLayout emailLayout = binding.txtInputEmailLayout;
        TextInputEditText password1 = binding.txtInputPassword1;
        TextInputEditText password2 = binding.txtInputPassword2;
        Button findAccount = binding.Findaccount;
        emailLayout.setErrorEnabled(true);
        passwordLayout1.setErrorEnabled(true);
        passwordLayout2.setErrorEnabled(true);
        findAccount.setOnClickListener(v -> {
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
            if(x) return;
            AuthServicesImp service = AuthServicesImp.getInstance();
            User user = new User();
            user.setEmail(email.getText().toString());
            user.setPassword(password1.getText().toString());
            service.changePassword(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Login NOW!", Toast.LENGTH_SHORT).show();
                    } else {
                        email.setError("There Is no Account With This Email!");
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    emailLayout.setError("There Is no Account With This Email!");
                    Log.e("Error", "Error is " + t.getMessage());
                }
            });
        });
        email.addTextChangedListener(clearError(emailLayout));
        password1.addTextChangedListener(clearError(passwordLayout1));
        password2.addTextChangedListener(clearError(passwordLayout2));
    }

    private TextWatcher clearError(TextInputLayout v) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                v.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }
}