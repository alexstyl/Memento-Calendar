package com.alexstyl.specialdates.addevent.bottomsheet

import android.content.Intent
import android.content.pm.PackageManager
import java.net.URI

class IntentResolver(private val packageManager: PackageManager) {

    internal fun createViewModelsFor(intent: Intent, absolutePath: String): List<PhotoPickerViewModel> {
        return packageManager.queryIntentActivities(intent, 0)
                .map { resolveInfo ->
                    val icon = resolveInfo.loadIcon(packageManager)
                    val label = resolveInfo.loadLabel(packageManager).toString()
                    val launchingIntent = Intent(intent)
                    launchingIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
                    PhotoPickerViewModel(icon, label, launchingIntent, URI.create(absolutePath))
                }
    }
}
