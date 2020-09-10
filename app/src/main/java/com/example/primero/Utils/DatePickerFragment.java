package com.example.primero.Utils;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.primero.UI.SlotActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment

{

    String turfId;
    public DatePickerFragment() {
    }

    public static DialogFragment newInstance(String TurfId) {
        Bundle args = new Bundle();
        args.putString("id", TurfId);
        DialogFragment f = new DatePickerFragment();
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        turfId =  getArguments().getString("id");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener pDataSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(getActivity(), SlotActivity.class);
                String Year = String.valueOf(year);
                String Month = String.valueOf(month+1);
                String Day = String.valueOf(dayOfMonth);
                intent.putExtra("Year",Year);
                intent.putExtra("Month",Month);
                intent.putExtra("Day",Day);
                intent.putExtra("TURF_ID",turfId);
                startActivity(intent);
            }
        };

        long now = System.currentTimeMillis() -1000;
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),pDataSetListner,year,month,day);
        dialog.getDatePicker().setMinDate(now);
        dialog.getDatePicker().setMaxDate(now + (1000*60*60*24*7));
        return dialog;


    }

}
