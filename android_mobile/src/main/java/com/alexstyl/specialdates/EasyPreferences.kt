package com.alexstyl.specialdates

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import android.support.annotation.BoolRes
import android.support.annotation.StringRes

class EasyPreferences private constructor(private val prefs: SharedPreferences, private val res: Resources) {

    private fun key(key: Int): String {
        return res.getString(key)
    }

    fun getBoolean(@StringRes bool: Int, defValue: Boolean): Boolean {
        return prefs.getBoolean(key(bool), defValue)
    }

    fun getBoolean(@StringRes bool: Int, @BoolRes fallbackDefaultValue: Int): Boolean {
        val contains = prefs.contains(key(bool))
        return if (contains) {
            prefs.getBoolean(key(bool), false)
        } else res.getBoolean(fallbackDefaultValue)
    }

    fun setBoolean(@StringRes key: Int, value: Boolean) {
        prefs.edit().putBoolean(key(key), value).apply()
    }

    fun setString(@StringRes key: Int, value: String) {
        prefs.edit().putString(key(key), value).apply()
    }

    fun setInteger(@StringRes key: Int, value: Int) {
        prefs.edit().putInt(key(key), value).apply()
    }

    fun getInt(@StringRes key: Int, defValue: Int): Int {
        return prefs.getInt(key(key), defValue)
    }

    fun getLong(@StringRes key: Int, defValue: Long): Long {
        return prefs.getLong(key(key), defValue)
    }

    fun getString(@StringRes key: Int, defValue: String): String {
        return prefs.getString(key(key), defValue)
    }

    fun getFloat(@StringRes key: Int, defValue: Float): Float {
        return prefs.getFloat(key(key), defValue)
    }

    fun setLong(@StringRes key: Int, value: Long) {
        prefs.edit().putLong(key(key), value).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun setFloat(@StringRes key: Int, value: Float) {
        prefs.edit().putFloat(key(key), value).apply()
    }

    fun setIntegers(firstPair: Pair<Int, Int>, vararg otherPairs: Pair<Int, Int>) {
        val edit = prefs.edit()
        edit.putInt(key(firstPair.first), firstPair.second)
        for (pair in otherPairs) {
            edit.putInt(key(pair.first), pair.second)
        }
        edit.apply()
    }

    fun addOnPreferenceChangedListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun removeOnPreferenceChagnedListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {

        fun createForDefaultPreferences(context: Context): EasyPreferences {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val resources = context.resources
            return EasyPreferences(preferences, resources)
        }

        fun createForPrivatePreferences(context: Context, @StringRes fileName: Int): EasyPreferences {
            val preferences = context.getSharedPreferences(context.getString(fileName), Context.MODE_PRIVATE)
            val resources = context.resources
            return EasyPreferences(preferences, resources)
        }
    }
}
