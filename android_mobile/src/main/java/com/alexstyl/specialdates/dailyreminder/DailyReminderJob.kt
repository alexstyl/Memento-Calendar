package com.alexstyl.specialdates.dailyreminder

import com.evernote.android.job.DailyJob

class DailyReminderJob(private val presenter: DailyReminderPresenter,
                       private val notifier: DailyReminderNotifier) : DailyJob() {
    
    companion object {
        const val TAG = "Daily_reminder"
    }

    override fun onRunDailyJob(params: Params): DailyJobResult {

        val view = NotificationDailyReminderView(notifier)
        presenter.startPresentingInto(view)
        presenter.stopPresenting()
        return DailyJobResult.SUCCESS
    }
}
