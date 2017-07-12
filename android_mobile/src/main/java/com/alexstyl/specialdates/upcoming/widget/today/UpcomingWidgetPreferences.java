package com.alexstyl.specialdates.upcoming.widget.today;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

class UpcomingWidgetPreferences {

    private static final int DEFAULT_VARIANT_ID = WidgetVariant.LIGHT.getId();
    private static final float DEFAULT_OPACITY = 1.0f;

    private final EasyPreferences preferences;

    UpcomingWidgetPreferences(Context context) {
        this.preferences = EasyPreferences.createForPrivatePreferences(context, R.string.pref_upcoming_widget_config);
    }

    WidgetVariant getSelectedVariant() {
        int variantId = preferences.getInt(R.string.key_upcoming_widget_variant, DEFAULT_VARIANT_ID);
        return WidgetVariant.fromId(variantId);
    }

    float getOppacityLevel() {
        return preferences.getFloat(R.string.key_upcoming_widget_opacity, DEFAULT_OPACITY);
    }

    void storeUserOptions(UpcomingWidgetConfigurationPanel.UserOptions userOptions) {
        preferences.setFloat(R.string.key_upcoming_widget_opacity, userOptions.getOpacityLevel());
        preferences.setInteger(R.string.key_upcoming_widget_variant, userOptions.getWidgetVariant().getId());
    }

    void addListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.addOnPreferenceChangedListener(listener);
    }

    public void removeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.removeOnPreferenceChagnedListener(listener);
    }
}
