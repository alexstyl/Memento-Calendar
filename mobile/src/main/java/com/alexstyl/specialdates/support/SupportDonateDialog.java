package com.alexstyl.specialdates.support;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Firebase;
import com.alexstyl.specialdates.billing.util.IabHelper;
import com.alexstyl.specialdates.billing.util.IabResult;
import com.alexstyl.specialdates.billing.util.Inventory;
import com.alexstyl.specialdates.billing.util.Purchase;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.novoda.notils.logger.simple.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Dialog with options of how the user can support the app
 * <p>Created by alexstyl on 09/08/15.</p>
 */
public class SupportDonateDialog extends MementoActivity implements View.OnClickListener {

    private static final String ITEM_TEST = "android.test.purchased";

    private static final String ITEM_DONATE_1 = "com.alexstyl.specialdates.support_1";
    private static final String ITEM_DONATE_2 = "com.alexstyl.specialdates.support_2";
    private static final String ITEM_DONATE_3 = "com.alexstyl.specialdates.support_3";
    private static final String ITEM_DONATE_4 = "com.alexstyl.specialdates.support_4";
    private static final String ITEM_DONATE_5 = "com.alexstyl.specialdates.support_5";

    private static final int REQUEST_CODE_TEST = 10001;

    private IabHelper iabHelper;

    private boolean mBillingServiceReady;
    private HashMap<String, String> mTokens = new HashMap<>();

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            Log.d("Query inventory finished.");
            if (result.isFailure()) {
                Log.e("Failed to query inventory: " + result);
                return;
            }

            Log.d("Query inventory was successful.");

            for (String token : mTokens.values()) {
                Purchase donationPurchase = inventory.getPurchase(token);
                if (donationPurchase != null /*&& verifyDeveloperPayload(gasPurchase)*/) {
                    Log.d("We have token. Consuming it.");
                    iabHelper.consumeAsync(inventory.getPurchase(token), null);
                }
            }
            // IAB is fully set up.
            mBillingServiceReady = true;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.donate);
        analytics = Firebase.get(this);
        mTokens = new HashMap<>();
        if (BuildConfig.DEBUG) {
            mTokens.put("1", ITEM_TEST);
        } else {
            mTokens.put("1", ITEM_DONATE_1);
            mTokens.put("2", ITEM_DONATE_2);
            mTokens.put("3", ITEM_DONATE_3);
            mTokens.put("4", ITEM_DONATE_4);
            mTokens.put("5", ITEM_DONATE_5);
        }

        setContentView(R.layout.dialog_donate);

        Button donate1 = (Button) findViewById(R.id.donate_1);
        donate1.setText("2.5 €");
        donate1.setOnClickListener(this);
        Button donate2 = (Button) findViewById(R.id.donate_2);
        donate2.setText("5 €");
        donate2.setOnClickListener(this);
        Button donate3 = (Button) findViewById(R.id.donate_3);
        donate3.setText("10 €");
        donate3.setOnClickListener(this);
        Button donate4 = (Button) findViewById(R.id.donate_4);
        donate4.setText("25 €");
        donate4.setOnClickListener(this);
        Button donate5 = (Button) findViewById(R.id.donate_5);
        donate5.setOnClickListener(this);
        donate5.setText("50 €");

        if (isBillingAvailable(this)) {
            initialiseBilling();
        } else {
            ErrorTracker.track(new RuntimeException("Billing is not available"));
            finish();
        }
    }

    private Analytics analytics;

    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            // if we were disposed of in the meantime, quit.
            if (iabHelper == null) {
                return;
            }

            // Don't complain if cancelling
            if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
                return;
            }

            if (!result.isSuccess()) {
                ErrorTracker.track(new RuntimeException("onIabPurchaseFinished error:  " + result.getMessage()));
                return;
            }

            // Purchase was success! Update accordingly
            Log.d("Bought " + purchase.getSku());

            // consume it so that the user can buy a donation again
            iabHelper.consumeAsync(purchase, null);
            Toast.makeText(SupportDonateDialog.this, R.string.thanks_for_support, Toast.LENGTH_SHORT).show();

            analytics.trackAction(successfulDonationEvent());
            finish();
            Log.d("Purchase successful!");
        }

    };

    private ActionWithParameters successfulDonationEvent() {
        return new ActionWithParameters(Action.DONATION, "success", true);
    }

    private void initialiseBilling() {
        if (iabHelper != null) {
            return;
        }
        iabHelper = new IabHelper(this, BuildConfig.API_KEY_VENDING);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

            public void onIabSetupFinished(IabResult result) {
                if (iabHelper == null) {
                    return;
                }
                if (!result.isSuccess()) {
                    ErrorTracker.track(new RuntimeException("Problem setting up in-app billing: " + result.getMessage()));
                    // Oh noes, there was a problem.
                    return;
                }
                iabHelper.queryInventoryAsync(mGotInventoryListener);

            }
        });
    }

    public static boolean isBillingAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        List<ResolveInfo> list = packageManager.queryIntentServices(intent, 0);
        return list.size() > 0;
    }

    @Override
    public void onClick(View v) {
        String token;
        if (BuildConfig.DEBUG) {
            token = ITEM_TEST;
        } else {
            String index = String.valueOf(v.getTag());
            token = mTokens.get(index);
        }

        if (mBillingServiceReady) {
            iabHelper.launchPurchaseFlow(this, token, REQUEST_CODE_TEST, mPurchaseFinishedListener);
        } else {
            ErrorTracker.track(new RuntimeException("Tried to buy but billing was not ready"));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (iabHelper == null || !iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) {
            iabHelper.dispose();
            iabHelper = null;
        }
    }

    public static void displayDialog(Context context) {
        Intent intent = new Intent(context, SupportDonateDialog.class);
        context.startActivity(intent);
    }
}
