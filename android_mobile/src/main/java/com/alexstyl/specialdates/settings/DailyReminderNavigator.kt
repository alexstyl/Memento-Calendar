package com.alexstyl.specialdates.settings

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.alexstyl.specialdates.MementoConstants.PACKAGE_NAME

@TargetApi(Build.VERSION_CODES.O)
class DailyReminderNavigator {

    fun openAdvancedSettings(activity: Activity) {
        activity.startActivity(
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, PACKAGE_NAME)
        )
    }
}
