package com.example.qrcodescanner.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScannerViewModel extends ViewModel {
    private final MutableLiveData<String> qrResult = new MutableLiveData<>();
    private boolean scanned = false;

    public LiveData<String> getQrResult() {
        return qrResult;
    }

    public void onQrScanned(String result) {
        if (scanned) return;
        scanned = true;
        qrResult.postValue(result);
    }

    public void reset() {
        scanned = false;
    }

}
