package com.example.qrcodescanner.roomdatabase;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "data").build();
        }
        return instance;
    }

}
