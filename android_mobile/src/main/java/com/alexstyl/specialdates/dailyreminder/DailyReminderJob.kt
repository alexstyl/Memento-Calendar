package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.Date
import com.evernote.android.job.DailyJob

class DailyReminderJob(private val presenter: DailyReminderPresenter,
                       private val notifier: DailyReminderNotifier) : DailyJob() {

    override fun onRunDailyJob(params: Params): DailyJobResult {
        val extras = params.extras
        val date = if (extras.containsDate()) {
            extras.getDate()
        } else {
            Date.today()
        }

        val view = NotificationDailyReminderView(notifier)
        presenter.startPresentingInto(view, date)
        presenter.stopPresenting()
        return DailyJobResult.SUCCESS
    }

    companion object {
        const val TAG = "Daily_reminder"
    }

}


