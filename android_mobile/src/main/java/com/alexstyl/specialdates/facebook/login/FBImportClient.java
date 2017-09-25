package com.alexstyl.specialdates.facebook.login;

import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class FBImportClient extends WebViewClient {
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";
    private static final String MOBILE_HOME = "m.facebook.com/home.php";
    private static final String DESKTOP_HOME = "www.facebook.com/home.php";
    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; U; Linux i686; en   US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
    private static final String URL_WITH_BIRTHDAY_SOURCE = "https://www.facebook.com/events/calendar";

    private final WebView webView;
    private FacebookLogInCallback listener;

    FBImportClient(WebView webview) {
        this.webView = webview;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (isHomePage(url)) {
            view.stopLoading();
            internalOnUserLoggedIn();
        }
    }

    private boolean isHomePage(String url) {
        return url.startsWith(HTTP + MOBILE_HOME) || url.startsWith(HTTPS + MOBILE_HOME)
                || url.startsWith(HTTP + DESKTOP_HOME) || url.startsWith(HTTPS + DESKTOP_HOME);
    }

    private void internalOnUserLoggedIn() {
        switchToDesktopBrowsing();
        webView.loadUrl(URL_WITH_BIRTHDAY_SOURCE);
        listener.onUserCredentialsSubmitted();
    }

    private void switchToDesktopBrowsing() {
        WebSettings settings = webView.getSettings();
        settings.setUserAgentString(DESKTOP_USER_AGENT);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.contains(URL_WITH_BIRTHDAY_SOURCE)) {
            webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        listener.onError(new FacebookLogInException(description));
    }

    public void setListener(FacebookLogInCallback listener) {
        this.listener = listener;
    }
}
