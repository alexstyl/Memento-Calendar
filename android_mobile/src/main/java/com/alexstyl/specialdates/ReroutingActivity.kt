package com.alexstyl.specialdates

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.alexstyl.android.Version
import com.alexstyl.android.toUri
import com.alexstyl.specialdates.dailyreminder.NotificationConstants
import com.alexstyl.specialdates.debug.DebugActivity
import com.alexstyl.specialdates.home.HomeActivity

class ReroutingActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            showDebugNotification()
        }

        val intent = HomeActivity.getStartIntent(this)
        startActivity(intent)

        finish()
    }

    private fun showDebugNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Version.hasOreo()) {

            val contactsChannel = NotificationChannel(
                    DEBUG_CHANNEL,
                    "Debug",
                    NotificationManager.IMPORTANCE_MIN)

            contactsChannel.enableLights(false)
            contactsChannel.enableVibration(false)

            notificationManager.createNotificationChannel(contactsChannel)
        }

        val debugPendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, DebugActivity::class.java), 0
        )

        val debugNotification = NotificationCompat.Builder(this, DEBUG_CHANNEL)
                .setContentTitle("Memento Debug")
                .setContentText("Tap for debug options")
                .setContentIntent(debugPendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setSmallIcon(R.drawable.ic_stat_memento)
                .setColor(Color.GRAY)
                .build()

        notificationManager.notify(1, debugNotification)
    }

    companion object {
        const val DEBUG_CHANNEL = "debug_channel"
    }
}