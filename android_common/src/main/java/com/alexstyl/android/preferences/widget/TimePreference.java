
package com.alexstyl.android.preferences.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.alexstyl.specialdates.common.R;

public class TimePreference extends DialogPreference {
    private int lastHour = 0;
    private int lastMinute = 0;

    private TimePicker picker;

    public TimePreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        setDialogTitle(ctx.getString(R.string.set_time));
        setPositiveButtonText(ctx.getString(R.string.set));
        setNegativeButtonText(null);
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        picker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
        return picker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();

            int[] time = new int[2];
            time[0] = lastHour;
            time[1] = lastMinute;

            if (callChangeListener(time)) {
                persistString(time[0] + ":" + time[1]);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time;

        if (restoreValue) {
            if (defaultValue == null) {
                time = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(
                        getKey(), "00:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);
    }

    private static int getHour(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[0]);
    }

    private static int getMinute(String time) {
        String[] pieces = time.split(":");

        return (Integer.parseInt(pieces[1]));
    }
}
