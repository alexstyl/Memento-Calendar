package com.alexstyl.specialdates.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.donate.AndroidDonationConstants;
import com.alexstyl.specialdates.donate.AndroidDonationService;
import com.alexstyl.specialdates.donate.Donation;
import com.alexstyl.specialdates.donate.DonationCallbacks;
import com.alexstyl.specialdates.donate.DonationPreferences;
import com.alexstyl.specialdates.donate.DonationService;
import com.alexstyl.specialdates.donate.util.IabHelper;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.theming.ThemingPreferences;
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment;
import com.novoda.notils.caster.Classes;

import javax.inject.Inject;

public final class MainPreferenceFragment extends MementoPreferenceFragment {

    private static final String FM_THEME_TAG = "fm_theme";

    private NamedayListPreference namedayLanguageListPreferences;
    private ThemingPreferences themingPreferences;
    private Preference appThemePreference;
    private MainPreferenceActivity activity;
    private DonationService donationService;
    @Inject Analytics analytics;
    @Inject StringResources stringResources;
    @Inject NamedayUserSettings namedaysPreferences;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = Classes.from(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent applicationModule = ((MementoApplication) getActivity().getApplication()).getApplicationModule();
        applicationModule.inject(this);

        addPreferencesFromResource(R.xml.preference_main);
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
        findPreference(R.string.key_namedays_contacts_only).setOnPreferenceChangeListener(onPreferenceChangeListener);
        namedayLanguageListPreferences = findPreference(R.string.key_namedays_language);

        namedayLanguageListPreferences.setOnNamedayLocaleChangeListener(
                new NamedayListPreference.OnNamedayLocaleChangeListener() {

                    @Override
                    public boolean onNamedayChanged(NamedayLocale locale) {
                        namedaysPreferences.setSelectedLanguage(locale.getCountryCode());
                        namedayLanguageListPreferences.setSummary(locale.getLanguageNameResId());
                        return true;
                    }

                }
        );

        final Preference restore = findPreference("key_donate_restore");
        donationService = new AndroidDonationService(
                new IabHelper(getActivity(), AndroidDonationConstants.PUBLIC_KEY),
                getActivity(),
                DonationPreferences.newInstance(getActivity()),
                analytics
        );
        donationService.setup(new DonationCallbacks() {
            @Override
            public void onDonateException(String message) {
                getPreferenceScreen().removePreference(restore);
            }

            @Override
            public void onDonationFinished(Donation donation) {
                // do nothing
            }
        });
        restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                donationService.restoreDonations();
                return true;
            }
        });

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
        namedayLanguageListPreferences.setSummary(namedaysPreferences.getSelectedLanguage().getLanguageNameResId());
        appThemePreference.setSummary(themingPreferences.getSelectedTheme().getThemeName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        donationService.dispose();
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
