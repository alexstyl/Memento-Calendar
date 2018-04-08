package com.alexstyl.specialdates.settings;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.dailyreminder.DailyReminderOreoChannelCreator;
import com.alexstyl.specialdates.dailyreminder.NotificationConstants;
import com.alexstyl.specialdates.donate.AndroidDonationConstants;
import com.alexstyl.specialdates.donate.AndroidDonationService;
import com.alexstyl.specialdates.donate.DonateMonitor;
import com.alexstyl.specialdates.donate.Donation;
import com.alexstyl.specialdates.donate.DonationCallbacks;
import com.alexstyl.specialdates.donate.DonationPreferences;
import com.alexstyl.specialdates.donate.DonationService;
import com.alexstyl.specialdates.donate.util.IabHelper;
import com.alexstyl.specialdates.events.SettingsPresenter;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.theming.ThemingPreferences;
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.novoda.notils.caster.Classes;

import javax.inject.Inject;

public final class MainPreferenceFragment extends MementoPreferenceFragment {

    private static final String FM_THEME_TAG = "fm_theme";

    private NamedayListPreference namedayLanguageListPreferences;
    private ThemingPreferences themingPreferences;
    private Preference appThemePreference;
    private ThemedMementoActivity activity;
    private DonationService donationService;
    @Inject Analytics analytics;
    @Inject Strings strings;
    @Inject NamedayUserSettings namedaysPreferences;
    @Inject CrashAndErrorTracker tracker;
    @Inject DonateMonitor donateMonitor;
    @Inject SettingsPresenter eventPresenter;
    @Inject NotificationManager notificatioManager;
    @Inject DailyReminderOreoChannelCreator dailyReminderOreoChannelCreator;

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
        themingPreferences = ThemingPreferences.Companion.newInstance(getActivity());

        dailyReminderOreoChannelCreator.createDailyReminderChannel();

        appThemePreference = findPreference(R.string.key_app_theme_id);
        appThemePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ThemeSelectDialog dialog = new ThemeSelectDialog();
                dialog.setOnThemeSelectedListener(themeSelectedListener);
                dialog.show(getFragmentManager(), FM_THEME_TAG);
                return true;
            }
        });

        findPreference(R.string.key_enable_bank_holidays).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                eventPresenter.updateEventOptions();
                return true;
            }
        });

        Preference bankholidaysLanguage = findPreference(R.string.key_bankholidays_language);
        bankholidaysLanguage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new OnlyGreekSupportedDialog().show(getFragmentManager(), "OnlyGreek");
                return true;
            }
        });

        findPreference(R.string.key_enable_namedays).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean enabled = (boolean) newValue;
                tracker.onNamedayLocaleChanged(enabled ? getLocale() : null);
                eventPresenter.refreshPeopleEvents();
                return true;
            }
        });

        namedayLanguageListPreferences = findPreference(R.string.key_namedays_language);
        namedayLanguageListPreferences.setOnNamedayLocaleChangeListener(
                new NamedayListPreference.OnNamedayLocaleChangeListener() {

                    @Override
                    public boolean onNamedayChanged(NamedayLocale locale) {
                        namedaysPreferences.setSelectedLanguage(locale.getCountryCode());
                        namedayLanguageListPreferences.setSummary(strings.localeName(locale));
                        eventPresenter.refreshPeopleEvents();
                        return true;
                    }
                }
        );
        findPreference(R.string.key_namedays_contacts_only).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                namedaysPreferences.setEnabledForContactsOnly((boolean) newValue);
                eventPresenter.updateEventOptions();
                return true;
            }
        });
        findPreference(R.string.key_namedays_full_name).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                eventPresenter.refreshPeopleEvents();
                return true;
            }
        });

        final Preference restorePreference = findPreference("key_donate_restore");
        donationService = new AndroidDonationService(
                new IabHelper(getActivity(), AndroidDonationConstants.PUBLIC_KEY),
                getActivity(),
                DonationPreferences.newInstance(getActivity()),
                analytics,
                tracker,
                donateMonitor
        );
        donationService.setup(new DonationCallbacks() {
            @Override
            public void onDonateException(String message) {
                getPreferenceScreen().removePreference(restorePreference);
            }

            @Override
            public void onDonationFinished(Donation donation) {
                // do nothing
            }
        });
        restorePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                donationService.restoreDonations();
                return true;
            }
        });

        findPreference(R.string.key_daily_reminder_settings).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (Version.hasOreo()) {
                    openDailyReminderChannelSettings();
                } else {
                    Intent intent = new Intent(getContext(), DailyReminderActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        eventPresenter.startMonitoring();
        reattachThemeDialogIfNeeded();
    }

    private void openDailyReminderChannelSettings() {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotificationConstants.CHANNEL_ID_CONTACTS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
        startActivity(intent);
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
        namedayLanguageListPreferences.setSummary(strings.localeName(namedaysPreferences.getSelectedLanguage()));
        appThemePreference.setSummary(themingPreferences.getSelectedTheme().getThemeName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventPresenter.stopMonitoring();
        donationService.dispose();
    }

    private final ThemeSelectDialog.OnThemeSelectedListener themeSelectedListener = new ThemeSelectDialog.OnThemeSelectedListener() {
        @Override
        public void onThemeSelected(MementoTheme theme) {
            analytics.trackAction(new ActionWithParameters(Action.SELECT_THEME, "theme name", getString(theme.getThemeName())));
            themingPreferences.setSelectedTheme(theme);
            activity.applyNewTheme();
        }
    };

}
