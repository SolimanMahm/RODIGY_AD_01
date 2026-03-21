package com.example.qrcodescanner;

import com.example.qrcodescanner.roomdatabase.Data;

public interface OnItemClickListener {
    default void onClick(int position) {
    }

    default void dataClicked(boolean isInsert, Data dat) {
    }

    default void delete(int position) {
    }
}
