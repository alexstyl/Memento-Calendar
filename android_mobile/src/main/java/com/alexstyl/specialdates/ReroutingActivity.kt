package com.alexstyl.specialdates

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.alexstyl.android.Version
import com.alexstyl.specialdates.home.HomeActivity
import javax.inject.Inject

class ReroutingActivity : Activity() {


    @Inject lateinit var errorTracker: CrashAndErrorTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MementoApplication).applicationModule.inject(this)

        if (BuildConfig.DEBUG) {
            showDebugNotification()
        }

        val targetIntent: Intent = createRedirectFor(intent)

        startActivity(targetIntent)

        finish()
    }

    private fun createRedirectFor(intent: Intent): Intent {
        return if (intent.isARedirect()) {
            val targetAction = intent.extras.getString(FCM_EXTRA__TARGET_ACTION)
            val rerouteIntent = Intent(targetAction)

            if (canLaunchIntent(rerouteIntent)) {
                rerouteIntent
            } else {
                errorTracker.track(IllegalArgumentException("Cannot reroute to $targetAction. Cannot resolve intent"))
                HomeActivity.getStartIntent(this)
            }

        } else {
            HomeActivity.getStartIntent(this)
        }
    }

    private fun canLaunchIntent(rerouteIntent: Intent) =
            packageManager.queryIntentActivities(rerouteIntent, 0).size > 0

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
                this, System.currentTimeMillis().toInt(), buildDebugOptionsIntent(), PendingIntent.FLAG_ONE_SHOT
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

    private fun buildDebugOptionsIntent(): Intent {
        return Intent(ACTION_DEBUG_OPTIONS)
                .apply {
                    `package` = BuildConfig.APPLICATION_ID
                }
    }

    companion object {
        const val ACTION_DEBUG_OPTIONS = "com.alexstyl.specialdates.ACTION_DEBUG_OPTIONS"
        const val DEBUG_CHANNEL = "debug_channel"
        const val FCM_EXTRA__TARGET_ACTION = "target_action"
    }

    private fun Intent.isARedirect(): Boolean {
        return this.extras?.containsKey(ReroutingActivity.FCM_EXTRA__TARGET_ACTION) ?: false
    }
}
