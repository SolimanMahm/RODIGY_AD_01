package com.example.qrcodescanner.generateQR;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;

import com.example.qrcodescanner.utils.constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.text.ParseException;
import java.util.Hashtable;

public class QRGenerator {
    public static Bitmap generate(
            QRType type,
            String data,
            int size
    ) throws ParseException {


        String qrContent = buildContent(type, data);

        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    qrContent,
                    BarcodeFormat.QR_CODE,
                    size,
                    size,
                    hints
            );

            Bitmap bitmap = Bitmap.createBitmap(
                    size,
                    size,
                    Bitmap.Config.ARGB_8888
            );

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(
                            x,
                            y,
                            bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE
                    );
                }
            }

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String buildContent(QRType type, String data) throws ParseException {
        String[] parts = data.split("\\|");
        switch (type) {
            case WEBSITE:
                if (!parts[0].startsWith("http://") && !parts[0].startsWith("https://"))
                    return "https://" + parts[0];
                return parts[0];
            case WiFi:
                return "WIFI:T:WPA;:S:" + parts[0] + ";P:" + parts[1] + ";;";
            case EVENT:
                parts[1] = constants.qrFormat.format(constants.sdf.parse(parts[1]));
                parts[2] = constants.qrFormat.format(constants.sdf.parse(parts[2]));
                return "BEGIN:VEVENT\n" +
                        "SUMMARY:" + parts[0] + "\n" +
                        "DTSTART:" + parts[1] + "\n" +
                        "DTEND:" + parts[2] + "\n" +
                        "LOCATION:" + parts[3] + "\n" +
                        "DESCRIPTION:" + parts[4] + "\n" +
                        "END:VEVENT";
            case CONTACT:
                if (!parts[6].startsWith("http://") && !parts[6].startsWith("https://"))
                    parts[6] = "https://" + parts[6];
                return "BEGIN:VCARD\n" +
                        "VERSION:3.0\n" +
                        "N:" + parts[1] + ";" + parts[0] + ";;;\n" +
                        "FN:" + parts[0] + " " + parts[1] + "\n" +
                        "ORG:" + parts[2] + "\n" +
                        "TITLE:" + parts[3] + "\n" +
                        "TEL;TYPE=CELL:" + parts[4] + "\n" +
                        "EMAIL:" + parts[5] + "\n" +
                        "URL:" + parts[6] + "\n" +
                        "ADR;TYPE=HOME:;;" + parts[7] + ";" + parts[8] + ";;" + parts[9] + "\n" +
                        "END:VCARD";
            case BUSINESS:
                if (!parts[4].startsWith("http://") && !parts[4].startsWith("https://"))
                    parts[4] = "https://" + parts[4];
                return "BEGIN:VCARD\n" +
                        "VERSION:3.0\n" +
                        "FN:" + parts[0] + "\n" +
                        "TEL;TYPE=WORK:" + parts[2] + "\n" +
                        "EMAIL;TYPE=WORK:" + parts[3] + "\n" +
                        "URL:" + parts[4] + "\n" +
                        "ADR;TYPE=WORK:;;" + parts[5] + ";" + parts[6] + ";;" + parts[7] + "\n" +
                        "NOTE:" + parts[1] + "\n" +
                        "END:VCARD";
            case LOCATION:
                return "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(parts[0]);
            case WHATSAPP:
                parts[0] = parts[0].replaceAll("[^0-9]", "");
                return "https://wa.me/" + parts[0];
            case EMAIL:
                return "mailto:" + parts[0];
            case X:
                if (parts[0].startsWith("@"))
                    parts[0] = parts[0].substring(1);
                return "https://x.com/" + parts[0];
            case INSTAGRAM:
                if (parts[0].startsWith("@"))
                    parts[0] = parts[0].substring(1);
                return "https://www.instagram.com/" + parts[0];
            case PHONE:
                parts[0] = parts[0].replaceAll("[^0-9+]", "");
                if (!parts[0].startsWith("+")) parts[0] = "+2" + parts[0];
                return "tel:" + parts[0];
            case TEXT:
            default:
                return parts[0];
        }
    }

}
