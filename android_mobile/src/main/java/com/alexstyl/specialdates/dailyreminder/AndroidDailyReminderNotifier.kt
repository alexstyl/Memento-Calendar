package com.alexstyl.specialdates.dailyreminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.support.v4.app.NotificationCompat
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.events.namedays.activity.NamedayActivity
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.person.PersonActivity
import com.alexstyl.specialdates.settings.NotificationConstants
import java.net.URI

class AndroidDailyReminderNotifier(private val context: Context,
                                   private val notificationManager: NotificationManager,
                                   private val imageLoader: ImageLoader,
                                   private val colors: Colors) : DailyReminderNotifier {

    override fun notifyFor(viewModel: DailyReminderViewModel) {
        if (viewModel.contacts.isNotEmpty()) {
            notifyContacts(viewModel.contacts) // TODO merge?
            notifySummary(viewModel.summaryViewModel) // TODO merge?
        }
        if (viewModel.namedays.isPresent) {
            notifyNamedays(viewModel.namedays.get())
        }
        if (viewModel.bankHoliday.isPresent) {
            notifyBankHolidays(viewModel.bankHoliday.get())
        }
    }

    private fun notifyBankHolidays(bankHoliday: BankHolidayNotificationViewModel) {
        val startIntent = NamedayActivity.getStartIntent(context, bankHoliday.date)
        val requestCode = NotificationConstants.NOTIFICATION_ID_BANK_HOLIDAY
        val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification =
                NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_CONTACTS)
                        .setContentTitle(bankHoliday.title)
                        .setContentText(bankHoliday.label)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_stat_bankholidays)
                        .setColor(colors.getBankholidaysColor())
                        .build()

        notificationManager.notify(NotificationConstants.NOTIFICATION_ID_BANK_HOLIDAY, notification)
    }

    private fun notifyNamedays(namedays: NamedaysNotificationViewModel) {
        val startIntent = NamedayActivity.getStartIntent(context, namedays.date)
        val requestCode = NotificationConstants.NOTIFICATION_ID_NAMEDAYS
        val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification =
                NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_CONTACTS)
                        .setContentTitle(namedays.title)
                        .setContentText(namedays.label)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_stat_namedays)
                        .setColor(colors.getNamedaysColor())
                        .build()

        notificationManager.notify(NotificationConstants.NOTIFICATION_ID_NAMEDAYS, notification)
    }

    private fun notifySummary(summary: SummaryNotificationViewModel) {
        val startIntent = HomeActivity.getStartIntent(context)
        val requestCode = NotificationConstants.NOTIFICATION_ID_CONTACTS_SUMMARY
        val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification =
                NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_CONTACTS)
                        .setContentTitle(summary.title)
                        .setContentText(summary.label)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_stat_memento)
                        .setColor(colors.getDailyReminderColor())
                        .setGroupSummary(true)
                        .setGroup(NotificationConstants.GROUP_DAILY_REMINDER)
                        .build()

        notificationManager.notify(summary.notificationId, notification)
    }

    private fun notifyContacts(contacts: List<ContactEventNotificationViewModel>) {
        contacts.forEach { contactViewModel ->
            val startIntent = PersonActivity.buildIntentFor(context, contactViewModel.contactEvent.contact)
            val requestCode = NotificationConstants.CHANNEL_ID_CONTACTS.hashCode() + contactViewModel.contactEvent.contact.contactID.toInt()
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    requestCode,
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)

            val notification =
                    NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_CONTACTS)
                            .setContentTitle(contactViewModel.title)
                            .setContentText(contactViewModel.label)
                            .setContentIntent(pendingIntent)
                            .loadLargeImage(contactViewModel.contactEvent.contact.imagePath)
                            .setSmallIcon(R.drawable.ic_stat_memento)
                            .setColor(colors.getDailyReminderColor())
                            .setGroup(NotificationConstants.GROUP_DAILY_REMINDER)
                            .build()

            notificationManager.notify(contactViewModel.notificationId, notification)
        }
    }

    override fun cancelAllEvents() {
        notificationManager.cancelAll()
    }

    private fun NotificationCompat.Builder.loadLargeImage(imagePath: URI): NotificationCompat.Builder = apply {
        val width = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width)
        val height = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height)
        imageLoader
                .load(imagePath)
                .withSize(width, height)
                .synchronously()
                .apply {
                    if (isPresent) {
                        setLargeIcon(get().toCircle())
                    }
                }
    }

    private fun Bitmap.toCircle(): Bitmap? {
        val output = Bitmap.createBitmap(
                this.width,
                this.height,
                Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val color = Color.RED
        val paint = Paint()
        val rect = Rect(0, 0, this.width, this.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, rect, rect, paint)

        return output
    }

}



