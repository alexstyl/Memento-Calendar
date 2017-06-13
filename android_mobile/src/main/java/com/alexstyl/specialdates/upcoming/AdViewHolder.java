package com.alexstyl.specialdates.upcoming;

import android.view.View;

import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

final class AdViewHolder extends UpcomingRowViewHolder<AdViewModel> {

    private final NativeExpressAdView adView;

    AdViewHolder(View convertView, NativeExpressAdView adView) {
        super(convertView);
        this.adView = adView;
    }

    @Override
    public void bind(AdViewModel element, OnUpcomingEventClickedListener listener) {
        // ads handle clicking by themselves
        adView.loadAd(new AdRequest.Builder()
                              .build());
    }
}
