package com.alexstyl.specialdates.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ShareAppIntentCreator;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.datedetails.actions.IntentAction;

public class AppUtils {

    public static Intent getEmailIntent(String to, String subject, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", to, null));
        if (!TextUtils.isEmpty(subject)) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!TextUtils.isEmpty(text)) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        return emailIntent;
    }

    public static Intent getSupportEmailIntent(Context context) {
        String subject = context.getString(R.string.app_name) + " " + MementoApplication.getVersionName(context);
        return getEmailIntent(MementoApplication.DEV_EMAIL, subject, getDeviceDetailsInfo());

    }

    private static String getDeviceDetailsInfo() {
        return getModel() + " (" + getAndroidVersion() + ")" +
                "\n-------------------------------\n";
    }

    private static String getModel() {
        return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    }

    private static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static boolean openIntentSafely(Context context, IntentAction action) {
        try {
            action.onStartAction(context);
            return true;
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
            Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static void shareApp(Context context) {
        openIntentSafely(
                context, new IntentAction() {
                    @Override
                    public void onStartAction(Context context) throws ActivityNotFoundException {
                        StringResources stringResource = new AndroidStringResources(context.getResources());
                        Intent intent = new ShareAppIntentCreator(context, stringResource).buildIntent();
                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_via)));
                    }

                    @Override
                    public String getName() {
                        return "share";
                    }
                }
        );
    }
}
