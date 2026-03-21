package com.example.qrcodescanner;


import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.qrcodescanner.databinding.ActivityMainBinding;
import com.example.qrcodescanner.roomdatabase.Data;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private ActivityMainBinding binding;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                101
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
        binding.bottomNavigation.setSelectedItemId(R.id.home);
        binding.bottomNavigation.setItemBackgroundResource(0);

        View decorView = getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {
            return insets.replaceSystemWindowInsets(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    0); // remove space from bottom navigation bar
        });

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.generate_qr) fragment = new GenerateQR();
                else if (item.getItemId() == R.id.history) fragment = new HistoryFragment();

                binding.bottomNavigation.setItemBackgroundResource(R.drawable.bottom_nav_item);

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });

        binding.btnHome.setOnClickListener(view -> {
            fragment = new HomeFragment();
            binding.bottomNavigation.setSelectedItemId(R.id.home);
            binding.bottomNavigation.setItemBackgroundResource(0);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (!(fragment instanceof HomeFragment)) {
                boolean isRoot = getSupportFragmentManager().getBackStackEntryCount() == 0;
                binding.bottomNavigation.setItemBackgroundResource(
                        isRoot ? R.drawable.bottom_nav_item : 0
                );
            }
        });

    }

    @Override
    public void dataClicked(boolean isInsert, Data data) {
        if (isInsert) {
            binding.bottomNavigation.setSelectedItemId(R.id.history);
            binding.bottomNavigation.setItemBackgroundResource(0);
        }
        ResultFragment resultFragment = ResultFragment.newInstance(isInsert, data);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, resultFragment)
                .addToBackStack(null)
                .commit();
    }
}