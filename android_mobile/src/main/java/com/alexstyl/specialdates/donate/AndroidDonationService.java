package com.alexstyl.specialdates.donate;

import android.app.Activity;

import com.alexstyl.specialdates.donate.util.IabHelper;
import com.alexstyl.specialdates.donate.util.IabResult;
import com.alexstyl.specialdates.donate.util.Purchase;

class AndroidDonationService implements DonationService {

    private final IabHelper iabHelper;
    private final Activity activity;
    private final int requestCode;
    private DonationCallbacks listener;

    AndroidDonationService(IabHelper iabHelper, Activity activity, int requestCode) {
        this.iabHelper = iabHelper;
        this.activity = activity;
        this.requestCode = requestCode;
    }

    @Override
    public void setup(final DonationCallbacks listener) {
        this.listener = listener;
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    listener.onDonateException(result.getMessage());
                }
            }
        });
    }

    @Override
    public void placeDonation(final Donation donation) {
        try {
            iabHelper.launchPurchaseFlow(
                    activity, donation.getIdentifier(), requestCode, new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            listener.onDonationFinished(donation);
                        }
                    }
            );
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            listener.onDonateException(e.getMessage());

        }
    }

    @Override
    public void dispose() {
        try {
            iabHelper.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
