package com.example.qrcodescanner;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qrcodescanner.databinding.FragmentGenarateQrBinding;
import com.example.qrcodescanner.generate.OneLevelActivity;
import com.example.qrcodescanner.generate.ThreeLevelActivity;
import com.example.qrcodescanner.generate.TwoLevelActivity;
import com.example.qrcodescanner.generateQR.QRType;
import com.example.qrcodescanner.utils.constants;


public class GenerateQR extends Fragment {

    private FragmentGenarateQrBinding binding;
    private Context context;

    private ActivityResultLauncher launcher;


    public GenerateQR() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated = inflater.inflate(R.layout.fragment_genarate_qr, container, false);
        binding = FragmentGenarateQrBinding.bind(inflated);

        Intent intent = new Intent(new Intent(context, OneLevelActivity.class));

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap QR = data.getParcelableExtra(OneLevelActivity.RESULT_QR);
                        QRType type = data.getSerializableExtra(OneLevelActivity.TYPE_QR, QRType.class);
                        String rData = data.getStringExtra(OneLevelActivity.DATA_QR);
                        ShowQRFragment qrFragment = ShowQRFragment.newInstance(true,QR, type, rData);
                        getParentFragmentManager()
                                .beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.fragmentContainer, qrFragment)
                                .commit();
                    }
                }
        );

        binding.btnSettings.setOnClickListener(view -> {
            startActivity(new Intent(context, SettingActivity.class));
        });

        binding.text.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.TEXT);
            launcher.launch(intent);
        });

        binding.website.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.WEBSITE);
            launcher.launch(intent);
        });

        binding.whatsapp.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.WHATSAPP);
            launcher.launch(intent);
        });

        binding.x.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.X);
            launcher.launch(intent);
        });

        binding.email.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.EMAIL);
            launcher.launch(intent);
        });

        binding.instagram.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.INSTAGRAM);
            launcher.launch(intent);
        });

        binding.telephone.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.PHONE);
            launcher.launch(intent);
        });

        binding.WiFi.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.WI_FI);
            launcher.launch(intent);
        });

        binding.event.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.EVENT);
            launcher.launch(intent);
        });

        binding.contact.setOnClickListener(view -> {
            launcher.launch(new Intent(context, TwoLevelActivity.class));
        });

        binding.business.setOnClickListener(view -> {
            launcher.launch(new Intent(context, ThreeLevelActivity.class));
        });

        binding.location.setOnClickListener(view -> {
            intent.putExtra(constants.GENERATE_TYPE, constants.LOCATION);
            launcher.launch(intent);
        });

        return inflated;
    }
}