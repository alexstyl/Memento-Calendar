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

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.PayPal;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.actions.IntentAction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains static utility methods.
 */
public class Utils {

    // Prevents instantiation.
    private Utils() {
    }

    /**
     * Uses static final constants to detect if the device's platform version is
     * Honeycomb or later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Uses static final constants to detect if the device's platform version is
     * ICS or later.
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
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

    public static boolean isRunningKitKat() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
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

    /**
     * Returns whether the device has
     *
     * @param context The context to use
     * @return
     */
    @SuppressLint("NewApi")
    public static boolean hasVibrator(Context context) {
        Vibrator vibr = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (!hasHoneycomb()) {
            return vibr != null;
        }
        return vibr.hasVibrator();
    }

    /**
     * Checks if the device is currently connected to the webz!
     *
     * @param context The context to use
     * @return Whether the device is online or not... duh
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        return ni.isConnectedOrConnecting();
    }

    /**
     * Returns the height of the navigation bars height
     *
     * @param context
     * @return
     */
    final public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
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

    /**
     * Checks if the running device has any installed applications that can handle the given intent
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
        );
        return list.size() > 0;
    }

    /**
     * Returns a JSON value from the raw folder, of the given resID
     *
     * @param context The context to use
     * @param resID   The resource ID of the JSON file
     * @return
     * @throws android.content.res.Resources.NotFoundException if the resource is not a JSON
     */
    public static JSONObject getJSON(@NonNull Context context, @RawRes int resID) {
        InputStream inputStream = context.getResources().openRawResource(resID);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new JSONObject(byteArrayOutputStream.toString());
        } catch (JSONException e) {
            throw new Resources.NotFoundException(e.getMessage());
        }
    }

    /**
     * Compares whether one is equal with at least one of the others
     *
     * @param one
     * @param others
     * @return
     */
    public static boolean equalsTo(Object one, Object... others) {
        for (Object other : others) {
            if (one.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public static boolean openPayPalDonation(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(PayPal.URL_DONATIONS));
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            if (BuildConfig.DEBUG) {
                // do nothing if we there is no browser installed
                Toast.makeText(context, "Exception thrown!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return false;
    }

}
