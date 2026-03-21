package com.example.qrcodescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.qrcodescanner.databinding.FragmentShowQrBinding;
import com.example.qrcodescanner.generateQR.QRType;
import com.example.qrcodescanner.roomdatabase.AppDatabase;
import com.example.qrcodescanner.roomdatabase.Data;
import com.example.qrcodescanner.roomdatabase.DataDao;
import com.example.qrcodescanner.roomdatabase.DatabaseClient;
import com.example.qrcodescanner.utils.SaveImage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ShowQRFragment extends Fragment {

    private static final String ARG_TYPE = "type";
    private static final String ARG_DATA = "data";
    private static final String ARG_QR = "QR";
    private static final String ARG_IS_INSERT = "isInsert";

    private QRType mType;
    private String mData;
    private Bitmap mQR;
    private boolean mIsInsert;

    private FragmentShowQrBinding binding;

    private AppDatabase db;
    private DataDao dataDao;
    private Executor executor;

    public ShowQRFragment() {
        // Required empty public constructor
    }

    public static ShowQRFragment newInstance(boolean isInsert, Bitmap qr, QRType type, String data) {
        ShowQRFragment fragment = new ShowQRFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_INSERT, isInsert);
        args.putSerializable(ARG_TYPE, type);
        args.putString(ARG_DATA, data);
        args.putParcelable(ARG_QR, qr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsInsert = getArguments().getBoolean(ARG_IS_INSERT);
            mType = getArguments().getSerializable(ARG_TYPE, QRType.class);
            mData = getArguments().getString(ARG_DATA);
            mQR = getArguments().getParcelable(ARG_QR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated = inflater.inflate(R.layout.fragment_show_qr, container, false);
        binding = FragmentShowQrBinding.bind(inflated);
        executor = Executors.newSingleThreadExecutor();

        db = DatabaseClient.getInstance(getContext());
        dataDao = db.dataDao();


        binding.btnBack.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        String tempType = mType.name().toLowerCase();
        final String type = tempType.substring(0, 1).toUpperCase() + tempType.substring(1);

        binding.type.setText(type);
        if (mIsInsert) binding.data.setText(showData(mType));
        else binding.data.setText(mData);
        binding.qrImage.setImageBitmap(mQR);


        if (mIsInsert) {
            executor.execute(() -> {
                dataDao.insert(new Data(0, showData(mType), type, formateDate(), mQR));
            });
        }


        binding.btnSave.setOnClickListener(view -> {
            SaveImage.saveBitmapToGallery(getContext(), mQR);
        });

        binding.btnShare.setOnClickListener(view -> {
            Uri uri = SaveImage.saveBitmapToCache(getContext(), mQR);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Scan this QR code");
            startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
        });

        binding.btnBack.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        return inflated;
    }

    private String formateDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return now.format(myFormatObj);
    }

    private String showData(QRType type) {
        String[] parts = mData.split("\\|");
        switch (type) {
            case WEBSITE:
                if (!parts[0].startsWith("http://") && !parts[0].startsWith("https://"))
                    return "https://" + parts[0];
                return parts[0];
            case WiFi:
                return "Network Name: " + parts[0] + "\nPassword: " + parts[1];
            case EVENT:
                return "Title: " + parts[0] +
                        "\nDescription: " + parts[4] +
                        "\nLocation: " + parts[3] +
                        "\nDate: " + parts[1] + " - " + parts[2];
            case CONTACT:
                if (!parts[6].startsWith("http://") && !parts[6].startsWith("https://"))
                    parts[6] = "https://" + parts[6];
                return "Name: " + parts[0] + " " + parts[1] +
                        "\nPhone: " + parts[4] +
                        "\nEmail: " + parts[5] +
                        "\nWebsite: " + parts[6] +
                        "\nAddress: " + parts[7] + ", " + parts[8] + ", " + parts[9] +
                        "\nCompany: " + parts[2] +
                        "\nTitle: " + parts[3];
            case BUSINESS:
                if (!parts[4].startsWith("http://") && !parts[4].startsWith("https://"))
                    parts[4] = "https://" + parts[4];
                return "Company: " + parts[0] +
                        "\nIndustry: " + parts[1] +
                        "\nPhone: " + parts[2] +
                        "\nEmail: " + parts[3] +
                        "\nWebsite: " + parts[4] +
                        "\nAddress: " + parts[5] + ", " + parts[6] + ", " + parts[7];
            case X:
            case INSTAGRAM:
                if (parts[0].startsWith("@"))
                    parts[0] = parts[0].substring(1);
                return parts[0];
            case PHONE:
                if (!parts[0].startsWith("+")) parts[0] = "+2" + parts[0];
                return parts[0];
            default:
                return parts[0];
        }
    }

}