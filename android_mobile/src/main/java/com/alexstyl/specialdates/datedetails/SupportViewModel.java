package com.alexstyl.specialdates.datedetails;

import android.view.animation.Animation;

import com.alexstyl.specialdates.support.Emoticon;

class SupportViewModel implements DateDetailsViewModel {

    private static final String smiley = " " + Emoticon.SMILEY.asText();

    private final Animation heartAnimation;
    private final String description;

    SupportViewModel(Animation heartAnimation, String description) {
        this.heartAnimation = heartAnimation;
        this.description = description;
    }

    Animation getHeartAnimation() {
        return heartAnimation;
    }

    @Override
    public int getViewType() {
        return DateDetailsViewType.RATE_APP;
    }

    public String getDescription() {
        return description;
    }
}
