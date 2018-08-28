package com.alexstyl.specialdates.settings

import android.content.Context
import android.os.Bundle
import android.preference.Preference
import com.alexstyl.specialdates.BuildConfig
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.dailyreminder.DailyReminderOreoChannelCreator
import com.alexstyl.specialdates.donate.AndroidDonationConstants
import com.alexstyl.specialdates.donate.AndroidDonationService
import com.alexstyl.specialdates.donate.DonateMonitor
import com.alexstyl.specialdates.donate.Donation
import com.alexstyl.specialdates.donate.DonationCallbacks
import com.alexstyl.specialdates.donate.DonationPreferences
import com.alexstyl.specialdates.donate.DonationService
import com.alexstyl.specialdates.donate.util.IabHelper
import com.alexstyl.specialdates.events.SettingsPresenter
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.home.HomeNavigator
import com.alexstyl.specialdates.theming.ThemingPreferences
import com.alexstyl.specialdates.toast
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import javax.inject.Inject

class UserSettingsFragment : MementoPreferenceFragment() {

    private var namedayLanguageListPreferences: NamedayListPreference? = null
    private var restorePreference: Preference? = null
    private var appThemePreference: Preference? = null
    private var themedMementoActivity: ThemedMementoActivity? = null

    @Inject lateinit var themingPreferences: ThemingPreferences
    @Inject lateinit var analytics: Analytics
    @Inject lateinit var strings: Strings
    @Inject lateinit var namedaysPreferences: NamedayUserSettings
    @Inject lateinit var tracker: CrashAndErrorTracker
    @Inject lateinit var donateMonitor: DonateMonitor
    @Inject lateinit var eventPresenter: SettingsPresenter

    @Inject lateinit var navigator: HomeNavigator

    private var donationService: DonationService? = null
    private val locale: NamedayLocale
        get() = namedaysPreferences.selectedLanguage


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ThemedMementoActivity) {
            this.themedMementoActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationModule = (activity!!.application as MementoApplication).applicationModule
        applicationModule.inject(this)

        addPreferencesFromResource(R.xml.preference_main)

        appThemePreference = findPreference(R.string.key_app_theme_id)
        namedayLanguageListPreferences = findPreference(R.string.key_namedays_language)
        restorePreference = findPreference(R.string.key_donate_restore)

        setupAppTheme(appThemePreference!!)
        setupDonate(findPreference(R.string.key_donate))
        setupBankHolidaysToggle(findPreference(R.string.key_enable_bank_holidays)!!)
        setupBankHolidaysLanguage(findPreference(R.string.key_bankholidays_language)!!)
        setupNamedaysToggle(findPreference(R.string.key_enable_namedays)!!)
        setupNamedayLanguage(namedayLanguageListPreferences!!)
        setupNamedaysContactsOnly(findPreference(R.string.key_namedays_contacts_only)!!)
        setupNamedaysFullName(findPreference(R.string.key_namedays_full_name)!!)

        donationService = AndroidDonationService(
                IabHelper(activity!!, AndroidDonationConstants.PUBLIC_KEY),
                activity,
                DonationPreferences.newInstance(getActivity()),
                analytics,
                tracker,
                donateMonitor
        )
        donationService!!.setup(object : DonationCallbacks {
            override fun onDonateException(message: String) {
                preferenceScreen.removePreference(restorePreference)
            }

            override fun onDonationFinished(donation: Donation) {
                // do nothing
            }
        })
        setupRestoreDonation(restorePreference!!)
        eventPresenter.startMonitoring()
        reattachThemeDialogIfNeeded()
    }

    private fun setupRestoreDonation(preference: Preference) {
        preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            donationService!!.restoreDonations()
            true
        }
    }

    private fun setupNamedaysFullName(preference: Preference) {
        preference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            eventPresenter.refreshPeopleEvents()
            true
        }
    }

    private fun setupNamedaysContactsOnly(preference: Preference) {
        preference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            namedaysPreferences.isEnabledForContactsOnly = newValue as Boolean
            eventPresenter.updateEventOptions()
            true
        }
    }

    private fun setupNamedayLanguage(namedayListPreference: NamedayListPreference) {
        namedayListPreference.setOnNamedayLocaleChangeListener { locale ->
            namedaysPreferences.setSelectedLanguage(locale.countryCode)
            namedayListPreference.summary = strings.localeName(locale)
            eventPresenter.refreshPeopleEvents()
            true
        }
    }

    private fun setupAppTheme(preference: Preference) {
        preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val dialog = ThemeSelectDialog()
            dialog.setOnThemeSelectedListener { theme ->
                analytics.trackThemeSelected(getString(theme.themeName))
                themingPreferences.selectedTheme = theme
                if (themedMementoActivity != null) {
                    themedMementoActivity?.applyNewTheme()
                } else if (BuildConfig.DEBUG) {
                    toast("Fragment is not attached to a ThemedMementoActivity. Theme saved, but not applied")
                }
            }
            dialog.show(fragmentManager!!, FM_THEME_TAG)
            true
        }
    }

    private fun setupBankHolidaysToggle(preference: Preference) {
        preference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            eventPresenter.updateEventOptions()
            true
        }
    }

    private fun setupDonate(preference: Preference?) {
        preference?.setOnPreferenceClickListener {
            navigator.toDonate(getActivity()!!)
            true
        }
    }

    private fun setupNamedaysToggle(preference1: Preference) {
        preference1.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val enabled = newValue as Boolean
            tracker.onNamedayLocaleChanged(if (enabled) locale else null)
            eventPresenter.refreshPeopleEvents()
            true
        }
    }

    private fun setupBankHolidaysLanguage(bankholidaysLanguage: Preference) {
        bankholidaysLanguage.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            OnlyGreekSupportedDialog().show(fragmentManager!!, "OnlyGreek")
            true
        }
    }

    private fun reattachThemeDialogIfNeeded() {
        val themeSelectDialog = fragmentManager!!.findFragmentByTag(FM_THEME_TAG) as ThemeSelectDialog?
        themeSelectDialog?.setOnThemeSelectedListener(ThemeSelectDialog.OnThemeSelectedListener { theme ->
            analytics.trackThemeSelected(getString(theme.themeName))
            themingPreferences.selectedTheme = theme
            themedMementoActivity?.applyNewTheme()
        })
    }

    override fun onResume() {
        super.onResume()
        namedayLanguageListPreferences!!.summary = strings.localeName(namedaysPreferences.selectedLanguage)
        appThemePreference!!.setSummary(themingPreferences.selectedTheme.themeName)
    }

    override fun onDestroy() {
        super.onDestroy()
        eventPresenter.stopMonitoring()
        donationService!!.dispose()
    }

    companion object {
        private const val FM_THEME_TAG = "fm_theme"
    }

}
