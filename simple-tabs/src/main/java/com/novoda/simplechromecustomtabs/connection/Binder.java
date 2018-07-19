package com.novoda.simplechromecustomtabs.connection;

import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.util.Log;

import com.novoda.simplechromecustomtabs.provider.AvailableAppProvider;

class Binder {

    private final AvailableAppProvider availableAppProvider;
    private ServiceConnection serviceConnection;

    Binder(@NonNull AvailableAppProvider availableAppProvider) {
        this.availableAppProvider = availableAppProvider;
    }

    public void bindCustomTabsServiceTo(@NonNull final Context context, ServiceConnectionCallback callback) {
        if (isConnected()) {
            return;
        }

        serviceConnection = new ServiceConnection(callback);
        availableAppProvider.findBestPackage(
                new AvailableAppProvider.PackageFoundCallback() {
                    @Override
                    public void onPackageFound(String packageName) {
                        CustomTabsClient.bindCustomTabsService(context, packageName, serviceConnection);
                    }

                    @Override
                    public void onPackageNotFound() {
                        serviceConnection = null;
                    }
                }, context
        );
    }

    private boolean isConnected() {
        return serviceConnection != null;
    }

    public void unbindCustomTabsService(@NonNull Context context) {
        if (isDisconnected()) {
            return;
        }

        try {
            context.unbindService(serviceConnection);
        } catch (IllegalArgumentException iae) {
            Log.e("SimpleChromeCustomTabs", "There was a problem unbinding from a CustomTabs service. :/", iae);
        } finally {
            serviceConnection.onServiceDisconnected(null);
            serviceConnection = null;
        }
    }

    private boolean isDisconnected() {
        return !isConnected();
    }

    private static class ServiceConnection extends CustomTabsServiceConnection {
        private final ServiceConnectionCallback serviceConnectionCallback;

        public ServiceConnection(ServiceConnectionCallback connectionCallback) {
            serviceConnectionCallback = connectionCallback;
        }

        @Override
        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
            if (hasServiceConnectionCallback()) {
                ConnectedClient connectedClient = new ConnectedClient(client);
                serviceConnectionCallback.onServiceConnected(connectedClient);
            }
        }

        private boolean hasServiceConnectionCallback() {
            return serviceConnectionCallback != null;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (hasServiceConnectionCallback()) {
                serviceConnectionCallback.onServiceDisconnected();
            }
        }
    }

}
