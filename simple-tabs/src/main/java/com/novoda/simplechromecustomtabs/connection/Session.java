package com.novoda.simplechromecustomtabs.connection;

import android.net.Uri;
import android.support.customtabs.CustomTabsSession;

public interface Session {
    void mayLaunch(Uri uri);

    CustomTabsSession getCustomTabsSession();

    Session NULL_SESSION = new Session() {

        @Override
        public void mayLaunch(Uri uri) {
            //no-op
        }

        @Override
        public CustomTabsSession getCustomTabsSession() {
            return null;
        }
    };

}
