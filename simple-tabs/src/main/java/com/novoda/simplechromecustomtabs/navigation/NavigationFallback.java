package com.novoda.simplechromecustomtabs.navigation;

import android.net.Uri;

public interface NavigationFallback {

    void onFallbackNavigateTo(Uri url);

}
