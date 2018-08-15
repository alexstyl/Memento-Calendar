package com.alexstyl.specialdates.upcoming;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.novoda.notils.caster.Classes;

public final class DatePickerDialogFragment extends MementoDialog {

    private static final String ARG_DAY_OF_MONTH = "day_of_month";
    private static final String ARG_MONTH = "month";
    private static final String ARG_YEAR = "year";

    private OnDateSetListener listener;

    public static DatePickerDialogFragment newInstance(Date startingDate) {
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_OF_MONTH, startingDate.getDayOfMonth());
        args.putInt(ARG_MONTH, startingDate.getMonth());
        args.putInt(ARG_YEAR, startingDate.getYear());
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = Classes.from(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date startingDate = getStartingDate();
        return new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        listener.onDateSelected(dateOn(dayOfMonth, toOneIndexedMonth(month), year));
                    }
                },
                startingDate.getYear(),
                getZeroIndexedMonth(startingDate),
                startingDate.getDayOfMonth()
        );

    }

    @MonthInt
    @SuppressWarnings("WrongConstant")
    private int toOneIndexedMonth(@MonthInt int month) {
        return month + 1;
    }

    private int getZeroIndexedMonth(Date startingDate) {
        return startingDate.getMonth() - 1;
    }

    private Date getStartingDate() {
        int dayOfMonth = getArguments().getInt(ARG_DAY_OF_MONTH);
        @MonthInt int month = getArguments().getInt(ARG_MONTH);
        int year = getArguments().getInt(ARG_YEAR);
        return dateOn(dayOfMonth, month, year);
    }

    public interface OnDateSetListener {
        void onDateSelected(Date dateSelected);
    }
}
