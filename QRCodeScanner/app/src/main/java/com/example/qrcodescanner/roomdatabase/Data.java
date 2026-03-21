package com.example.qrcodescanner.roomdatabase;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;

@Entity
public class Data implements Serializable{
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "history_type")
    public int historyType;
    @ColumnInfo(name = "qr_content")
    public String QRContent;
    @ColumnInfo(name = "qr_type")
    public String QRType;
    @ColumnInfo(name = "qr_date_time")
    public String QRDateAndTime;
    @ColumnInfo(name = "qr_image")
    @Nullable
    public Bitmap QRImage;

    public Data(int historyType, String QRContent, String QRType, String QRDateAndTime, Bitmap QRImage) {
        this.historyType = historyType;
        this.QRContent = QRContent;
        this.QRType = QRType;
        this.QRDateAndTime = QRDateAndTime;
        this.QRImage = QRImage;
    }
}
