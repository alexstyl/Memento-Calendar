/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexstyl.specialdates.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.actions.IntentAction;

/**
 * This class contains static utility methods.
 */
public class Utils {

    // Prevents instantiation.
    private Utils() {
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * Emulate the as operator of C#. If the object can be cast to type it will
     * be casted. If not this returns null.
     */
    public static <T> T as(Class<T> type, Object o) {
        if (type.isInstance(o)) {
            return type.cast(o);
        }
        return null;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Uses static final constants to detect if the device's platform version is
     * Lollipop or later.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Sets the visiblity of the view
     *
     * @param view       The view to hide/show
     * @param setVisible True will make the view VISIBLE, else GONE
     */
    public static void toggleViewVisibility(View view, boolean setVisible) {
        if (view == null) {
            return;
        }
        int visibility = View.GONE;
        if (setVisible) {
            visibility = View.VISIBLE;
        }
        view.setVisibility(visibility);

    }

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
        return getEmailIntent(MementoApplication.DEV_EMAIL, subject, Utils.getDeviceDetailsInfo());

    }

    public static String getDeviceDetailsInfo() {
        StringBuilder builder = new StringBuilder();

        builder.append(getModel()).append(" (").append(getAndroidVersion()).append(")");
        builder.append("\n-------------------------------\n");
        return builder.toString();
    }

    private static String getModel() {
        return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    }

    public static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static boolean hasVibrator(Context context) {
        Vibrator vibr = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        return vibr.hasVibrator();
    }

    /**
     * Starts a {@link IntentAction}. A Toast is displayed if the action throws an ActivityNotFoundException
     *
     * @param context
     * @param action
     * @return
     */
    public static boolean openIntentSafely(Context context, IntentAction action) {
        try {
            action.onStartAction(context);
            return true;
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
            Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT)
                    .show();
        }
        return false;
    }

    public static void shareApp(Context context) {
        openIntentSafely(
                context, new IntentAction() {
                    @Override
                    public void onStartAction(Context context) throws ActivityNotFoundException {
                        String appName = context.getString(R.string.app_name);
                        String shareText = String.format(context.getString(R.string.share_text), appName, MementoApplication.MARKET_LINK_SHORT);

                        Intent intent2 = new Intent(Intent.ACTION_SEND);
                        intent2.setType("text/plain");
                        intent2.putExtra(Intent.EXTRA_TEXT, shareText);
                        context.startActivity(Intent.createChooser(intent2, context.getString(R.string.share_via)));
                    }

                    @Override
                    public String getName() {
                        return "share";
                    }
                }
        );
    }
}
