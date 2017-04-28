package com.alexstyl.specialdates.support;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;

import com.alexstyl.specialdates.Features;

public class AskForSupport {

    private static final long RETRY_INTERVAL = DateUtils.DAY_IN_MILLIS * 1;
    private CallForRatingPreferences preferences;

    public AskForSupport(Context context) {
        this.preferences = new CallForRatingPreferences(context);
    }

    public boolean shouldAskForRating() {
        if (Features.RATE_CARD) {
            return !preferences.hasUserRated() && isTimeToAskAgain();
        }
        return false;
    }

    private boolean isTimeToAskAgain() {
        if (preferences.triggered()) {
            return true;
        }
        long timeSinceLastRate = System.currentTimeMillis() - preferences.lastAskTimeAsked();
        return timeSinceLastRate > RETRY_INTERVAL;
    }

    public void onRateEnd() {
        preferences.setHasUserRated(true);
    }

    public void requestForRatingSooner() {
        preferences.saveToDisplayRating();
    }

    public void askForRatingFromUser(Context context) {
        context.startActivity(new Intent(context, RateDialog.class));
        preferences.setLastAskTimedNow();
    }
}
