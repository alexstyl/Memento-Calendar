package com.alexstyl.android

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings


class PowerOptimisations(val context: Context) {

    @TargetApi(Build.VERSION_CODES.M)
    fun isIgnoringOptimisations(): Boolean {
        if (!Version.hasMarshmallow()) {
            return true
        }
        val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager
        val packageName = context.packageName
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestIgnore(activity: Activity) {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:${context.packageName}")
        activity.startActivity(intent)
    }
}