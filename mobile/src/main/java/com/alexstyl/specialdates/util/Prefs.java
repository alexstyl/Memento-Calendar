package com.alexstyl.specialdates.util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

/**
 * <p>Created by alexstyl on 11/09/15.</p>
 */
public class Prefs {

    private Context mContext;
    private static Prefs sInstance;

    private Prefs(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static Prefs from(Context context) {
        if (sInstance == null) {
            sInstance = new Prefs(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Returns the int preference associated to the given key, or the default given value if the key wasn't found
     */
    public int getInt(@StringRes int key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(mContext.getString(key), defaultValue);
    }

    public void setInt(@StringRes int key, int value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putInt(mContext.getString(key), value).apply();
    }
}
