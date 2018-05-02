package com.alexstyl.specialdates.addevent.bottomsheet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public final class IntentResolver {

    private final PackageManager packageManager;

    public IntentResolver(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    List<ImagePickerOptionViewModel> createViewModelsFor(Intent intent, String absolutePath) {
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        List<ImagePickerOptionViewModel> viewModels = new ArrayList<>(resolveInfos.size());
        for (ResolveInfo resolveInfo : resolveInfos) {
            Drawable icon = resolveInfo.loadIcon(packageManager);
            String label = String.valueOf(resolveInfo.loadLabel(packageManager));
            Intent launchingIntent = new Intent(intent.getAction());
            launchingIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            viewModels.add(new ImagePickerOptionViewModel(icon, label, launchingIntent, absolutePath));
        }
        return viewModels;
    }
}
