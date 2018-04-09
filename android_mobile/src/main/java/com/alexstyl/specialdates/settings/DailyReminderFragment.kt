package com.alexstyl.specialdates.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.RingtonePreference
import android.text.format.DateFormat
import com.alexstyl.android.preferences.widget.TimePreference
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.TimeOfDay
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler
import com.alexstyl.specialdates.dailyreminder.DailyReminderUserSettings
import com.alexstyl.specialdates.permissions.AndroidPermissions
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import java.net.URI
import java.util.Calendar
import javax.inject.Inject

class DailyReminderFragment : MementoPreferenceFragment() {

    private var enablePreference: CheckBoxPreference? = null
    private var ringtonePreference: ClickableRingtonePreference? = null
    private var timePreference: TimePreference? = null

    private var permissionChecker: AndroidPermissions? = null
        @Inject set
    private var scheduler: DailyReminderScheduler? = null
        @Inject set
    var analytics: Analytics? = null
        @Inject set
    var tracker: CrashAndErrorTracker? = null
        @Inject set
    var preferences: DailyReminderUserSettings? = null
        @Inject set
    var navigator: DailyReminderNavigator? = null
        @Inject set


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationModule = (activity!!.application as MementoApplication).applicationModule
        applicationModule.inject(this)

        addPreferencesFromResource(R.xml.preference_dailyreminder)

        permissionChecker = AndroidPermissions(tracker!!, activity!!)
        enablePreference = findPreference(R.string.key_daily_reminder)

        enablePreference!!.onPreferenceChangeListener = OnPreferenceChangeListener { preference, newValue ->
            val isEnabled = newValue as Boolean
            preferences!!.setEnabled(isEnabled)

            if (isEnabled) {
                analytics!!.trackDailyReminderEnabled()
                scheduler!!.setupReminder(preferences)
            } else {
                analytics!!.trackDailyReminderDisabled()
                scheduler!!.cancelReminder()
            }
            true
        }

        timePreference = findPreference(R.string.key_daily_reminder_time)
        timePreference!!.onPreferenceChangeListener = OnPreferenceChangeListener { preference, newValue ->
            val time = newValue as IntArray
            val timeOfDay = TimeOfDay(time[0], time[1])
            updateTimeSet(timeOfDay)
            analytics!!.trackDailyReminderTimeUpdated(timeOfDay)
            preferences!!.setDailyReminderTime(timeOfDay)
            scheduler!!.updateReminderTime(timeOfDay)
            true
        }

        ringtonePreference = findPreference(R.string.key_daily_reminder_ringtone)
        ringtonePreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (permissionChecker!!.canReadExternalStorage()) {
                // the permission exists. Let the system handle the event
                false
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), EXTERNAL_STORAGE_REQUEST_CODE)
                true
            }
        }
        ringtonePreference?.onPreferenceChangeListener = OnPreferenceChangeListener { preference, newValue ->
            val ringtoneUri = URI.create(newValue as String)
            ringtonePreference!!.updateRingtoneSummaryWith(ringtoneUri)
            true
        }



        findPreference<Preference>(R.string.key_daily_reminder_advanced_settings)?.setOnPreferenceClickListener { _ ->
            navigator!!.openAdvancedSettings(activity as Activity)
            false
        }


        hideVibratorSettingIfNotPresent()
    }

    private fun hideVibratorSettingIfNotPresent() {
        if (hasNoVibratorHardware()) {
            val vibratePreference = findPreference(getString(R.string.key_daily_reminder_vibrate_enabled))
            preferenceScreen.removePreference(vibratePreference)
        }
    }

    private fun hasNoVibratorHardware(): Boolean {
        val vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return !vibrator.hasVibrator()
    }

    override fun onResume() {
        super.onResume()
        enablePreference!!.isChecked = preferences!!.isEnabled()
        ringtonePreference?.updateRingtoneSummaryWith(preferences!!.getRingtone())

        val timeOfDay = preferences!!.getTimeSet()
        updateTimeSet(timeOfDay)
    }

    private fun updateTimeSet(time: TimeOfDay) {
        val timeString = getStringHour(time)
        val summary = String.format(getString(R.string.daily_reminder_time_summary), timeString)
        timePreference!!.summary = summary

    }

    private fun getStringHour(time: TimeOfDay): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, time.hours)
        cal.set(Calendar.MINUTE, time.minutes)
        return getHour(activity, cal).toString()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL_STORAGE_REQUEST_CODE && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ringtonePreference!!.onClick()
        }
    }

    private fun RingtonePreference.updateRingtoneSummaryWith(ringtoneUri: URI) {
        var name: String? = null
        if (ringtoneUri.toString().isNotEmpty()) {
            val ringtone = RingtoneManager.getRingtone(activity, Uri.parse(ringtoneUri.toString()))
            if (ringtone != null) {
                name = ringtone.getTitle(activity)
            }
        } else {
            name = getString(R.string.no_sound)
        }
        ringtonePreference!!.summary = name
    }

    companion object {

        private val EXTERNAL_STORAGE_REQUEST_CODE = 15

        // Char sequence for a 12 hour format.
        private val DEFAULT_FORMAT_12_HOUR = "hh:mm a"
        // Char sequence for a 24 hour format.
        private val DEFAULT_FORMAT_24_HOUR = "kk:mm"

        fun getHour(context: Context?, cal: Calendar): CharSequence {
            val is24Hour = DateFormat.is24HourFormat(context)
            return if (is24Hour) {
                DateFormat.format(DEFAULT_FORMAT_24_HOUR, cal)
            } else {
                DateFormat.format(DEFAULT_FORMAT_12_HOUR, cal)
            }

        }
    }
}

