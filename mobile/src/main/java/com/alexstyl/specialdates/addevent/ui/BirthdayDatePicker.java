package com.alexstyl.specialdates.addevent.ui;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateConstants;
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.upcoming.MonthLabels;
import com.novoda.notils.caster.Views;

import java.util.Locale;

public class BirthdayDatePicker extends LinearLayout {

    private final MonthLabels labels;

    private final NumberPicker dayPicker;
    private final NumberPicker monthPicker;
    private final NumberPicker yearPicker;

    private final CheckedTextView includesYearCheckbox;

    private final Date today;

    public BirthdayDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
        labels = MonthLabels.forLocale(Locale.getDefault());
        today = Date.today();
        inflate(getContext(), R.layout.merge_birthday_picker, this);

        dayPicker = Views.findById(this, R.id.day_picker);
        setupDayPicker();

        monthPicker = Views.findById(this, R.id.month_picker);
        setupMonthPicker();

        yearPicker = Views.findById(this, R.id.year_picker);
        setupYearPicker();

        includesYearCheckbox = Views.findById(this, R.id.include_year_checkbox);
        includesYearCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = includesYearCheckbox.isChecked();
                includesYearCheckbox.setChecked(!checked);

                if (includesYearCheckbox.isChecked()) {
                    showYearPicker();
                } else {
                    hideYearPicker();
                }
            }

            private void hideYearPicker() {
                TransitionManager.beginDelayedTransition(BirthdayDatePicker.this);
                yearPicker.setVisibility(GONE);
            }

            private void showYearPicker() {
                TransitionManager.beginDelayedTransition(BirthdayDatePicker.this);
                yearPicker.setVisibility(VISIBLE);
            }
        });
    }

    private void setupDayPicker() {
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        dayPicker.setValue(today.getDayOfMonth());

    }

    private void setupMonthPicker() {
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        monthPicker.setDisplayedValues(labels.getMonthsOfYear());

        monthPicker.setValue(today.getMonth());
        monthPicker.setOnValueChangedListener(dateValidator);
    }

    private void setupYearPicker() {
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(todaysYear());

        yearPicker.setValue(todaysYear());
        yearPicker.setOnValueChangedListener(dateValidator);
    }

    private Integer todaysYear() {
        return today.getYear();
    }

    public void setDisplayingDate(Date dateToDisplay) {
        if (dateToDisplay.hasYear()) {
            dayPicker.setValue(dateToDisplay.getDayOfMonth());
            monthPicker.setValue(dateToDisplay.getMonth());
            yearPicker.setValue(dateToDisplay.getYear());
            yearPicker.setVisibility(VISIBLE);
            includesYearCheckbox.setChecked(true);
        } else {
            dayPicker.setValue(dateToDisplay.getDayOfMonth());
            monthPicker.setValue(dateToDisplay.getMonth());
            yearPicker.setValue(todaysYear());
            yearPicker.setVisibility(GONE);
            includesYearCheckbox.setChecked(false);
        }
    }

    public Date getDisplayingBirthday() {
        int dayOfMonth = getDayOfMonth();
        int month = getMonth();
        if (isDisplayingYear()) {
            int year = getYear();
            return Date.on(dayOfMonth, month, year);
        } else {
            return Date.on(dayOfMonth, month);
        }
    }

    private boolean isDisplayingYear() {
        return includesYearCheckbox.isChecked();
    }

    private int getDayOfMonth() {
        return dayPicker.getValue();
    }

    @MonthInt
    @SuppressWarnings("WrongConstant")
    private int getMonth() {
        return monthPicker.getValue();
    }

    private int getYear() {
        return yearPicker.getValue();
    }

    private final NumberPicker.OnValueChangeListener dateValidator = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (getMonth() == DateConstants.FEBRUARY && isDisplayingYear()) {

                if (isValidDate(29, DateConstants.FEBRUARY, getYear())) {
                    dayPicker.setMaxValue(29);
                } else {
                    dayPicker.setMaxValue(28);
                }
            } else {
                dayPicker.setMaxValue(31);
            }
        }

        private boolean isValidDate(int dayOfMonth, int month, int year) {
            try {
                Date.on(dayOfMonth, month, year);
                return true;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
    };

}
