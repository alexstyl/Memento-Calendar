package com.alexstyl.specialdates.dailyreminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.provider.ContactsContract.Contacts.CONTENT_LOOKUP_URI
import android.support.v4.app.NotificationCompat
import com.alexstyl.android.Version
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.dailyreminder.actions.PersonActionsActivity
import com.alexstyl.specialdates.events.namedays.activity.NamedaysOnADayActivity
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.person.PersonActivity
import java.net.URI


class AndroidDailyReminderNotifier(private val context: Context,
                                   private val notificationManager: NotificationManager,
                                   private val imageLoader: ImageLoader,
                                   private val colors: Colors) : DailyReminderNotifier {

    override fun notifyFor(viewModel: DailyReminderViewModel) {
        if (viewModel.contacts.isNotEmpty()) {
            when {
                supportsNotificationGroupping() -> {
                    notifyContacts(viewModel.contacts)
                    notifySummary(viewModel)
                }
                viewModel.contacts.size == 1 -> notifyContacts(viewModel.contacts)
                else -> notifySummary(viewModel)
            }
        }

        if (viewModel.namedays.isPresent) {
            notifyNamedays(viewModel.namedays.get())
        }
        if (viewModel.bankHoliday.isPresent) {
            notifyBankHolidays(viewModel.bankHoliday.get())
        }
    }

    private fun supportsNotificationGroupping() = Version.hasOreo()

    private fun notifyContacts(viewModels: List<ContactEventNotificationViewModel>) {
        viewModels.forEach { viewModel ->
            val requestCode = NotificationConstants.CHANNEL_ID_CONTACTS.hashCode() + viewModel.hashCode()
            val startIntent = PersonActivity.buildIntentFor(context, viewModel.contact)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    requestCode,
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)


            val notification =
                    NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_CONTACTS)
                            .setContentTitle(viewModel.title)
                            .setContentText(viewModel.label)
                            .setContentIntent(pendingIntent)
                            .setActions(viewModel)
                            .loadLargeImage(viewModel.contact.imagePath)
                            .setSmallIcon(R.drawable.ic_stat_memento)
                            .setColor(colors.getDailyReminderColor())
                            .setGroup(NotificationConstants.DAILY_REMINDER_GROUP_ID)
                            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                            .setAutoCancel(true)
                            .addPerson(CONTENT_LOOKUP_URI.buildUpon().appendPath(viewModel.contact.contactID.toString()).build().toString())
                            .build()
            notificationManager.notify(viewModel.notificationId, notification)
        }
    }

    private fun notifySummary(viewModel: DailyReminderViewModel) {
        val startIntent = HomeActivity.getStartIntent(context)
        val requestCode = NotificationConstants.NOTIFICATION_ID_CONTACTS_SUMMARY
        val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val summary = viewModel.summaryViewModel

        val notification =
                NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_CONTACTS)
                        .setContentTitle(summary.title)
                        .setContentText(summary.text)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_stat_memento)
                        .setColor(colors.getDailyReminderColor())
                        .setGroupSummary(true)
                        .setGroup(NotificationConstants.DAILY_REMINDER_GROUP_ID)
                        .setInboxStyle(viewModel.summaryViewModel)
                        .build()

        notificationManager.notify(summary.notificationId, notification)
    }

    private fun notifyBankHolidays(bankHoliday: BankHolidayNotificationViewModel) {
        val startIntent = HomeActivity.getStartIntent(context)
        val requestCode = NotificationConstants.NOTIFICATION_ID_BANK_HOLIDAY
        val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification =
                NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_BANKHOLIDAY)
                        .setContentTitle(bankHoliday.title)
                        .setContentText(bankHoliday.label)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_stat_bankholidays)
                        .setColor(colors.getBankholidaysColor())
                        .build()

        notificationManager.notify(NotificationConstants.NOTIFICATION_ID_BANK_HOLIDAY, notification)
    }

    private fun notifyNamedays(namedays: NamedaysNotificationViewModel) {
        val startIntent = NamedaysOnADayActivity.getStartIntent(context, namedays.date)
        val requestCode = NotificationConstants.NOTIFICATION_ID_NAMEDAYS
        val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification =
                NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_NAMEDAYS)
                        .setContentTitle(namedays.title)
                        .setContentText(namedays.label)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(namedays.label))
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_stat_namedays)
                        .setColor(colors.getNamedaysColor())
                        .build()

        notificationManager.notify(NotificationConstants.NOTIFICATION_ID_NAMEDAYS, notification)
    }

    override fun cancelAllEvents() {
        notificationManager.cancel(NotificationConstants.NOTIFICATION_ID_CONTACTS_SUMMARY)
        notificationManager.cancel(NotificationConstants.NOTIFICATION_ID_BANK_HOLIDAY)
        notificationManager.cancel(NotificationConstants.NOTIFICATION_ID_NAMEDAYS)
    }

    private fun NotificationCompat.Builder.loadLargeImage(imagePath: URI): NotificationCompat.Builder = apply {
        val width = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width)
        val height = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height)
        val bitmap =
                imageLoader
                        .load(imagePath)
                        .withSize(width, height)
                        .synchronously()
        if (bitmap.isPresent) {
            if (Version.hasLollipop()) {
                setLargeIcon(bitmap.get().toCircle())
            } else {
                setLargeIcon(bitmap.get())
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

    private fun NotificationCompat.Builder.setActions(contactEventViewModel: ContactEventNotificationViewModel): NotificationCompat.Builder {
        contactEventViewModel.actions.forEach { actionViewModel ->
            val intent = buildIntentFor(actionViewModel, contactEventViewModel)
            val pendingIntent = PendingIntent.getActivity(context, actionViewModel.id, intent, 0)
            addAction(NotificationCompat.Action(0, actionViewModel.label, pendingIntent))
        }
        return this
    }

    private fun buildIntentFor(actionViewModel: ContactActionViewModel, contactEventViewModel: ContactEventNotificationViewModel): Intent =
            when (actionViewModel.type) {
                ActionType.CALL -> PersonActionsActivity.buildCallIntentFor(context, contactEventViewModel.contact)
                ActionType.SEND_WISH -> PersonActionsActivity.buildSendIntentFor(context, contactEventViewModel.contact)
            }
}

private fun NotificationCompat.Builder.setInboxStyle(viewModel: SummaryNotificationViewModel): NotificationCompat.Builder {
    val inboxStyle = NotificationCompat.InboxStyle()
    viewModel.lines.forEach { line ->
        inboxStyle.addLine(line)
    }
    setStyle(inboxStyle)
    return this
}




