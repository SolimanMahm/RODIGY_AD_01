package com.example.qrcodescanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qrcodescanner.databinding.FragmentScanBinding;
import com.example.qrcodescanner.roomdatabase.AppDatabase;
import com.example.qrcodescanner.roomdatabase.Data;
import com.example.qrcodescanner.roomdatabase.DataDao;
import com.example.qrcodescanner.roomdatabase.DatabaseClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScanFragment extends Fragment implements OnItemClickListener {

    private Context context;
    private OnItemClickListener listener;
    private AppDatabase db;
    private DataDao dataDao;
    private Executor executor;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnItemClickListener)
            listener = (OnItemClickListener) context;
        else throw new RuntimeException("Activity not implement OnItemClickListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        FragmentScanBinding binding = FragmentScanBinding.bind(view);

        db = DatabaseClient.getInstance(getContext());
        dataDao = db.dataDao();
        executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            List<Data> dataList = dataDao.getByType(1);
            requireActivity().runOnUiThread(() -> {
                binding.recyclerViewScan.setAdapter(new CustomAdapter(dataList, new OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        listener.dataClicked(false, dataList.get(position));
                    }

                    @Override
                    public void delete(int position) {
                        executor.execute(() -> {
                            dataDao.delete(dataList.get(position));
                            requireActivity().runOnUiThread(() -> {
                                dataList.remove(position);
                                binding.recyclerViewScan.getAdapter().notifyItemRemoved(position);
                                binding.recyclerViewScan.getAdapter().notifyItemRangeChanged(position, dataList.size());
                            });
                        });
                    }
                }));
            });
        });


//        ArrayList<QR> data = new ArrayList<>();
//        data.add(new QR(0, "https://itunes.com", "Data", "16 Dec 2022, 9:30 pm"));
//        data.add(new QR(0, "https://itunes.com", "Data", "16 Dec 2022, 9:30 pm"));
//        data.add(new QR(0, "https://itunes.com", "Data", "16 Dec 2022, 9:30 pm"));
//        data.add(new QR(0, "https://itunes.com", "Data", "16 Dec 2022, 9:30 pm"));


        binding.recyclerViewScan.setLayoutManager(new LinearLayoutManager(context));


        return binding.getRoot();
    }

    @Override
    public void onClick(int position) {
        listener.onClick(position);
    }
}