package com.example.qrcodescanner.generate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcodescanner.R;
import com.example.qrcodescanner.databinding.ActivityThreeLevelBinding;
import com.example.qrcodescanner.generateQR.QRGenerator;
import com.example.qrcodescanner.generateQR.QRType;
import com.example.qrcodescanner.utils.constants;

import java.text.ParseException;

public class ThreeLevelActivity extends AppCompatActivity {

    ActivityThreeLevelBinding binding;
    private Bitmap QR;
    public static final String TYPE_QR = "type";
    public static final String RESULT_QR = "result";
    public static final String DATA_QR = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThreeLevelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ((ViewGroup.MarginLayoutParams) binding.scroll.getLayoutParams()).setMargins(0, 36 * 3, 0, 0);

        binding.btnBack.setOnClickListener(view -> finish());

        binding.btn.setOnClickListener(view -> {
            if (isInputValid()) {
                try {
                    QR = QRGenerator.generate(
                            QRType.BUSINESS,
                            getData(),
                            constants.QRCodeSize
                    );
                } catch (ParseException e) {
                    Toast.makeText(this, "Please Try Again!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.putExtra(TYPE_QR, QRType.BUSINESS);
                intent.putExtra(RESULT_QR, QR);
                intent.putExtra(DATA_QR, getData());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private boolean isInputValid() {
        if (binding.inputName.getText().toString().isEmpty()) {
            binding.inputName.setError(getText(R.string.error));
            return false;
        }
        if (binding.inputIndustry.getText().toString().isEmpty()) {
            binding.inputIndustry.setError(getText(R.string.error));
            return false;
        }
        if (binding.inputPhone.getText().toString().isEmpty()) {
            binding.inputPhone.setError(getText(R.string.error));
            return false;
        }
        if (binding.inputEmail.getText().toString().isEmpty()) {
            binding.inputEmail.setError(getText(R.string.error));
            return false;
        }
        if (binding.inputWebsite.getText().toString().isEmpty()) {
            binding.inputWebsite.setError(getText(R.string.error));
            return false;
        }
        if (binding.inputAddress.getText().toString().isEmpty()) {
            binding.inputAddress.setError(getText(R.string.error));
            return false;
        }
        if (binding.inputCity.getText().toString().isEmpty()) {
            binding.inputCity.setError(getText(R.string.error));
            return false;
        }
        if (binding.inputCountry.getText().toString().isEmpty()) {
            binding.inputCountry.setError(getText(R.string.error));
            return false;
        }
        return true;
    }

    private String getData() {
        return binding.inputName.getText().toString().trim() +
                "|" + binding.inputIndustry.getText().toString().trim() +
                "|" + binding.inputPhone.getText().toString().trim() +
                "|" + binding.inputEmail.getText().toString().trim() +
                "|" + binding.inputWebsite.getText().toString().trim() +
                "|" + binding.inputAddress.getText().toString().trim() +
                "|" + binding.inputCity.getText().toString().trim() +
                "|" + binding.inputCountry.getText().toString().trim();
    }

}