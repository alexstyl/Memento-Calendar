package com.alexstyl.specialdates;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.wear.SharedConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.novoda.notils.string.StringUtils;

import java.util.ArrayList;

public class ContactEventsActivity extends Activity {

    private static final String TAG = ContactEventsActivity.class.getSimpleName();

    private TextView dateText;
    private TextView namesText;
    private TextView emptyText;
    private LinearLayout eventContainer;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_events);

        dateText = (TextView) findViewById(R.id.contact_events_date_text);
        namesText = (TextView) findViewById(R.id.contact_events_names_text);
        emptyText = (TextView) findViewById(R.id.contact_events_empty_text);
        eventContainer = (LinearLayout) findViewById(R.id.contact_events_event_container);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(connectionCallback)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
    }

    private final GoogleApiClient.ConnectionCallbacks connectionCallback = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Wearable.DataApi.addListener(googleApiClient, dataListener);
            WearCommunicationService wearCommunicationService = new WearCommunicationService(googleApiClient);
            wearCommunicationService.loadDataItems(itemsLoadedCallback);
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d(TAG, "connectionSuspended: " + i);
        }
    };

    private final GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.d(TAG, "connectionFailed: " + connectionResult);
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(ContactEventsActivity.this, connectionResult.getErrorCode(), 100);
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    ContactEventsActivity.this.finish();
                }
            });
            errorDialog.show();
        }
    };

    private WearCommunicationService.Callback itemsLoadedCallback = new WearCommunicationService.Callback() {
        @Override
        public void onDataItemsLoaded(DataItem item) {
            displayDataItem(item);
        }

        @Override
        public void onNoDataItemsAvailable() {
            showEmptyItems();
        }
    };

    private void showEmptyItems() {
        eventContainer.setVisibility(View.GONE);
        emptyText.setVisibility(View.VISIBLE);
    }

    private final DataApi.DataListener dataListener = new DataApi.DataListener() {
        @Override
        public void onDataChanged(DataEventBuffer dataEventBuffer) {
            for (DataEvent event : dataEventBuffer) {
                if (event.getType() == DataEvent.TYPE_CHANGED) {
                    DataItem item = event.getDataItem();
                    if (uriIsSupported(item.getUri())) {
                        displayDataItem(item);
                    }
                }
            }
        }
    };

    private boolean uriIsSupported(Uri uri) {
        return uri.getPath().equals(SharedConstants.NEXT_CONTACT_EVENTS_PATH);
    }

    private void displayDataItem(DataItem item) {
        DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
        long date = dataMap.getLong(SharedConstants.KEY_DATE);
        CharSequence dateString = formatDate(date);
        ArrayList<String> namesList = dataMap.getStringArrayList(SharedConstants.KEY_CONTACTS_NAMES);

        dateText.setText(dateString);
        namesText.setText(StringUtils.join(namesList, "\n"));
        emptyText.setVisibility(View.GONE);
        eventContainer.setVisibility(View.VISIBLE);
    }

    private CharSequence formatDate(long date) {
        return DateUtils.getRelativeTimeSpanString(
                date,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS,
                0
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(googleApiClient, dataListener);
        googleApiClient.disconnect();
    }
}
