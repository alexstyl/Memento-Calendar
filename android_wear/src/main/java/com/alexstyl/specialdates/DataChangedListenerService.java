package com.alexstyl.specialdates;

import android.content.ComponentName;
import android.support.wearable.complications.ProviderUpdateRequester;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

public class DataChangedListenerService extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);

        ComponentName providerComponentName = new ComponentName(this, ContactEventsProviderService.class);
        ProviderUpdateRequester providerUpdateRequester = new ProviderUpdateRequester(this, providerComponentName);
        providerUpdateRequester.requestUpdateAll();
    }
}
