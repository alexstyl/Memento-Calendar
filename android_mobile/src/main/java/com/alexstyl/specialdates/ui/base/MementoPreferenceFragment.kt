package com.alexstyl.specialdates.ui.base

import android.content.Context
import android.preference.Preference
import android.preference.PreferenceCategory
import android.support.annotation.StringRes
import android.support.v4.preference.PreferenceFragment
import com.alexstyl.specialdates.util.GreekNameUtils

open class MementoPreferenceFragment : PreferenceFragment() {

    override fun addPreferencesFromResource(preferencesResId: Int) {
        super.addPreferencesFromResource(preferencesResId)
        if (GreekNameUtils.isGreekLocaleSelected(activity as Context)) {
            fixCategoryTitles()
        }
    }

    private fun fixCategoryTitles() {
        val count = preferenceScreen.preferenceCount
        for (i in 0 until count) {
            val preference = preferenceScreen.getPreference(i)
            val category = preference as? PreferenceCategory
            if (category != null && category.title != null) {

                val title = category.title.toString()
                val accentLessTitle = GreekNameUtils.removeAccents(title)
                category.title = accentLessTitle
            }
        }
    }

    fun <T : Preference> findPreference(@StringRes keyId: Int): T? {
        return findPreference(getString(keyId)) as T?
    }

    fun <T : Preference> findPreferenceOrThrow(@StringRes keyId: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findPreference(getString(keyId)) as T
    }

    fun onPreferenceClick(key: Int, function: () -> Unit) {
        findPreference<Preference>(key)!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            function()
            true
        }
    }
}
