package com.example.qrcodescanner.generate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.qrcodescanner.R;
import com.example.qrcodescanner.databinding.ActivityLevelOneBinding;
import com.example.qrcodescanner.generateQR.QRGenerator;
import com.example.qrcodescanner.generateQR.QRType;
import com.example.qrcodescanner.utils.DateTimePicker;
import com.example.qrcodescanner.utils.OnDateSelectedListener;
import com.example.qrcodescanner.utils.constants;

import java.text.ParseException;
import java.util.Calendar;

public class OneLevelActivity extends AppCompatActivity {

    private ActivityLevelOneBinding binding;
    private String type;

    private Bitmap QR;
    public static final String RESULT_QR = "result";
    public static final String TYPE_QR = "type";

    public static final String DATA_QR = "data";

    private QRType qrType = QRType.TEXT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelOneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hideAllInput(true);

        type = getIntent().getStringExtra(constants.GENERATE_TYPE);

        switch (type) {
            case constants.WEBSITE:
                website();
                break;
            case constants.WHATSAPP:
                whatsApp();
                break;
            case constants.X:
                x();
                break;
            case constants.EMAIL:
                email();
                break;
            case constants.INSTAGRAM:
                instagram();
                break;
            case constants.PHONE:
                phone();
                break;
            case constants.WI_FI:
                wi_fi();
                break;
            case constants.EVENT:
                event();
                break;
            case constants.LOCATION:
                location();
                break;
        }

        binding.btn.setOnClickListener(view -> {
            if (isInputValid()) {
                try {
                    QR = QRGenerator.generate(
                            qrType,
                            getData(),
                            constants.QRCodeSize
                    );
                } catch (ParseException e) {
                    Toast.makeText(this, "Please Try Again!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.putExtra(TYPE_QR, qrType);
                intent.putExtra(RESULT_QR, QR);
                intent.putExtra(DATA_QR, getData());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.btnBack.setOnClickListener(view -> finish());

        binding.input2.setOnClickListener(view -> {
            if (qrType == QRType.EVENT) {
                DateTimePicker picker = new DateTimePicker(this, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Calendar calendar) {
                        binding.input2.setText(constants.sdf.format(calendar.getTime()));
                    }
                });
                picker.datePicker();
            }
        });

        binding.input3.setOnClickListener(view -> {
            if (qrType == QRType.EVENT) {
                DateTimePicker picker = new DateTimePicker(this, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Calendar calendar) {
                        binding.input3.setText(constants.sdf.format(calendar.getTime()));
                    }
                });
                picker.datePicker();
            }
        });

    }

    private String getData() {
        return binding.input.getText().toString().trim() +
                "|" + binding.input2.getText().toString().trim() +
                "|" + binding.input3.getText().toString().trim() +
                "|" + binding.input4.getText().toString().trim() +
                "|" + binding.input5.getText().toString().trim();
    }

    private boolean isInputValid() {
        if (binding.input.getVisibility() == View.VISIBLE && binding.input.getText().toString().isEmpty()) {
            binding.input.setError(getText(R.string.error));
            return false;
        } else if (qrType == QRType.EMAIL && !binding.input.getText().toString().contains("@")) {
            binding.input.setError(getText(R.string.error));
            return false;
        }
        if (binding.input2.getVisibility() == View.VISIBLE && binding.input2.getText().toString().isEmpty()) {
            binding.input2.setError(getText(R.string.error));
            return false;
        }
        if (binding.input3.getVisibility() == View.VISIBLE && binding.input3.getText().toString().isEmpty()) {
            binding.input3.setError(getText(R.string.error));
            return false;
        }
        if (binding.input4.getVisibility() == View.VISIBLE && binding.input4.getText().toString().isEmpty()) {
            binding.input4.setError(getText(R.string.error));
            return false;
        }
        if (binding.input5.getVisibility() == View.VISIBLE && binding.input5.getText().toString().isEmpty()) {
            binding.input5.setError(getText(R.string.error));
            return false;
        }
        return true;
    }

    private void hideAllInput(boolean isHide) {
        if (isHide) {
            /// for Text & Website & WhatsApp & Twitter & Email & Instagram & Phone
            showInputTwo(!isHide);
            binding.text3.setVisibility(View.GONE);
            binding.input3.setVisibility(View.GONE);
            binding.text4.setVisibility(View.GONE);
            binding.input4.setVisibility(View.GONE);
            binding.text5.setVisibility(View.GONE);
            binding.input5.setVisibility(View.GONE);
        } else {
            ///  for Event
            showInputTwo(!isHide);
            binding.text3.setVisibility(View.VISIBLE);
            binding.input3.setVisibility(View.VISIBLE);
            binding.text4.setVisibility(View.VISIBLE);
            binding.input4.setVisibility(View.VISIBLE);
            binding.text5.setVisibility(View.VISIBLE);
            binding.input5.setVisibility(View.VISIBLE);
        }
    }

