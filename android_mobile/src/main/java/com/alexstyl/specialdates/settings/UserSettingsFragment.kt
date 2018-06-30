package com.alexstyl.specialdates.settings

import android.app.Activity
import android.os.Bundle
import android.preference.Preference
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
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.novoda.notils.caster.Classes
import javax.inject.Inject

class UserSettingsFragment : MementoPreferenceFragment() {

    private var namedayLanguageListPreferences: NamedayListPreference? = null
    private var themingPreferences: ThemingPreferences? = null
    private var appThemePreference: Preference? = null
    private var activity: ThemedMementoActivity? = null

    lateinit var analytics: Analytics
        @Inject set
    lateinit var strings: Strings
        @Inject set
    lateinit var namedaysPreferences: NamedayUserSettings
        @Inject set
    lateinit var tracker: CrashAndErrorTracker
        @Inject set
    lateinit var donateMonitor: DonateMonitor
        @Inject set
    lateinit var eventPresenter: SettingsPresenter
        @Inject set
    lateinit var dailyReminderOreoChannelCreator: DailyReminderOreoChannelCreator
        @Inject set
    lateinit var navigator: HomeNavigator
        @Inject set

    private var donationService: DonationService? = null
    private val locale: NamedayLocale
        get() = namedaysPreferences.selectedLanguage

    private val themeSelectedListener = ThemeSelectDialog.OnThemeSelectedListener { theme ->
        analytics.trackThemeSelected(getString(theme.themeName))
        themingPreferences!!.selectedTheme = theme
        activity!!.applyNewTheme()
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.activity = Classes.from<ThemedMementoActivity>(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationModule = (getActivity()!!.application as MementoApplication).applicationModule
        applicationModule.inject(this)

        addPreferencesFromResource(R.xml.preference_main)
        themingPreferences = ThemingPreferences.newInstance(getActivity()!!)

        dailyReminderOreoChannelCreator.createDailyReminderChannel()

        appThemePreference = findPreference(R.string.key_app_theme_id)
        appThemePreference!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val dialog = ThemeSelectDialog()
            dialog.setOnThemeSelectedListener(themeSelectedListener)
            dialog.show(fragmentManager!!, FM_THEME_TAG)
            true
        }

        findPreference<Preference>(R.string.key_donate)?.setOnPreferenceClickListener {
            navigator.toDonate(activity as Activity)
            true
        }
        findPreference<Preference>(R.string.key_enable_bank_holidays)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
            eventPresenter.updateEventOptions()
            true
        }

        val bankholidaysLanguage = findPreference<Preference>(R.string.key_bankholidays_language)
        bankholidaysLanguage!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            OnlyGreekSupportedDialog().show(fragmentManager!!, "OnlyGreek")
            true
        }

        findPreference<Preference>(R.string.key_enable_namedays)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val enabled = newValue as Boolean
            tracker.onNamedayLocaleChanged(if (enabled) locale else null)
            eventPresenter.refreshPeopleEvents()
            true
        }

        namedayLanguageListPreferences = findPreference(R.string.key_namedays_language)
        namedayLanguageListPreferences!!.setOnNamedayLocaleChangeListener { locale ->
            namedaysPreferences.setSelectedLanguage(locale.countryCode)
            namedayLanguageListPreferences!!.summary = strings.localeName(locale)
            eventPresenter.refreshPeopleEvents()
            true
        }
        findPreference<Preference>(R.string.key_namedays_contacts_only)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            namedaysPreferences.isEnabledForContactsOnly = newValue as Boolean
            eventPresenter.updateEventOptions()
            true
        }
        findPreference<Preference>(R.string.key_namedays_full_name)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            eventPresenter.refreshPeopleEvents()
            true
        }

        val restorePreference = findPreference("key_donate_restore")
        donationService = AndroidDonationService(
                IabHelper(getActivity()!!, AndroidDonationConstants.PUBLIC_KEY),
                getActivity(),
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
        restorePreference!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            donationService!!.restoreDonations()
            true
        }
        eventPresenter.startMonitoring()
        reattachThemeDialogIfNeeded()
    }

    private fun reattachThemeDialogIfNeeded() {
        val themeSelectDialog = fragmentManager!!.findFragmentByTag(FM_THEME_TAG) as ThemeSelectDialog?
        themeSelectDialog?.setOnThemeSelectedListener(themeSelectedListener)
    }

    override fun onResume() {
        super.onResume()
        namedayLanguageListPreferences!!.summary = strings.localeName(namedaysPreferences.selectedLanguage)
        appThemePreference!!.setSummary(themingPreferences!!.selectedTheme.themeName)
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
