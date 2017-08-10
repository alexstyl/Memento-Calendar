package com.alexstyl.specialdates.theming;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.android.Version;

public final class Themer {

    private final ThemingPreferences preferences;
    private final AttributeExtractor attributeExtractor;

    private static Themer instance;

    public static Themer get(Context context) {
        if (instance == null) {
            ThemingPreferences preferences = ThemingPreferences.newInstance(context);
            instance = new Themer(preferences, new AttributeExtractor());
        }
        return instance;
    }

    private Themer(ThemingPreferences preferences, AttributeExtractor attributeExtractor) {
        this.preferences = preferences;
        this.attributeExtractor = attributeExtractor;
    }

    public void initialiseActivity(MementoActivity activity) {
        MementoTheme theme = preferences.getSelectedTheme();
        activity.setTheme(theme.androidTheme());

        if (Version.hasLollipop()) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int primaryDark = attributeExtractor.extractPrimaryColorFrom(activity);
            window.setStatusBarColor(primaryDark);
        }

    }

    public MementoTheme getCurrentTheme() {
        return preferences.getSelectedTheme();
    }

}
