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
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.upcoming.MonthLabels;
import com.novoda.notils.caster.Views;

import java.util.Locale;

public class EventDatePicker extends LinearLayout {

    private static final int FIRST_DAY_OF_MONTH = 1;

    private final MonthLabels labels;

    private final NumberPicker dayPicker;
    private final NumberPicker monthPicker;
    private final NumberPicker yearPicker;

    private final CheckedTextView includesYearCheckbox;

    private final Date today;
    private static final int FIRST_MONTH = 1;
    private static final int LAST_MONTH = 12;
    private static final int FIRST_YEAR = 1900;

    public EventDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
        labels = MonthLabels.forLocale(Locale.getDefault());
        today = Date.Companion.today();
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
                TransitionManager.beginDelayedTransition(EventDatePicker.this);
                yearPicker.setVisibility(GONE);
            }

            private void showYearPicker() {
                TransitionManager.beginDelayedTransition(EventDatePicker.this);
                yearPicker.setVisibility(VISIBLE);
            }
        });
    }

    private void setupDayPicker() {
        dayPicker.setMinValue(FIRST_DAY_OF_MONTH);
        dayPicker.setMaxValue(today.maxDaysInMonth());

        dayPicker.setValue(today.getDayOfMonth());

    }

    private void setupMonthPicker() {
        monthPicker.setMinValue(FIRST_MONTH);
        monthPicker.setMaxValue(LAST_MONTH);

        monthPicker.setDisplayedValues(labels.getMonthsOfYear());

        monthPicker.setValue(today.getMonth());
        monthPicker.setOnValueChangedListener(dateValidator);
    }

    private void setupYearPicker() {
        yearPicker.setMinValue(FIRST_YEAR);
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

    public Date getDisplayingDate() {
        int dayOfMonth = getDayOfMonth();
        int month = getMonth();
        if (isDisplayingYear()) {
            int year = getYear();
            return Date.Companion.on(dayOfMonth, month, year);
        } else {
            return Date.Companion.on(dayOfMonth, month);
        }
    }

    private boolean isDisplayingYear() {
        return includesYearCheckbox.isChecked();
    }

    private int getDayOfMonth() {
        return dayPicker.getValue();
    }

    @MonthInt
    private int getMonth() {
        @MonthInt int value = monthPicker.getValue();
        return value;
    }

    private int getYear() {
        return yearPicker.getValue();
    }

    private final NumberPicker.OnValueChangeListener dateValidator = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (isDisplayingYear()){
                dayPicker.setMaxValue(Date.Companion.on(FIRST_DAY_OF_MONTH, getMonth(), getYear()).maxDaysInMonth());
            } else {
                dayPicker.setMaxValue(Date.Companion.on(FIRST_DAY_OF_MONTH, getMonth()).maxDaysInMonth());
            }
        }

    };

}
