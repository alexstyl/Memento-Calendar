package com.alexstyl.specialdates.dailyreminder

import android.app.Notification
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
import android.graphics.Typeface
import android.support.v4.app.NotificationCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
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
        var largeIcon: Bitmap? = null
        val contactCount = events.size

        if (shouldDisplayContactImage(contactCount)) {
            val (_, _, imagePath) = events[0].contact
            val size = dimensions.getPixelSize(android.R.dimen.notification_large_icon_width)
            val loadedIcon = imageLoader.load(imagePath)
                    .withSize(size, size)
                    .synchronously()
            if (Version.hasLollipop() && loadedIcon.isPresent) {
                // in Lollipop the notifications is the default to use Rounded Images
                largeIcon = getCircleBitmap(loadedIcon.get())
            }
        }

        val startIntent = HomeActivity.getStartIntent(context)
        val intent = PendingIntent.getActivity(
                context, NOTIFICATION_ID_DAILY_REMINDER_CONTACTS,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val contacts = ArrayList<Contact>()
        for ((_, _, _, contact) in events) {
            contacts.add(contact)
        }
        val title = NaturalLanguageUtils.joinContacts(strings, contacts, MAX_CONTACTS)

        val builder = NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
                .setSmallIcon(R.drawable.ic_stat_memento)
                .setContentTitle(title)
                .setLargeIcon(largeIcon)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setNumber(events.size)
                .setColor(colors.getDailyReminderColor())
                .setSound(preferences.ringtoneSelected)

        if (events.size == 1) {
            val event = events[0]
            val msg = event.getLabel(date, strings)

            val bigTextStyle = NotificationCompat.BigTextStyle().bigText(msg)
            bigTextStyle.setBigContentTitle(title)
            builder.setContentText(msg)

            builder.setStyle(bigTextStyle)

        } else if (contacts.size > 1) {
            val inboxStyle = NotificationCompat.InboxStyle()
            inboxStyle.setBigContentTitle(title)

            for (i in events.indices) {

                val event = events[i]
                val contact = event.contact
                val name = contact.displayName.toString()

                val lineFormatted = name + "\t\t" + event.getLabel(date, strings)

                val sb = SpannableString(lineFormatted)
                sb.setSpan(StyleSpan(Typeface.BOLD), 0, name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                inboxStyle.addLine(sb)
            }

            builder.setStyle(inboxStyle)
            builder.setContentText(TextUtils.join(", ", contacts))
        }

        if (supportsPublicNotifications()) {
            val publicTitle = context.getString(R.string.contact_celebration_count, contactCount)
            builder.setPublicVersion(
                    NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
                            .setSmallIcon(R.drawable.ic_stat_memento)
                            .setAutoCancel(true)
                            .setContentIntent(intent)
                            .setContentTitle(publicTitle)
                            .setColor(colors.getDailyReminderColor()).build())
        }

        if (preferences.vibrationSet) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE)
        }

        val notification = builder.build()

        createDailyReminderChannel()
        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS, notification)
    }

    fun forDailyReminderAll(events: List<ContactEvent>) {
        createDailyReminderChannel()
        // TODO pass available actions and include CALL + SEND WISHES
        val contacts = ArrayList<Contact>()
        for ((_, _, _, contact) in events) {
            contacts.add(contact)
        }
        val width = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width)
        val height = context.resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height)
        events.forEach {
            val startIntent = PersonActivity.buildIntentFor(context, it.contact)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    NOTIFICATION_ID_DAILY_REMINDER_CONTACTS + it.contact.contactID.toInt(),
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)

            notificationManager.notify(
                    NOTIFICATION_ID_DAILY_REMINDER_CONTACTS + it.contact.contactID.toInt(),
                    NotificationCompat.Builder(context, CHANNEL_DAY_REMINDER)
                            .setSmallIcon(R.drawable.ic_stat_memento)
                            .setContentTitle(it.contact.displayName.toString())
                            .setContentIntent(pendingIntent)
                            .setColor(colors.getDailyReminderColor())
                            .setSound(preferences.ringtoneSelected)
                            .setGroup(NOTIFICATION_GROUP_DAILY_REMINDER)
                            .apply {
                                imageLoader
                                        .load(it.contact.imagePath)
                                        .withSize(width, height)
                                        .synchronously().apply {
                                            if (isPresent) {
                                                setLargeIcon(get().toCircle())
                                            }
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
                .setContentTitle("Summary Title")
                .setContentText("Content Text")
                .setGroupSummary(true)
                .setGroup(NOTIFICATION_GROUP_DAILY_REMINDER)
                .setSmallIcon(R.drawable.ic_stat_memento)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Big Text"))
                .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER_CONTACTS, groupBuilder.build())
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

    private fun shouldDisplayContactImage(contactCount: Int): Boolean {
        return contactCount == 1
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

