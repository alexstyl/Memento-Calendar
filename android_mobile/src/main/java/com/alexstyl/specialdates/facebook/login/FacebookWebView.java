package com.alexstyl.specialdates.facebook.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.facebook.FacebookUserSettings;

import javax.inject.Inject;

public class FacebookWebView extends WebView {

    private FacebookLogInCallback callback;
    private FBImportClient client;
    @Inject FacebookUserSettings facebookUserSettings;

    public FacebookWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();

    }

    public FacebookWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup() {
        ((MementoApplication) getContext().getApplicationContext()).getApplicationModule().inject(this);

        clearCache(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);

        addJavascriptInterface(new FacebookJavaScriptInterface(), "HTMLOUT");
        client = new FBImportClient(this);
        setWebViewClient(client);
    }

    public void loadLogInPage() {
        loadUrl("https://m.facebook.com/login");
    }

    public void setCallback(FacebookLogInCallback callback) {
        this.callback = callback;
        client.setListener(callback);
    }

    private class FacebookJavaScriptInterface {

        private UserCredentialsExtractorTask userCredentialsExtractorTask;

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            if (userCredentialsExtractorTask == null) {
                userCredentialsExtractorTask = new UserCredentialsExtractorTask(html, facebookUserSettings, callback);
                userCredentialsExtractorTask.execute();
            }
        }
    }
}
