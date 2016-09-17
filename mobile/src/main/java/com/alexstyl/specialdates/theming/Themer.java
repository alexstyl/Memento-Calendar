package com.alexstyl.specialdates.theming;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.util.Utils;

public class Themer {

    private final ThemingPreferences preferences;
    private final AttributeExtractor attributeExtractor;

    private static Themer INSTANCE;

    public static Themer get(Context context) {
        if (INSTANCE == null) {
            ThemingPreferences preferences = ThemingPreferences.newInstance(context);
            INSTANCE = new Themer(preferences, new AttributeExtractor());
        }
        return INSTANCE;
    }

    private Themer(ThemingPreferences preferences, AttributeExtractor attributeExtractor) {
        this.preferences = preferences;
        this.attributeExtractor = attributeExtractor;
    }

    public void initialiseActivity(MementoActivity activity) {
        MementoTheme theme = preferences.getSelectedTheme();
        activity.setTheme(theme.androidTheme());

        if (Utils.hasLollipop()) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int primaryDark = fetchPrimaryDarkColor(activity);
            window.setStatusBarColor(primaryDark);
        }

    }

    private int fetchPrimaryDarkColor(Context context) {
        return attributeExtractor.extractPrimaryColorFrom(context);
    }

    private boolean fetchDarkIcons(Context context) {
        return attributeExtractor.extractDarkIconsFrom(context);
    }

    public MementoTheme getCurrentTheme() {
        return preferences.getSelectedTheme();
    }

    public boolean isActivityUsingDarkIcons(Context activity) {
        return fetchDarkIcons(activity);
    }
}
