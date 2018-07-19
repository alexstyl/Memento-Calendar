package com.novoda.simplechromecustomtabs.navigation;

import android.support.customtabs.CustomTabsIntent;

public interface Composer {

    CustomTabsIntent.Builder compose(CustomTabsIntent.Builder builder);

}
