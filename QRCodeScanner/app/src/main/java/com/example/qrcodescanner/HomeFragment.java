package com.example.qrcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrcodescanner.databinding.FragmentHomeBinding;
import com.example.qrcodescanner.roomdatabase.Data;
import com.example.qrcodescanner.utils.CameraManager;
import com.example.qrcodescanner.utils.ScannerViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment {

    private int progressSeekBar = 0;
    private int maxSeekBar = 10;

    private Context context;
    private PreviewView previewView;
    private ScannerViewModel viewModel;
    private CameraManager cameraManager;
    private FragmentHomeBinding binding;

    private OnItemClickListener listener;

    private ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            cameraManager.scanQrFromGallery(context, imageUri);
                        }
                    }
            );

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        listener = (OnItemClickListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.bind(v);


        binding.btnCamera.setOnClickListener(view -> {
            cameraManager.switchCamera(getViewLifecycleOwner());
            progressSeekBar = 0;
            binding.zoomSlider.setProgress(progressSeekBar);
        });

        binding.btnFlash.setOnClickListener(view -> {
            cameraManager.toggleFlash();
        });

        binding.btnGallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });

        binding.btnInZoom.setOnClickListener(view -> {
            if (progressSeekBar + 1 <= maxSeekBar) progressSeekBar++;
            binding.zoomSlider.setProgress(progressSeekBar);
        });

        binding.btnOutZoom.setOnClickListener(view -> {
            if (progressSeekBar - 1 >= 0) progressSeekBar--;
            binding.zoomSlider.setProgress(progressSeekBar);
        });

        binding.zoomSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressSeekBar = i;
                cameraManager.setZoom((float) (((progressSeekBar * 1.0) / 10) + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewView = view.findViewById(R.id.preview_view);

        viewModel = new ViewModelProvider(this).get(ScannerViewModel.class);

        observeQrResult();
    }

    @Override
    public void onStart() {
        super.onStart();
        startCamera();
    }

    private void startCamera() {
        if (cameraManager == null) {
            cameraManager = new CameraManager(
                    getViewLifecycleOwner(),
                    requireContext(),
                    previewView,
                    viewModel::onQrScanned
            );
        }
    }

    private void observeQrResult() {
        viewModel.getQrResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {

                Log.d("Soliman", "observeQrResult: " + result);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new HistoryFragment()).commit();
                listener.dataClicked(true, new Data(1, result, getType(result), formateDate(), null));

                cameraManager.stopScanning();

                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    cameraManager.resumeScanning();
                    viewModel.reset();
                }, 2000);
            }
        });
    }

    private String formateDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return now.format(myFormatObj);
    }

    private String getType(String result) {
        String type = "Text";
        if (result.startsWith("http://") || result.startsWith("https://")) type = "Website";
        return type;
    }
}