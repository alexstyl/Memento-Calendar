package com.alexstyl.specialdates.settings;

import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.support.v4.preference.PreferenceFragment;

import com.alexstyl.specialdates.ui.base.MementoPreferenceActivity;
import com.alexstyl.specialdates.util.GreekNameUtils;
import com.alexstyl.specialdates.util.Utils;
import com.novoda.notils.caster.Classes;

public class MyPreferenceFragment extends PreferenceFragment {

    private MementoPreferenceActivity parentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = Classes.from(activity);
    }

    @Override
    public void addPreferencesFromResource(int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);
        if (GreekNameUtils.isGreekLocaleSelected(getActivity())) {
            fixCategoryTitles();
        }
    }

    final private void fixCategoryTitles() {
        int count = getPreferenceScreen().getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            PreferenceCategory category = Utils.as(PreferenceCategory.class, preference);
            if (category != null && category.getTitle() != null) {

                CharSequence title = category.getTitle().toString();
                if (title != null) {
                    String accentLessTitle = GreekNameUtils.removeAccents(title);
                    category.setTitle(accentLessTitle);
                }
            }
        }
    }

}
