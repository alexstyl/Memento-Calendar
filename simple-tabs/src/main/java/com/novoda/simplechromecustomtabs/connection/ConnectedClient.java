package com.novoda.simplechromecustomtabs.connection;

import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;

class ConnectedClient {

    private static final int NO_FLAGS = 0;

    private CustomTabsClient customTabsClient;

    public ConnectedClient(@NonNull CustomTabsClient customTabsClient) {
        this.customTabsClient = customTabsClient;
    }

    public Session newSession() {
        if (!stillConnected()) {
            throw new IllegalStateException("Cannot start session on a disconnected client. Use stillConnected() to check connection");
        }

        warmup();

        return SimpleChromeCustomTabsSession.newSessionFor(customTabsClient);
    }

    public boolean stillConnected() {
        return customTabsClient != null;
    }

    private void warmup() {
        customTabsClient.warmup(NO_FLAGS);
    }

    public void disconnect() {
        customTabsClient = null;
    }

}
