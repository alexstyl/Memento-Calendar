package com.alexstyl.specialdates.donate;

import android.app.Activity;
import android.widget.Toast;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.donate.util.IabHelper;
import com.alexstyl.specialdates.donate.util.IabResult;
import com.alexstyl.specialdates.donate.util.Inventory;
import com.alexstyl.specialdates.donate.util.Purchase;

public class AndroidDonationService implements DonationService {

    private final IabHelper iabHelper;
    private final Activity activity;
    private DonationCallbacks listener;

    public AndroidDonationService(IabHelper iabHelper, Activity activity) {
        this.iabHelper = iabHelper;
        this.activity = activity;
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
    public void placeDonation(final Donation donation, int requestCode) {
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

    @Override
    public void checkForDonations() {
        try {
            iabHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    boolean hasDonated = containsDonations(inv);
                    if (hasDonated) {
                        Toast.makeText(activity, R.string.donate_thanks_for_donating, Toast.LENGTH_SHORT).show();
                        DonateMonitor.getInstance().onDonationUpdated();
                    } else {
                        Toast.makeText(activity, R.string.donate_no_donation_found, Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            ErrorTracker.track(e);
        }
    }

    private static boolean containsDonations(Inventory inventory) {
        for (AndroidDonation donation : AndroidDonation.values()) {
            if (inventory.hasPurchase(donation.getIdentifier())) {
                return true;
            }
        }
        return false;
    }
}
