package com.alexstyl.specialdates.settings;

import android.content.Context;
import android.util.AttributeSet;

public class RingtonePreference extends android.preference.RingtonePreference {
    private OnPreferenceClickListener onPreferenceClickListener;

    public RingtonePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnPreferenceClickListener(OnPreferenceClickListener onPreferenceClickListener) {
        // don't call super. We'll handle the clicks ourselves
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    @Override
    protected void onClick() {
        if (!onPreferenceClickListener.onPreferenceClick(this)) {
            super.onClick();
        }
    }
}
