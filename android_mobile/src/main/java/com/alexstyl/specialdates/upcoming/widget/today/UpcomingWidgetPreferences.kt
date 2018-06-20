package com.alexstyl.specialdates.upcoming.widget.today

import android.content.Context
import android.content.SharedPreferences

import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.R

class UpcomingWidgetPreferences(context: Context) {

    private val preferences: EasyPreferences =
            EasyPreferences.createForPrivatePreferences(context, R.string.pref_upcoming_widget_config)

    val selectedVariant: WidgetVariant
        get() {
            val variantId = preferences.getInt(R.string.key_upcoming_widget_variant, DEFAULT_VARIANT_ID)
            return WidgetVariant.fromId(variantId)
        }

    val oppacityLevel: Float
        get() = preferences.getFloat(R.string.key_upcoming_widget_opacity, DEFAULT_OPACITY)

    fun storeUserOptions(userOptions: UserOptions) {
        preferences.setFloat(R.string.key_upcoming_widget_opacity, userOptions.opacityLevel)
        preferences.setInteger(R.string.key_upcoming_widget_variant, userOptions.widgetVariant.id)
    }

    fun addListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.addOnPreferenceChangedListener(listener)
    }

    fun removeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.removeOnPreferenceChagnedListener(listener)
    }

    companion object {

        private val DEFAULT_VARIANT_ID = WidgetVariant.LIGHT.id
        private val DEFAULT_OPACITY = 1.0f
    }
}
