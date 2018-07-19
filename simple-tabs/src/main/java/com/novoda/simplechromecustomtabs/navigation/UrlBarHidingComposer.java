package com.novoda.simplechromecustomtabs.navigation;

import android.support.customtabs.CustomTabsIntent;

class UrlBarHidingComposer implements Composer {

    @Override
    public CustomTabsIntent.Builder compose(CustomTabsIntent.Builder builder) {
        return builder.enableUrlBarHiding();
    }

}
