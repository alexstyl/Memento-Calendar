package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.view.animation.AnimationUtils;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.support.Emoticon;

class SupportViewModelFactory {
    private final Context context;
    private final StringResources stringResources;

    SupportViewModelFactory(Context context, StringResources stringResources) {
        this.context = context;
        this.stringResources = stringResources;
    }

    DateDetailsViewModel createViewModel() {
        String text = stringResources.getString(R.string.support_app_description_short) + " " + Emoticon.SMILEY.asText();
        return new SupportViewModel(AnimationUtils.loadAnimation(context, R.anim.heartbeat), text);
    }
}
