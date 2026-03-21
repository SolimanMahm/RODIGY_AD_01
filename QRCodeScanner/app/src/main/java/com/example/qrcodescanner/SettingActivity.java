package com.example.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.qrcodescanner.databinding.ActivitySettingBinding;
import com.example.qrcodescanner.utils.constants;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    private String APP_PACKAGE_NAME;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        APP_PACKAGE_NAME = this.getPackageName();

        binding.vibrate.setOnClickListener(view -> binding.vibrateSwitch.toggle());

        binding.beep.setOnClickListener(view -> binding.beepSwitch.toggle());

        sharedPreferences = getSharedPreferences(constants.SETTING, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        binding.vibrateSwitch.setChecked(sharedPreferences.getBoolean(constants.VIBRATE, false));
        binding.beepSwitch.setChecked(sharedPreferences.getBoolean(constants.BEEP, false));

        binding.vibrateSwitch.setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    changeSwitchColor(binding.vibrateSwitch, b);
                    editor.putBoolean(constants.VIBRATE, b);
                    editor.apply();
                }
        );

        binding.beepSwitch.setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    changeSwitchColor(binding.beepSwitch, b);
                    editor.putBoolean(constants.BEEP, b);
                    editor.apply();
                }
        );

        binding.rateUs.setOnClickListener(view ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(constants.GOOGLE_PLAY_LINK + APP_PACKAGE_NAME)))

        );

        binding.share.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, constants.SHARE_MESSAGE + APP_PACKAGE_NAME);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
        });

        binding.privacyPolicy.setOnClickListener(view ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(constants.PRIVACY_URL)))
        );

        binding.btnBack.setOnClickListener(view -> finish());

    }

    private void changeSwitchColor(Switch aSwitch, boolean isChecked) {
        if (isChecked) {
            aSwitch.setThumbTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.beloved_sunflower)
            );
            aSwitch.setTrackTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.beloved_sunflower)
            );
        } else {
            aSwitch.setThumbTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.gray)
            );
            aSwitch.setTrackTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.white)
            );
        }

    }

}