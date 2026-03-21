package com.example.qrcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qrcodescanner.databinding.FragmentResultBinding;
import com.example.qrcodescanner.generateQR.QRType;
import com.example.qrcodescanner.roomdatabase.AppDatabase;
import com.example.qrcodescanner.roomdatabase.Data;
import com.example.qrcodescanner.roomdatabase.DataDao;
import com.example.qrcodescanner.roomdatabase.DatabaseClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ResultFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_IS_INSERT = "isInsert";
    private Data data;
    private boolean isInsert;

    private FragmentResultBinding binding;

    private Executor executor;
    private AppDatabase db;
    private DataDao dataDao;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(boolean isInsert, Data param1) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_INSERT, isInsert);
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isInsert = getArguments().getBoolean(ARG_IS_INSERT);
            data = getArguments().getSerializable(ARG_PARAM1, Data.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_result, container, false);
        binding = FragmentResultBinding.bind(inflated);
        executor = Executors.newSingleThreadExecutor();
        binding.data.setText(data.QRType);
        binding.date.setText(data.QRDateAndTime);
        binding.url.setText(data.QRContent);

        db = DatabaseClient.getInstance(getContext());
        dataDao = db.dataDao();

        if (data.historyType == 1){
            binding.showQR.setVisibility(View.GONE);
            binding.btnShare.setVisibility(View.GONE);
            binding.textView.setVisibility(View.GONE);
        }

        if (isInsert) {
            executor.execute(() -> {
                dataDao.insert(data);
            });
        }

        binding.btnBack.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        binding.showQR.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, ShowQRFragment.newInstance(false, data.QRImage, typeMap(data.QRType), data.QRContent)).commit();
        });

        binding.btnCopy.setOnClickListener(view -> {
            requireContext().getSystemService(ClipboardManager.class)
                    .setPrimaryClip(ClipData.newPlainText("QR Code", data.QRContent));
            Toast.makeText(requireContext(), "Text Copied", Toast.LENGTH_SHORT).show();
        });

        return inflated;
    }

    private QRType typeMap(String type) {
        switch (type) {
            case "Text":
                return QRType.TEXT;
            case "Website":
                return QRType.WEBSITE;
            case "Wifi":
                return QRType.WiFi;
            case "Event":
                return QRType.EVENT;
            case "Location":
                return QRType.LOCATION;
            case "Business":
                return QRType.BUSINESS;
            default:
                return QRType.TEXT;
        }
    }
}