package com.alexstyl.specialdates.dailyreminder

import android.app.IntentService
import android.content.Intent
import com.alexstyl.android.AlarmManagerCompat
import com.alexstyl.specialdates.MementoApplication
import javax.inject.Inject

class DailyReminderIntentService : IntentService("DailyReminder") {

    var presenter: DailyReminderPresenter? = null
        @Inject set
    var dailyReminderOptions: DailyReminderUserSettings? = null
        @Inject set
    var notifier: DailyReminderNotifier? = null
        @Inject set
    var userSettings: DailyReminderUserSettings? = null
        @Inject set

    override fun onCreate() {
        super.onCreate()
        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val view = NotificationDailyReminderView(notifier!!)
        presenter?.startPresentingInto(view)

        if (dailyReminderOptions!!.isEnabled()) {
            val alarmManagerCompat = AlarmManagerCompat.from(this)

            val scheduler = DailyReminderScheduler(alarmManagerCompat, this)
            scheduler.setupReminder(userSettings)
        }
        presenter?.stopPresenting()
    }
}
