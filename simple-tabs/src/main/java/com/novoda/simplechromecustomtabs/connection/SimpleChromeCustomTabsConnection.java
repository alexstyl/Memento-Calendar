package com.novoda.simplechromecustomtabs.connection;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

class SimpleChromeCustomTabsConnection implements Connection, ServiceConnectionCallback {

    private final Binder binder;

    private ConnectedClient client;
    private Session session = Session.NULL_SESSION;
    private Uri pendingUrlToWarmUp;

    SimpleChromeCustomTabsConnection(Binder binder) {
        this.binder = binder;
    }

    @Override
    public void connectTo(@NonNull Activity activity) {
        binder.bindCustomTabsServiceTo(activity.getApplicationContext(), this);
    }

    @Override
    public void onServiceConnected(ConnectedClient client) {
        this.client = client;

        if (isConnected()) {
            session = client.newSession();
            warmUpPendingUrlIfAny();
        }
    }

    private void warmUpPendingUrlIfAny() {
        if (isEmpty(pendingUrlToWarmUp)) {
            return;
        }
        session.mayLaunch(pendingUrlToWarmUp);
        pendingUrlToWarmUp = Uri.EMPTY;
    }

    @Override
    public boolean isConnected() {
        return client != null && client.stillConnected();
    }

    @Override
    public void mayLaunch(Uri uri) {
        if (isEmpty(uri)) {
            return;
        }

        if (hasActiveSession()) {
            session.mayLaunch(uri);
        } else {
            pendingUrlToWarmUp = uri;
        }
    }

    private boolean isEmpty(Uri url) {
        return url == null || url.equals(Uri.EMPTY) ;
    }

    private boolean hasActiveSession() {
        return isConnected() && session != null;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void disconnectFrom(@NonNull Activity activity) {
        binder.unbindCustomTabsService(activity.getApplicationContext());
    }

    @Override
    public boolean isDisconnected() {
        return !isConnected();
    }

    @Override
    public void onServiceDisconnected() {
        if (isConnected()) {
            client.disconnect();
        }
    }

}
