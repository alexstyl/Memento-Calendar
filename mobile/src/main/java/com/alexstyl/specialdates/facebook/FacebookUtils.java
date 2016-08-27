package com.alexstyl.specialdates.facebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alexstyl.specialdates.contact.actions.IntentAction;
import com.alexstyl.specialdates.util.Utils;

/**
 * <p>Created by Alex on 6/8/2014.</p>
 */
public class FacebookUtils {

    private static final String PAGE_ID = "529432650512872";
    public static final String DATA_PROFILE = "fb://profile/";
    private static final String FB_PAGE_NAME = "days.official";
    private static final String URL_FACEBOOK_PAGE = "https://www.facebook.com/" + FB_PAGE_NAME;
    public static final String URL_MESSENGER = "https://www.facebook.com/messages/";
    public static final String URL_FACEBOOK = "https://m.facebook.com/";
    public static final String DATA_COMPOSE = "fb://messaging/compose/";

    public static void openFacebookPage(Context context) throws ActivityNotFoundException {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DATA_PROFILE + PAGE_ID));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // the fb app is not installed. try via the browser
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_FACEBOOK_PAGE));
            Utils.openIntentSafely(
                    context, new IntentAction() {
                        @Override
                        public void onStartAction(Context context) throws ActivityNotFoundException {
                            context.startActivity(intent);
                        }
                    }
            );
        }

    }

}
