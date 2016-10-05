package com.alexstyl.specialdates;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;
import android.text.format.DateUtils;
import android.util.Log;

import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.wear.SharedConstants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.novoda.notils.string.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactEventsProviderService extends ComplicationProviderService {

    private static final String TAG = ContactEventsProviderService.class.getSimpleName();
    private static final int CONTACT_EVENTS_ACTIVITY_REQUEST_CODE = 100;
    private static final int NO_DATE_AVAILABLE = -1;
    private static final String NO_DATE_PLACEHOLDER = "-";

    private WearCommunicationService wearCommunicationService;

    private Analytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = AnalyticsProvider.getAnalytics(this);
    }

    @Override
    public void onComplicationActivated(int complicationId, int type, ComplicationManager manager) {
        super.onComplicationActivated(complicationId, type, manager);
        analytics.trackAction(new ActionWithParameters(Action.COMPLICATION, "enabled", "true"));
    }

    @Override
    public void onComplicationDeactivated(int complicationId) {
        super.onComplicationDeactivated(complicationId);
        analytics.trackAction(new ActionWithParameters(Action.COMPLICATION, "enabled", "false"));
    }

    @Override
    public void onComplicationUpdate(final int complicationId, final int dataType, final ComplicationManager complicationManager) {

        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                wearCommunicationService.loadDataItems(new WearCommunicationService.Callback() {
                    @Override
                    public void onDataItemsLoaded(DataItem item) {
                        ComplicationData complicationData = createComplicationData(item, dataType);

                        if (complicationData != null) {
                            complicationManager.updateComplicationData(complicationId, complicationData);
                        }
                    }

                    @Override
                    public void onNoDataItemsAvailable() {
                        ComplicationData emptyComplicationData = createComplicationData(dataType, NO_DATE_AVAILABLE, Collections.<String>emptyList());
                        complicationManager.updateComplicationData(complicationId, emptyComplicationData);
                    }
                });
            }

            @Override
            public void onConnectionSuspended(int i) {
                // no-op
            }
        };

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(connectionCallbacks)
                .build();

        wearCommunicationService = new WearCommunicationService(googleApiClient);

        googleApiClient.connect();
    }

    private ComplicationData createComplicationData(DataItem item, int dataType) {
        DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
        long date = dataMap.getLong(SharedConstants.KEY_DATE);
        ArrayList<String> namesList = dataMap.getStringArrayList(SharedConstants.KEY_CONTACTS_NAMES);

        ComplicationData complicationData = createComplicationData(dataType, date, namesList);

        return complicationData;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    private ComplicationData createComplicationData(int dataType, long date, List<String> namesList) {
        ComplicationData complicationData = null;
        switch (dataType) {
            case ComplicationData.TYPE_SHORT_TEXT:
                complicationData = new ComplicationData.Builder(ComplicationData.TYPE_SHORT_TEXT)
                        .setShortText(ComplicationText.plainText(formatShortDate(date)))
                        .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_complication_contact_event))
                        .setTapAction(createContactEventsActivityIntent())
                        .build();
                break;
            case ComplicationData.TYPE_LONG_TEXT:
                complicationData = new ComplicationData.Builder(ComplicationData.TYPE_LONG_TEXT)
                        .setLongTitle(ComplicationText.plainText(formatLongDate(date)))
                        .setLongText(ComplicationText.plainText(StringUtils.join(namesList, ", ")))
                        .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_complication_contact_event))
                        .setTapAction(createContactEventsActivityIntent())
                        .build();
                break;
            default:
                Log.w(TAG, "Unexpected complication type " + dataType);
        }
        return complicationData;
    }

    private CharSequence formatShortDate(long date) {
        if (date == NO_DATE_AVAILABLE) {
            return NO_DATE_PLACEHOLDER;
        }
        return DateUtils.getRelativeTimeSpanString(
                date,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL
        );
    }

    private CharSequence formatLongDate(long date) {
        if (date == NO_DATE_AVAILABLE) {
            return NO_DATE_PLACEHOLDER;
        }
        return DateUtils.getRelativeTimeSpanString(
                date,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS,
                0
        );
    }

    private PendingIntent createContactEventsActivityIntent() {
        Intent intent = new Intent(this, ContactEventsActivity.class);
        return PendingIntent.getActivity(this, CONTACT_EVENTS_ACTIVITY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
