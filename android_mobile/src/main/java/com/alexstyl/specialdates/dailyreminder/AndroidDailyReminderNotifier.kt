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
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.person.PersonActivity
import com.alexstyl.specialdates.settings.NotificationConstants
import java.net.URI

class AndroidDailyReminderNotifier(private val context: Context,
                                   private val notificationManager: NotificationManager,
                                   private val imageLoader: ImageLoader,
                                   private val colors: Colors) : DailyReminderNotifier {

    override fun forContacts(viewModels: List<ContactEventNotificationViewModel>) {
        viewModels.forEach { viewModel ->
            val startIntent = PersonActivity.buildIntentFor(context, viewModel.contact)
            val requestCode = viewModel.contact.contactID.toInt()
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
                            .loadLargeImage(viewModel.contact.imagePath)
                            .setSmallIcon(R.drawable.ic_stat_memento)
                            .setColor(colors.getDailyReminderColor())
                            .build()

            notificationManager.notify(viewModel.notificationId, notification)
        }
    }

    override fun cancelAllEvents() {
        notificationManager.cancel(NotificationConstants.NOTIFICATION_ID_CONTACTS_SUMMARY)
//        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_NAMEDAYS)
//        notificationManager.cancel(NOTIFICATION_ID_DAILY_REMINDER_BANKHOLIDAYS)
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


