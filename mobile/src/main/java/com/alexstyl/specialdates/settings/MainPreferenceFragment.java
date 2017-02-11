package com.alexstyl.specialdates.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.theming.ThemingPreferences;
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment;
import com.novoda.notils.caster.Classes;

final public class MainPreferenceFragment extends MementoPreferenceFragment {

    private final String FM_THEME_TAG = "fm_theme";

    private NamedayListPreference namedayLanguageListPreferences;
    private NamedayPreferences namedaysPreferences;
    private ThemingPreferences themingPreferences;
    private Preference appThemePreference;
    private MainPreferenceActivity activity;
    private Analytics analytics;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = Classes.from(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_main);
        analytics = AnalyticsProvider.getAnalytics(getActivity());
        themingPreferences = ThemingPreferences.newInstance(getActivity());
        Preference bankholidaysLanguage = findPreference(R.string.key_bankholidays_language);
        bankholidaysLanguage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new OnlyGreekSupportedDialog().show(getFragmentManager(), "OnlyGreek");
                return true;
            }
        });

        appThemePreference = findPreference(R.string.key_app_theme);
        appThemePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ThemeSelectDialog dialog = new ThemeSelectDialog();
                dialog.setOnThemeSelectedListener(themeSelectedListener);
                dialog.show(getFragmentManager(), FM_THEME_TAG);
                return true;
            }
        });
        appThemePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                activity.reapplyTheme();
                return true;
            }
        });
        findPreference(R.string.key_enable_namedays).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean enabled = (boolean) newValue;
                ErrorTracker.onNamedayLocaleChanged(enabled ? getLocale() : null);
                return true;
            }
        });
        namedaysPreferences = NamedayPreferences.newInstance(getActivity());
        findPreference(R.string.key_namedays_contacts_only).setOnPreferenceChangeListener(onPreferenceChangeListener);
        namedayLanguageListPreferences = findPreference(R.string.key_namedays_language);

        namedayLanguageListPreferences.setOnNamedayLocaleChangeListener(
                new NamedayListPreference.OnNamedayLocaleChangeListener() {

                    @Override
                    public boolean onNamedayChanged(NamedayLocale locale) {
                        namedaysPreferences.setSelectedLanguage(locale.getCountryCode());
                        namedayLanguageListPreferences.setSummary(locale.getNameResId());
                        return true;
                    }

                }
        );
        reattachThemeDialogIfNeeded();
    }

    private void reattachThemeDialogIfNeeded() {
        ThemeSelectDialog themeSelectDialog = (ThemeSelectDialog) getFragmentManager().findFragmentByTag(FM_THEME_TAG);
        if (themeSelectDialog != null) {
            themeSelectDialog.setOnThemeSelectedListener(themeSelectedListener);
        }
    }

    private NamedayLocale getLocale() {
        return namedaysPreferences.getSelectedLanguage();
    }

    @Override
    public void onResume() {
        super.onResume();
        namedayLanguageListPreferences.setSummary(namedaysPreferences.getSelectedLanguage().getNameResId());
        appThemePreference.setSummary(themingPreferences.getSelectedTheme().getThemeName());

    }

    private final ThemeSelectDialog.OnThemeSelectedListener themeSelectedListener = new ThemeSelectDialog.OnThemeSelectedListener() {

        @Override
        public void onThemeSelected(MementoTheme theme) {
            analytics.trackAction(new ActionWithParameters(Action.SELECT_THEME, "theme name", theme.getThemeName()));
            themingPreferences.setSelectedTheme(theme);
            activity.reapplyTheme();
        }
    };

    private final Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            namedaysPreferences.setEnabledForContactsOnly((boolean) newValue);
            return true;
        }
    };
}
