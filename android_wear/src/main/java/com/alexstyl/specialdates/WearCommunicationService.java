package com.alexstyl.specialdates;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.wear.SharedConstants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.Wearable;

class WearCommunicationService {

    private final GoogleApiClient googleApiClient;

    WearCommunicationService(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    void loadDataItems(final Callback callback) {
        PendingResult<DataItemBuffer> result = Wearable.DataApi.getDataItems(googleApiClient);
        result.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(@NonNull DataItemBuffer dataItems) {
                boolean found = false;
                for (DataItem item : dataItems) {
                    if (uriIsUnsupported(item.getUri())) {
                        continue;
                    }
                    callback.onDataItemsLoaded(item);
                    found = true;
                }
                if (!found) {
                    callback.onNoDataItemsAvailable();
                }
            }
        });
    }

    private boolean uriIsUnsupported(Uri uri) {
        return !uri.getPath().equals(SharedConstants.NEXT_CONTACT_EVENTS_PATH);
    }

    interface Callback {

        void onDataItemsLoaded(DataItem item);

        void onNoDataItemsAvailable();

    }
}
