package com.kasa77.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.kasa77.R;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment {

    private DatePicker datePicker;

    public interface DateDialogListener {
        void onFinishDialog(Date date);
    }
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        @SuppressLint("InflateParams") View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date,null);
        datePicker = v.findViewById(R.id.dialog_date_date_picker);
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(v)
                .setTitle("Select Date")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            int year = datePicker.getYear();
                            int mon = datePicker.getMonth();
                            int day = datePicker.getDayOfMonth();
                            Date date = new GregorianCalendar(year,mon,day).getTime();
                            DateDialogListener activity = (DateDialogListener) getActivity();
                            assert activity != null;
                            activity.onFinishDialog(date);
                            dismiss();
                        })
                .create();
    }
}