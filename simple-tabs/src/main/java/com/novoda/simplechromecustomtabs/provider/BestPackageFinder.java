package com.novoda.simplechromecustomtabs.provider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.WorkerThread;
import android.support.customtabs.CustomTabsService;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class BestPackageFinder {

    private static final String NO_PACKAGE_FOUND = "";
    private static final String ANY_URL = "http://www.example.com";
    private static final Intent INTENT_TO_EXTERNAL_LINK = new Intent(Intent.ACTION_VIEW, Uri.parse(ANY_URL));

    @WorkerThread
    public String findBestPackage(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<String> packagesSupportingCustomTabs = getPackagesSupportingCustomTabs(packageManager);
        if (packagesSupportingCustomTabs.isEmpty()) {
            return NO_PACKAGE_FOUND;
        }

        String defaultPackage = getDefaultPackage(packageManager);
        if (packagesSupportingCustomTabs.contains(defaultPackage)) {
            return defaultPackage;
        }

        return packagesSupportingCustomTabs.get(0);
    }

    private String getDefaultPackage(PackageManager packageManager) {
        ResolveInfo defaultActivityInfo = packageManager.resolveActivity(INTENT_TO_EXTERNAL_LINK, 0);

        if (defaultActivityInfo == null) {
            return NO_PACKAGE_FOUND;
        }

        String packageName = defaultActivityInfo.activityInfo.packageName;

        return TextUtils.isEmpty(packageName) ? NO_PACKAGE_FOUND : packageName;
    }

    private List<String> getPackagesSupportingCustomTabs(PackageManager packageManager) {
        List<ResolveInfo> resolvedInfoList = packageManager.queryIntentActivities(INTENT_TO_EXTERNAL_LINK, 0);
        List<String> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedInfoList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (packageManager.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        return packagesSupportingCustomTabs;
    }

}
