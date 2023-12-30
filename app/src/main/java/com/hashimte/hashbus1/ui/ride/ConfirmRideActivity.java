package com.hashimte.hashbus1.ui.ride;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hashimte.hashbus1.R;
import com.hashimte.hashbus1.api.UserServices;
import com.hashimte.hashbus1.api.UserServicesImp;
import com.hashimte.hashbus1.databinding.ActivityConfirmRideBinding;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.model.User;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Size;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Query;


public class ConfirmRideActivity extends AppCompatActivity {
    private ActivityConfirmRideBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private String urlText;

    private User user;
    private Gson gson;
    private SharedPreferences journeyPrefs;
    private SearchDataSchedule schedule;
    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmRideBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirm Ride");
        setContentView(binding.getRoot());
        gson = new Gson();
        journeyPrefs = getSharedPreferences("journey_prefs", MODE_PRIVATE);
        data = getIntent().getExtras();
        schedule = gson.fromJson(
                data.getString("searchData", "{}"),
                SearchDataSchedule.class
        );
        user = gson.fromJson(getSharedPreferences("app_prefs", MODE_PRIVATE).getString("userInfo", null), User.class);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            initQRCodeScanner();
        }
        binding.btnCancelConf.setOnClickListener(v -> {
            finish();
        });

    }

    private void initQRCodeScanner() {
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(ConfirmRideActivity.this);
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                bindImageAnalysis(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(ConfirmRideActivity.this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initQRCodeScanner();
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void bindImageAnalysis(ProcessCameraProvider processCameraProvider) {
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ConfirmRideActivity.this), image -> {
            Image mediaImage = image.getImage();
            if (mediaImage != null) {
                InputImage image1 = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                BarcodeScanner scanner = BarcodeScanning.getClient();
                Task<List<Barcode>> result = scanner.process(image1);
                result.addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        urlText = barcode.getRawValue();
                        binding.txtUrl.setText(urlText);
                    }
                    image.close();
                    mediaImage.close();
                });
                if (urlText != null) {
                    imageAnalysis.clearAnalyzer();
                    confirmNow(Integer.parseInt(urlText.substring(urlText.indexOf("#") + 1)));
                }
            }
        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(binding.preview.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);

    }

    private void confirmNow(Integer busId) {
        UserServicesImp.getInstance().confirmRide(user.getUserID(), schedule.getJourney().getId(), busId, schedule.getSchedule()).enqueue(
                new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            if (Boolean.TRUE.equals(response.body())) {
                                //TODO, Confirm
                                Toast.makeText(ConfirmRideActivity.this, "DONE", Toast.LENGTH_LONG).show();
                                binding.txtUrl.setText("DONE");
                                journeyPrefs.edit().putBoolean("confirmed", true).apply();
                            } else {
                                //TODO, Message
                                Toast.makeText(ConfirmRideActivity.this, "NOT DONE", Toast.LENGTH_LONG).show();
                                binding.txtUrl.setText("False");
                            }
                            urlText = null;
                            finish();
                        } else {
                            Toast.makeText(ConfirmRideActivity.this, "NOT DONE", Toast.LENGTH_LONG).show();
                            urlText = null;
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(ConfirmRideActivity.this, "NOT DONE", Toast.LENGTH_LONG).show();
                        binding.txtUrl.setText("NOT DONE");
                        urlText = null;
                    }
                }
        );
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