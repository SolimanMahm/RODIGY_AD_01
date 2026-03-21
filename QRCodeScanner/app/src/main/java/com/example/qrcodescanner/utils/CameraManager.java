package com.example.qrcodescanner.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CameraManager {

    private ImageAnalysis imageAnalysis;
    private boolean isScanning = true;

    private SharedPreferences sharedPreferences;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    private ProcessCameraProvider cameraProvider;
    private Preview preview;
    private Camera camera;
    private Consumer<String> onResult;

    private final BarcodeScanner scanner =
            BarcodeScanning.getClient(
                    new BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                            .build()
            );

    private boolean vibrateEnabled;
    private boolean beepEnabled;
    private ToneGenerator toneGenerator;

    public CameraManager(
            LifecycleOwner lifecycleOwner,
            Context context,
            PreviewView previewView,
            Consumer<String> onResult
    ) {

        sharedPreferences = context.getSharedPreferences(constants.SETTING, Context.MODE_PRIVATE);
        vibrateEnabled = sharedPreferences.getBoolean(constants.VIBRATE, false);
        beepEnabled = sharedPreferences.getBoolean(constants.BEEP, false);
        this.onResult = onResult;

        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(context);

        future.addListener(() -> {
            try {
                cameraProvider = future.get();

                preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(
                        Executors.newSingleThreadExecutor(),
                        imageProxy -> {
                            if (!isScanning) {
                                imageProxy.close();
                                return;
                            }

                            @SuppressLint("UnsafeOptInUsageError")
                            Image mediaImage = imageProxy.getImage();

                            if (mediaImage == null) {
                                imageProxy.close();
                                return;
                            }

                            InputImage image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.getImageInfo().getRotationDegrees()
                            );

                            scanner.process(image)
                                    .addOnSuccessListener(barcodes -> {
                                        if (!barcodes.isEmpty()) {
                                            isScanning = false;
                                            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                                                vibrate(context);
                                            });
                                            beep();
                                            onResult.accept(barcodes.get(0).getRawValue());
                                        }
                                    })
                                    .addOnCompleteListener(task -> imageProxy.close());
                        }
                );

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    public void stopScanning() {
        isScanning = false;
    }

    public void resumeScanning() {
        isScanning = true;
    }

    public void switchCamera(LifecycleOwner lifecycleOwner) {
        if (cameraProvider == null || preview == null || imageAnalysis == null) return;
        cameraSelector = (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                ? CameraSelector.DEFAULT_FRONT_CAMERA
                : CameraSelector.DEFAULT_BACK_CAMERA;

        cameraProvider.unbindAll();
        camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
        );

        if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA)
            camera.getCameraControl().enableTorch(false);
    }

    public void toggleFlash() {
        if (camera == null) return;

        if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) return;

        boolean isOn =
                camera.getCameraInfo().getTorchState().getValue()
                        == TorchState.ON;

        camera.getCameraControl().enableTorch(!isOn);
    }

    public void setZoom(float zoom) {
        if (camera == null) return;

        camera.getCameraControl().setZoomRatio(zoom);
    }

    public void scanQrFromGallery(Context context, Uri imageUri) {
        try {
            stopScanning();

            InputImage image =
                    InputImage.fromFilePath(context, imageUri);

            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        if (!barcodes.isEmpty()) {
                            String result = barcodes.get(0).getRawValue();
                            if (result != null) onResult.accept(result);
                        }
                    })
                    .addOnFailureListener(Throwable::printStackTrace)
                    .addOnCompleteListener(task -> resumeScanning());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vibrate(Context context) {
        if (!vibrateEnabled) return;

        Vibrator vibrator =
                (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator == null || !vibrator.hasVibrator()) return;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                        VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE)
                );
            } else vibrator.vibrate(120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void beep() {
        if (!beepEnabled) return;

        try {
            if (toneGenerator == null) {
                toneGenerator =
                        new ToneGenerator(
                                AudioManager.STREAM_MUSIC, 80
                        );
            }

            toneGenerator.startTone(
                    ToneGenerator.TONE_PROP_BEEP, 150
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
