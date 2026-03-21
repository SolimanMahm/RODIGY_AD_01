package com.example.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qrcodescanner.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    private Context context;
    private FragmentHistoryBinding binding;
    private ViewPagerAdapter pagerAdapter;

    public HistoryFragment() {
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

        View inflated = inflater.inflate(R.layout.fragment_history, container, false);
        binding = FragmentHistoryBinding.bind(inflated);

        pagerAdapter = new ViewPagerAdapter(context, getChildFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.btnSettings.setOnClickListener(view -> startActivity(new Intent(context, SettingActivity.class)));

        return inflated;
    }
}