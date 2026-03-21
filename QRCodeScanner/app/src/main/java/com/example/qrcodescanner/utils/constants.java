package com.example.qrcodescanner.utils;

import android.icu.text.SimpleDateFormat;

import java.util.Locale;

public class constants {
    public static final String GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id=";
    public static final String SHARE_MESSAGE = "Check out this amazing app:\n" + GOOGLE_PLAY_LINK;
    public static final String PRIVACY_URL = "https://solimanmahmoud-dev.github.io/";
    public static final String SETTING = "SETTING";
    public static final String VIBRATE = "Vibrate";
    public static final String BEEP = "Beep";
    public static final String GENERATE_TYPE = "Type";
    public static final String TEXT = "Text";
    public static final String WEBSITE = "Website";
    public static final String WHATSAPP = "WhatsApp";
    public static final String X = "X";
    public static final String EMAIL = "Email";
    public static final String INSTAGRAM = "Instagram";
    public static final String PHONE = "Phone";
    public static final String WI_FI = "Wi-Fi";
    public static final String EVENT = "Event";
    public static final String LOCATION = "Location";
    public static final SimpleDateFormat sdf =
            new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat qrFormat =
            new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.US);
    public static final int QRCodeSize = 180;
}
