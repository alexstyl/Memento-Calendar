package com.alexstyl.specialdates.dailyreminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.date.getDateExtra
import com.alexstyl.specialdates.date.todaysDate
import javax.inject.Inject

class DailyReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var presenter: DailyReminderPresenter
    @Inject lateinit var notifier: DailyReminderNotifier
    @Inject lateinit var scheduler: DailyReminderScheduler
    @Inject lateinit var userSettings: DailyReminderUserSettings


    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as MementoApplication).applicationModule.inject(this)
        val date = intent.getDateExtra(todaysDate())

        val view = NotificationDailyReminderView(notifier)
        presenter.startPresentingInto(view, date)
        presenter.stopPresenting()

        scheduler.scheduleReminderFor(userSettings.getTimeSet())
    }

}
