package com.alexstyl.specialdates;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.annotation.StringRes;
import android.support.v4.util.Pair;

public final class EasyPreferences {

    private final SharedPreferences prefs;
    private final Resources res;

    public static EasyPreferences createForDefaultPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        return new EasyPreferences(preferences, resources);
    }

    public static EasyPreferences createForPrivatePreferences(Context context, @StringRes int fileName) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(fileName), Context.MODE_PRIVATE);
        Resources resources = context.getResources();
        return new EasyPreferences(preferences, resources);
    }

    private EasyPreferences(SharedPreferences preferences, Resources resources) {
        this.prefs = preferences;
        this.res = resources;
    }

    private String key(int key) {
        return res.getString(key);
    }

    public boolean getBoolean(@StringRes int bool, boolean defValue) {
        return prefs.getBoolean(key(bool), defValue);
    }

    public boolean getBoolean(@StringRes int bool, @BoolRes int fallbackDefaultValue) {
        boolean contains = prefs.contains(key(bool));
        if (contains) {
            return prefs.getBoolean(key(bool), false);
        }
        return res.getBoolean(fallbackDefaultValue);
    }

    public void setBoolean(@StringRes int key, boolean value) {
        prefs.edit().putBoolean(key(key), value).apply();
    }

    public void setString(@StringRes int key, String value) {
        prefs.edit().putString(key(key), value).apply();
    }

    public void setInteger(@StringRes int key, int value) {
        prefs.edit().putInt(key(key), value).apply();
    }

    public int getInt(@StringRes int key, int defValue) {
        return prefs.getInt(key(key), defValue);
    }

    public long getLong(@StringRes int key, long defValue) {
        return prefs.getLong(key(key), defValue);
    }

    public String getString(@StringRes int key, String defValue) {
        return prefs.getString(key(key), defValue);
    }

    public float getFloat(@StringRes int key, float defValue) {
        return prefs.getFloat(key(key), defValue);
    }

    public void setLong(@StringRes int key, long value) {
        prefs.edit().putLong(key(key), value).apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public void setFloat(@StringRes int key, float value) {
        prefs.edit().putFloat(key(key), value).apply();
    }

    public void setIntegers(Pair<Integer, Integer> firstPair, Pair<Integer, Integer>... otherPairs) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(key(firstPair.first), firstPair.second);
        for (Pair<Integer, Integer> pair : otherPairs) {
            edit.putInt(key(pair.first), pair.second);
        }
        edit.apply();
    }

    public void addOnPreferenceChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void removeOnPreferenceChagnedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
