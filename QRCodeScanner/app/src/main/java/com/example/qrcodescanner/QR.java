package com.example.qrcodescanner;

public class QR {
    private int image;
    private String data;

    public QR(int image, String url, String data, String date) {
        this.image = image;
        this.data = data;
    }

    public int getImage() {
        return image;
    }

    public String getData() {
        return data;
    }
}
