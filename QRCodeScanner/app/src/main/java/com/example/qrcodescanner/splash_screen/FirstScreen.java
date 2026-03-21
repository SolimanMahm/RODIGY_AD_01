package com.example.qrcodescanner.splash_screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.qrcodescanner.MainActivity;
import com.example.qrcodescanner.databinding.ActivityFirstScreenBinding;

public class FirstScreen extends AppCompatActivity {

    private ActivityFirstScreenBinding binding;

    private static final int PERMISSION_REQUEST_CODE = 2026;

    private String[] permission = {
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(() -> {
            binding.textView.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.VISIBLE);
        }, 3000);

        requestPermissions(permission, PERMISSION_REQUEST_CODE);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

    }

    private void checkPermissions() {


        boolean allGranted = true;
        for (String p : permission) {
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (allGranted) startMainActivity();
        else {
            requestPermissions(permission, PERMISSION_REQUEST_CODE);
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(FirstScreen.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted) startMainActivity();
            else Toast.makeText(this, "Camera and Vibrate are required", Toast.LENGTH_SHORT).show();
        }
    }
}