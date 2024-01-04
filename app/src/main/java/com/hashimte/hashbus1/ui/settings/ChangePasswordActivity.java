package com.hashimte.hashbus1.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServices;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.databinding.ActivityChangePasswordBinding;
import com.hashimte.hashbus1.model.ChangePassword;
import com.hashimte.hashbus1.model.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        Gson gson = new Gson();
        SharedPreferences appPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        User user = gson.fromJson(appPrefs.getString("userInfo", null), User.class);
        binding.btnChangePassword.setOnClickListener(v -> {
            if (!(binding.retypePass.getText().toString().equals(binding.newPass.getText().toString()))) {
                Toast.makeText(this, "New password does not match with repeated password", Toast.LENGTH_SHORT).show();
                return;
            }
            user.setPassword(binding.currentPass.getText().toString());
            if(binding.newPass.getText().toString().equals(user.getPassword())){
                Toast.makeText(this, "New password does match with old password", Toast.LENGTH_SHORT).show();
                return;
            }
            ChangePassword changePassword = new ChangePassword(
                    user, binding.newPass.getText().toString());
            UserServicesImp.getInstance().changePassword(changePassword).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Toast.makeText(ChangePasswordActivity.this, "Password change successfully", Toast.LENGTH_SHORT).show();
                    user.setPassword(null);
                    finish();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.i("saif" , "Error from saif alhara") ;
                }
            });
        });
    }
    @Override
    public boolean onOptionsItemSelected(@lombok.NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}