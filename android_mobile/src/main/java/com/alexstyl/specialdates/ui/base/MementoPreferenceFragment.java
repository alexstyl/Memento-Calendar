package com.alexstyl.specialdates.ui.base;

import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.support.annotation.StringRes;
import android.support.v4.preference.PreferenceFragment;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.util.GreekNameUtils;

public class MementoPreferenceFragment extends PreferenceFragment {

    @Override
    public void addPreferencesFromResource(int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);
        if (GreekNameUtils.isGreekLocaleSelected(getActivity())) {
            fixCategoryTitles();
        }
    }

    private void fixCategoryTitles() {
        int count = getPreferenceScreen().getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            PreferenceCategory category = Version.as(PreferenceCategory.class, preference);
            if (category != null && category.getTitle() != null) {

                CharSequence title = category.getTitle().toString();
                String accentLessTitle = GreekNameUtils.removeAccents(title);
                category.setTitle(accentLessTitle);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends Preference> T findPreference(@StringRes int keyId) {
        return (T) findPreference(getString(keyId));
    }

}
