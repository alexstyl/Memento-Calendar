package com.alexstyl.specialdates.widgetprovider;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class UpcomingWidgetPreferences {

    private static final int DEFAULT_VARIANT_ID = WidgetVariant.LIGHT.getId();
    private static final float DEFAULT_OPACITY = 1.0f;

    private final EasyPreferences preferences;

    public UpcomingWidgetPreferences(Context context) {
        this.preferences = EasyPreferences.createForPrivatePreferences(context, R.string.pref_upcoming_widget_config);
    }

    public WidgetVariant getSelectedVariant() {
        int variantId = preferences.getInt(R.string.key_upcoming_widget_variant, DEFAULT_VARIANT_ID);
        return WidgetVariant.fromId(variantId);
    }

    public float getOppacityLevel() {
        return preferences.getFloat(R.string.key_upcoming_widget_opacity, DEFAULT_OPACITY);
    }

    public void storeUserOptions(UpcomingWidgetConfigurationPanel.UserOptions userOptions) {
        preferences.setFloat(R.string.key_upcoming_widget_opacity, userOptions.getOpacityLevel());
        preferences.setInteger(R.string.key_upcoming_widget_variant, userOptions.getWidgetVariant().getId());
    }
}