    private void showInputTwo(boolean isShow) {
        if (isShow) {
            binding.text2.setVisibility(View.VISIBLE);
            binding.input2.setVisibility(View.VISIBLE);
        } else {
            binding.text2.setVisibility(View.GONE);
            binding.input2.setVisibility(View.GONE);
        }
    }

    private void website() {
        qrType = QRType.WEBSITE;
        hideAllInput(true);
        binding.title.setText(R.string.website);
        binding.image.setImageResource(R.drawable.ic_website);
        binding.text.setText("Website URL");
        binding.input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        binding.input.setHint("www.qrcode.com");
    }

    private void whatsApp() {
        qrType = QRType.WHATSAPP;
        hideAllInput(true);
        binding.title.setText(R.string.whatsapp);
        binding.image.setImageResource(R.drawable.ic_whatsapp);
        binding.text.setText("WhatsApp Number");
        binding.input.setInputType(InputType.TYPE_CLASS_PHONE);
        binding.input.setHint("+20 123 4567 890");
    }

    private void x() {
        qrType = QRType.X;
        hideAllInput(true);
        binding.title.setText(R.string.x);
        binding.image.setImageResource(R.drawable.ic_x);
        binding.text.setText("Username");
        binding.input.setInputType(InputType.TYPE_CLASS_TEXT);
        binding.input.setHint("Enter x username");
    }

    private void email() {
        qrType = QRType.EMAIL;
        hideAllInput(true);
        binding.title.setText(R.string.email);
        binding.image.setImageResource(R.drawable.ic_email);
        binding.text.setText(R.string.email);
        binding.input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        binding.input.setHint("Enter email address");
    }

    private void instagram() {
        qrType = QRType.INSTAGRAM;
        hideAllInput(true);
        binding.title.setText(R.string.instagram);
        binding.image.setImageResource(R.drawable.ic_instagram);
        binding.text.setText("Username");
        binding.input.setInputType(InputType.TYPE_CLASS_TEXT);
        binding.input.setHint("Enter Instagram username");
    }

    private void phone() {
        qrType = QRType.PHONE;
        hideAllInput(true);
        binding.title.setText(R.string.telephone);
        binding.image.setImageResource(R.drawable.ic_telephone);
        binding.text.setText("Phone Number");
        binding.input.setInputType(InputType.TYPE_CLASS_PHONE);
        binding.input.setHint("+92xxxxxxxxxx");
    }

    private void wi_fi() {
        qrType = QRType.WiFi;
        hideAllInput(true);
        showInputTwo(true);
        binding.title.setText(R.string.wi_fi);
        binding.image.setImageResource(R.drawable.ic_wi_fi);

        // input one
        binding.text.setText("Network");
        binding.input.setInputType(InputType.TYPE_CLASS_TEXT);
        binding.input.setHint("Enter network name");

        // input two
        binding.text2.setText("Password");
        binding.input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding.input2.setTypeface(
                ResourcesCompat.getFont(this, R.font.itim_regular)
        );
        binding.input2.setHint("Enter password");
    }

    private void event() {
        qrType = QRType.EVENT;
        hideAllInput(false);
        binding.title.setText(R.string.event);
        binding.image.setImageResource(R.drawable.ic_event);
        ((ViewGroup.MarginLayoutParams) binding.scroll.getLayoutParams()).setMargins(0, 36 * 3, 0, 0);

        // input one
        binding.text.setText("Event Name");
        binding.input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        binding.input.setHint("Enter name");

        // input two
        binding.text2.setText("Start Date and Time");
        binding.input2.setInputType(
                InputType.TYPE_NULL
        );
        binding.input2.setFocusable(false);
        binding.input2.setClickable(true);
        binding.input2.setHint("26 Jan 2026, 10:40 pm");

        // input three
        binding.text3.setText("End Date and Time");
        binding.input3.setInputType(
                InputType.TYPE_NULL
        );
        binding.input3.setFocusable(false);
        binding.input3.setClickable(true);
        binding.input3.setHint("26 Jan 2026, 10:40 pm");

        // input four
        binding.text4.setText("Event Location");
        binding.input4.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        binding.input4.setHint("Enter location");

        // input four
        binding.text5.setText("Description");
        binding.input4.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        binding.input5.setHint("Enter any details");
    }

    private void location() {
        qrType = QRType.LOCATION;
        hideAllInput(true);
        binding.title.setText(R.string.location);
        binding.image.setImageResource(R.drawable.ic_location);
        binding.text.setText(R.string.location);
        binding.input.setHint("e.g Nasr City, Cairo, Egypt");
    }
}