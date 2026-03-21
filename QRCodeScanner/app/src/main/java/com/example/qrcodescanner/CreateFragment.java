package com.example.qrcodescanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qrcodescanner.databinding.FragmentCreateBinding;
import com.example.qrcodescanner.roomdatabase.AppDatabase;
import com.example.qrcodescanner.roomdatabase.Data;
import com.example.qrcodescanner.roomdatabase.DataDao;
import com.example.qrcodescanner.roomdatabase.DatabaseClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AppDatabase db;
    private DataDao dataDao;
    private Executor executor;

    private Context context;

    private OnItemClickListener listener;

    public CreateFragment() {
        // Required empty public constructor
    }

    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        FragmentCreateBinding binding = FragmentCreateBinding.bind(view);

        db = DatabaseClient.getInstance(getContext());
        dataDao = db.dataDao();
        executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            List<Data> dataList = dataDao.getByType(0);
            requireActivity().runOnUiThread(() -> {
                binding.recyclerViewCreate.setAdapter(new CustomAdapter(dataList, new OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        listener.dataClicked(false,dataList.get(position));
                    }

                    @Override
                    public void delete(int position) {
                        executor.execute(() -> {
                            dataDao.delete(dataList.get(position));
                            requireActivity().runOnUiThread(() -> {
                                dataList.remove(position);
                                binding.recyclerViewCreate.getAdapter().notifyItemRemoved(position);
                                binding.recyclerViewCreate.getAdapter().notifyItemRangeChanged(position, dataList.size());
                            });
                        });
                    }
                }));
            });
        });


        binding.recyclerViewCreate.setLayoutManager(new LinearLayoutManager(context));

        return binding.getRoot();
    }
}