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
import android.support.v4.app.NotificationCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.alexstyl.android.Version
import com.alexstyl.resources.Colors
import com.alexstyl.resources.DimensionResources
import com.alexstyl.specialdates.BuildConfig
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.activity.NamedayActivity
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.person.PersonActivity
import com.alexstyl.specialdates.util.NaturalLanguageUtils
import java.util.ArrayList

class DailyReminderNotifier constructor(private val context: Context,
                                        private val notificationManager: NotificationManager,
                                        private val imageLoader: ImageLoader,
                                        private val strings: Strings,
                                        private val colors: Colors,
                                        private val dimensions: DimensionResources,
                                        private val preferences: DailyReminderPreferences) {


    fun forDailyReminder(date: Date, events: List<ContactEvent>) {
        createDailyReminderChannel()
        val width = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width)
        val height = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height)

        val contacts = ArrayList<Contact>()
        for ((_, _, _, contact) in events) {
            contacts.add(contact)
        }

        events.forEach { event ->
            val startIntent = PersonActivity.buildIntentFor(context, event.contact)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    NOTIFICATION_ID_DAILY_REMINDER_CONTACTS + event.contact.contactID.toInt(),
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)

            notificationManager.notify(
                    NOTIFICATION_ID_DAILY_REMINDER_CONTACTS + event.contact.contactID.toInt(),
                    NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
                            .setSmallIcon(R.drawable.ic_stat_memento)
                            .setContentTitle(event.contact.displayName.toString())
                            .setContentText(createTextFor(event, date))
                            .setContentIntent(pendingIntent)
                            .setColor(colors.getDailyReminderColor())
                            .setSound(preferences.ringtoneSelected)
                            .setGroup(NOTIFICATION_GROUP_DAILY_REMINDER)
                            .apply {
                                imageLoader
                                        .load(event.contact.imagePath)
                                        .withSize(width, height)
                                        .synchronously().apply {
                                            if (isPresent) {
                                                setLargeIcon(get().toCircle())
                                            }
                                        }
                            }.apply {
                                if (supportsPublicNotifications()) {
                                    val publicTitle = context.getString(R.string.contact_celebration_count, contacts.size)
                                    setPublicVersion(
                                            NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
                                                    .setSmallIcon(R.drawable.ic_stat_memento)
                                                    .setAutoCancel(true)
                                                    .setContentIntent(pendingIntent)
                                                    .setContentTitle(publicTitle)
                                                    .setColor(colors.getDailyReminderColor())
                                                    .build())
                                }
                            }
                            .build()
            )
        }
        val startIntent = HomeActivity.getStartIntent(context)
        val pendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID_DAILY_REMINDER_CONTACTS,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)


        val groupBuilder = NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
                .setContentTitle(NaturalLanguageUtils.joinContacts(strings, contacts, MAX_CONTACTS))
                // .setContentText("Content Text")
                .setGroupSummary(true)
                .setGroup(NOTIFICATION_GROUP_DAILY_REMINDER)
                .setColor(colors.getDailyReminderColor())
                .setSmallIcon(R.drawable.ic_stat_memento)
                .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS, groupBuilder.build())
    }

    private fun createTextFor(it: ContactEvent, date: Date): SpannableString {
        val label = it.getLabel(date, strings)
        return SpannableString(label).apply {
            setSpan(ForegroundColorSpan(colors.getColorFor(it.type)), 0, label.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun createDailyReminderChannel() {
        if (Version.hasOreo()) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(
                            DailyReminderNotifier.CHANNEL_DAY_REMINDER,
                            strings.dailyReminder(),
                            NotificationManager.IMPORTANCE_DEFAULT
                    )
            )
        }
    }

    private fun supportsPublicNotifications(): Boolean {
        return Version.hasLollipop()
    }

    fun forNamedays(names: List<String>, date: Date) {
        if (names.isEmpty()) {
            return
        }
        val intent = PendingIntent.getActivity(
                context, NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS,
                NamedayActivity.getStartIntent(context, date),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val subtitle = NaturalLanguageUtils.join(strings, names, 1)
        val fullsubtitle = TextUtils.join(", ", names)

        val builder = NotificationCompat.Builder(context, CHANNEL_NAMEDAYS)
                .setSmallIcon(R.drawable.ic_stat_namedays)
                .setStyle(NotificationCompat.BigTextStyle().bigText(fullsubtitle))
                .setContentTitle(strings.todaysNamedays(names.size))
                .setContentText(subtitle)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(colors.getNamedaysColor())
                .setContentIntent(intent)
        if (names.size > 1) {
            builder.setNumber(names.size)
        }
        createNamedaysChannel()
        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS, builder.build())
    }

    private fun createNamedaysChannel() {
        if (Version.hasOreo()) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(
                            DailyReminderNotifier.CHANNEL_NAMEDAYS,
                            strings.namedays(),
                            NotificationManager.IMPORTANCE_DEFAULT
                    )
            )
        }
    }

    fun cancelAllEvents() {
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS)
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS)
        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS)

    }

    fun forBankholiday(bankHoliday: BankHoliday) {
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
                            DailyReminderNotifier.CHANNEL_BANK_HOLIDAYS,
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
        private const val MAX_CONTACTS = 3
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
}

