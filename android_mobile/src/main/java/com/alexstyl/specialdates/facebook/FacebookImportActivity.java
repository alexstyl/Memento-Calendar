package com.alexstyl.specialdates.facebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.SimpleOnImageLoadedCallback;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;
import com.novoda.notils.meta.AndroidUtils;

public class FacebookImportActivity extends ThemedMementoActivity {

    private FacebookWebView webView;
    private ImageView avatar;
    private TextView helloView;
    private TextView moreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_import);

        Toolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        avatar = Views.findById(this, R.id.facebook_import_avatar);
        helloView = Views.findById(this, R.id.facebook_import_hello);
        moreText = Views.findById(this, R.id.facebook_import_description);

        clearAllCookies();
        webView = Views.findById(this, R.id.facebook_import_webview);
        webView.setListener(facebookCallback);
    }

    private void broadcastUserSignedIn() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(FacebookConstants.ACTION_SIGNED_IN));
        // TODO start update service
    }

    private void clearAllCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
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

    @Override
    protected void onStart() {
        super.onStart();
        webView.loadSignInPage();
    }

    private final FacebookCallback facebookCallback = new FacebookCallback() {
        @Override
        public void onCalendarFound(UserCredentials userCredentials) {
            FacebookImportActivity.this.broadcastUserSignedIn();

            webView.setVisibility(View.GONE);
            helloView.setVisibility(View.VISIBLE);
            avatar.setVisibility(View.VISIBLE);
            moreText.setVisibility(View.VISIBLE);

            Uri uri = ImagePathCreator.INSTANCE.forUid(userCredentials.getUid());
            UILImageLoader imageLoader = UILImageLoader.createCircleLoader(getResources());
            imageLoader.loadImage(uri, new ImageSize(avatar.getWidth(), avatar.getWidth()), new SimpleOnImageLoadedCallback() {
                @Override
                public void onImageLoaded(Bitmap loadedImage) {
                    setCircularAvatarFrom(loadedImage);

                    final Animation animation = AnimationUtils.loadAnimation(FacebookImportActivity.this, R.anim.bounce);
                    BounceInterpolator interpolator = new BounceInterpolator(0.4, 20);
                    animation.setInterpolator(interpolator);
                    avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            avatar.startAnimation(animation);
                        }
                    });

                    avatar.setVisibility(View.VISIBLE);
                    avatar.startAnimation(animation);
                }

                private void setCircularAvatarFrom(Bitmap loadedImage) {
                    avatar.setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(
                            loadedImage,
                            Color.WHITE,
                            getResources().getDimensionPixelSize(R.dimen.facebook_avatar_stroke)
                    ));
                }
            });

            helloView.setText(getString(R.string.facebook_hi, userCredentials.getName()));
        }

        @Override
        public void onSignInComplete() {
            AndroidUtils.requestHideKeyboard(FacebookImportActivity.this, webView);
            webView.setVisibility(View.GONE);
        }

        @Override
        public void onError() {
            Log.e("Fail");
            // update the UI and exit
            // we failed :-1:
        }
    };

}
