package com.alexstyl.specialdates.addevent.bottomsheet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

final class IntentResolver {

    private final PackageManager packageManager;

    IntentResolver(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    List<IntentOptionViewModel> createViewModelsFor(Intent intent) {
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        List<IntentOptionViewModel> viewModels = new ArrayList<>(resolveInfos.size());
        for (ResolveInfo resolveInfo : resolveInfos) {
            Drawable icon = resolveInfo.loadIcon(packageManager);
            String label = String.valueOf(resolveInfo.loadLabel(packageManager));
            Intent launchingIntent = new Intent(intent.getAction());
            launchingIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            viewModels.add(new IntentOptionViewModel(icon, label, launchingIntent));
        }
        return viewModels;
    }
}
