package com.example.qrcodescanner.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class DateTimePicker {
    private final Calendar calendar;
    private final Context context;
    private OnDateSelectedListener listener;

    public DateTimePicker(Context context, OnDateSelectedListener listener) {
        calendar = Calendar.getInstance();
        this.context = context;
        this.listener = listener;
    }

    public void datePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (datePicker, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    listener.onDateSelected(calendar);
                    timePicker();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void timePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (timePicker, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    listener.onDateSelected(calendar);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }
}
