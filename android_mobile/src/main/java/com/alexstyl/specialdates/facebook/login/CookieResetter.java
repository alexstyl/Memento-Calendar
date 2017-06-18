package com.alexstyl.specialdates.facebook.login;

import android.webkit.CookieManager;

class CookieResetter {
    private final CookieManager cookieManager;

    CookieResetter(CookieManager instance) {
        this.cookieManager = instance;
    }

    void clearAll() {
        cookieManager.setCookie(".facebook.com", "locale=");
        cookieManager.setCookie(".facebook.com", "datr=");
        cookieManager.setCookie(".facebook.com", "s=");
        cookieManager.setCookie(".facebook.com", "csm=");
        cookieManager.setCookie(".facebook.com", "fr=");
        cookieManager.setCookie(".facebook.com", "lu=");
        cookieManager.setCookie(".facebook.com", "c_user=");
        cookieManager.setCookie(".facebook.com", "xs=");
        cookieManager.setCookie(".facebook.com", "wd");
        cookieManager.setCookie(".facebook.com", "presence");
        cookieManager.setCookie(".facebook.com", "act");
        cookieManager.setCookie(".facebook.com", "lu");
        cookieManager.setCookie(".facebook.com", "pl");
        cookieManager.setCookie(".facebook.com", "fr");
        cookieManager.setCookie(".facebook.com", "xs");
        cookieManager.setCookie(".facebook.com", "c_user");
        cookieManager.setCookie(".facebook.com", "sb");
        cookieManager.setCookie(".facebook.com", "dats");
        cookieManager.setCookie(".facebook.com", "datr");
        cookieManager.setCookie(".facebook.com", "locale");
        cookieManager.setCookie(".facebook.com", "x-referer");
    }
}
