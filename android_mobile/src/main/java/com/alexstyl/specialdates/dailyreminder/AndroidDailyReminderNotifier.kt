package com.alexstyl.specialdates.dailyreminder

import android.app.NotificationChannel
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
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.alexstyl.android.Version
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.BuildConfig
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.person.PersonActivity
import java.net.URI

class AndroidDailyReminderNotifier(private val context: Context,
                                   private val notificationManager: NotificationManager,
                                   private val imageLoader: ImageLoader,
                                   private val strings: Strings,
                                   private val userSettings: DailyReminderUserSettings,
                                   private val colors: Colors) : DailyReminderNotifier {

    override fun forContacts(viewModels: List<ContactEventNotificationViewModel>) {
        createDailyReminderChannel()

        viewModels.forEach { viewModel ->
            val startIntent = PersonActivity.buildIntentFor(context, viewModel.contact)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    NOTIFICATION_ID_DAILY_REMINDER_CONTACTS + viewModel.contact.contactID.toInt(),
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)

            val lights = NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
                    .setContentTitle(viewModel.title)
                    .setContentText(viewModel.label)
                    .setContentIntent(pendingIntent)
                    .setColor(colors.getDailyReminderColor())
                    .setSound(viewModel.sound.toUri())
                    .setLights(Color.RED, 400, 1000)
            val notification = lights.apply {
                lights.setDefaults(NotificationCompat.DEFAULT_VIBRATE or
                        if (userSettings.isVibrationEnabled()) NotificationCompat.DEFAULT_LIGHTS else 0)
            }
                    .loadImage(viewModel.contact.imagePath)
                    .setSmallIcon(R.drawable.ic_stat_memento)
                    .setGroup(NOTIFICATION_GROUP_DAILY_REMINDER)
                    .build()

            //  TODO .setPublicVersion(publicVersionFor(pendingIntent, viewModels))

            notificationManager.notify(viewModel.notificationId, notification)
        }
    }

//    private fun publicVersionFor(pendingIntent: PendingIntent?, contacts: List<ContactEventNotificationViewModel>): Notification? {
//        // TODO view Model
//        return NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
//                .setSmallIcon(R.drawable.ic_stat_memento)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setContentTitle(context.getString(R.string.contact_celebration_count, contacts.size))
//                .setColor(colors.getDailyReminderColor())
//                .build()
//    }

    private fun createDailyReminderChannel() {
        if (Version.hasOreo()) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(
                            AndroidDailyReminderNotifier.CHANNEL_DAY_REMINDER,
                            strings.dailyReminder(),
                            NotificationManager.IMPORTANCE_DEFAULT
                    )
            )
        }
    }

    override fun forNamedays(names: List<String>, date: Date) {
        TODO()
    }

    private fun createNamedaysChannel() {
        if (Version.hasOreo()) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(
                            AndroidDailyReminderNotifier.CHANNEL_NAMEDAYS,
                            strings.namedays(),
                            NotificationManager.IMPORTANCE_DEFAULT
                    )
            )
        }
    }

    override fun cancelAllEvents() {
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS)
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS)
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS)

    }

    override fun forBankholiday(bankHoliday: BankHoliday) {
        val intent = PendingIntent.getActivity(
                context, NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS,
                HomeActivity.getStartIntent(context),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val subtitle = bankHoliday.holidayName
        val title = context.getString(R.string.dailyreminder_title_bankholiday)
        val builder = NotificationCompat.Builder(context, CHANNEL_BANK_HOLIDAYS)
                .setSmallIcon(R.drawable.ic_stat_bankholidays)
                .setStyle(NotificationCompat.BigTextStyle().bigText(subtitle))
                .setContentTitle(title)
                .setContentText(subtitle)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(colors.getBankholidaysColor())
                .setContentIntent(intent)

        createBankholidayChannel()
        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS, builder.build())
    }

    private fun createBankholidayChannel() {       
        if (Version.hasOreo()) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(
                            AndroidDailyReminderNotifier.CHANNEL_BANK_HOLIDAYS,
                            strings.bankholidays(),
                            NotificationManager.IMPORTANCE_DEFAULT
                    )
            )
        }
    }

    companion object {

        private const val NOTIFICATION_ID_DAILY_REMINDER_CONTACTS = 0
        private const val NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS = 1
        private const val NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS = 2
        private const val NOTIFICATION_GROUP_DAILY_REMINDER = "reminder_group"
        const val CHANNEL_DAY_REMINDER = BuildConfig.APPLICATION_ID + ".channel:daily_reminder"
        const val CHANNEL_NAMEDAYS = BuildConfig.APPLICATION_ID + ".channel:namedays"
        const val CHANNEL_BANK_HOLIDAYS = BuildConfig.APPLICATION_ID + ".channel:bank_holidays"

        private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
            val output = Bitmap.createBitmap(
                    bitmap.width,
                    bitmap.height,
                    Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)

            val color = Color.RED
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawOval(rectF, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)

            return output
        }
    }

    private fun Bitmap.toCircle(): Bitmap? = getCircleBitmap(this)

    private fun NotificationCompat.Builder.loadImage(imagePath: URI): NotificationCompat.Builder = apply {
        val width = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width)
        val height = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height)
        imageLoader
                .load(imagePath)
                .withSize(width, height)
                .synchronously().apply {
                    if (isPresent) {
                        setLargeIcon(get().toCircle())
                    }
                }
    }

}

private fun URI.toUri(): Uri = Uri.parse(this.toString())



