package com.novoda.simplechromecustomtabs.connection;

import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsSession;

class SimpleChromeCustomTabsSession implements Session {

    private final CustomTabsSession session;

    public static Session newSessionFor(CustomTabsClient client) {
        if (client == null) {
            return NULL_SESSION;
        } else {
            CustomTabsSession session = client.newSession(null);
            return new SimpleChromeCustomTabsSession(session);
        }
    }

    SimpleChromeCustomTabsSession(CustomTabsSession session) {
        this.session = session;
    }

    @Override
    public void mayLaunch(Uri uri) {
        session.mayLaunchUrl(uri, null, null);
    }

    @Override
    public CustomTabsSession getCustomTabsSession() {
        return session;
    }

}
