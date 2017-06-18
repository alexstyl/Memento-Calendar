package com.alexstyl.specialdates.facebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ShareAppIntentCreator;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.images.SimpleOnImageLoadedCallback;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;

public class FacebookImportActivity extends ThemedMementoActivity implements FacebookImportView {

    private FacebookWebView webView;
    private ImageView avatar;
    private TextView helloView;
    private TextView moreText;
    private ScreenOrientationLock orientationLock;
    private ProgressBar progress;
    private final FacebookImagePathCreator imagePathCreator = FacebookImagePathCreator.INSTANCE;
    private CircularDrawableFactory circularDrawableFactory;
    private Button shareButton;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_import);

        orientationLock = new ScreenOrientationLock();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.facebook_avatar_stroke);
        circularDrawableFactory = new CircularDrawableFactory(getResources(), dimensionPixelSize);

        Toolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        avatar = Views.findById(this, R.id.facebook_import_avatar);
        helloView = Views.findById(this, R.id.facebook_import_hello);
        moreText = Views.findById(this, R.id.facebook_import_description);
        progress = Views.findById(this, R.id.progress);
        shareButton = Views.findById(this, R.id.facebook_import_share);
        shareButton.setOnClickListener(shareAppIntentOnClick());
        closeButton = Views.findById(this, R.id.facebook_import_close);
        closeButton.setOnClickListener(finishActivityOnClick());

        new CookieResetter(CookieManager.getInstance()).clearAll();
        webView = Views.findById(this, R.id.facebook_import_webview);
        webView.setListener(facebookCallback);
    }

    private View.OnClickListener finishActivityOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private View.OnClickListener shareAppIntentOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareAppIntentCreator appIntentCreator = new ShareAppIntentCreator(thisActivity(), new AndroidStringResources(getResources()));
                Intent intent = appIntentCreator.buildIntent();
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        webView.loadSignInPage();
    }

    private final FacebookCallback facebookCallback = new FacebookCallback() {
        @Override
        public void onSignedInThroughWebView() {
            AndroidUtils.requestHideKeyboard(thisActivity(), webView);
            showLoading();
        }

        @Override
        public void onCalendarFound(UserCredentials userCredentials) {
            FacebookImportActivity.this.broadcastUserSignedIn();
            showData(userCredentials);
        }

        @Override
        public void onError() {
            showError();
        }
    };

    @Override
    public void showLoading() {
        orientationLock.lock(thisActivity());

        progress.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        avatar.setVisibility(View.GONE);
        helloView.setVisibility(View.GONE);
        moreText.setVisibility(View.GONE);
        closeButton.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);
    }

    @Override
    public void showData(UserCredentials userCredentials) {
        progress.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        avatar.setVisibility(View.VISIBLE);
        helloView.setVisibility(View.VISIBLE);
        moreText.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.VISIBLE);
        shareButton.setVisibility(View.VISIBLE);

        Uri uri = imagePathCreator.forUid(userCredentials.getUid());
        UILImageLoader imageLoader = UILImageLoader.createCircleLoader(getResources());
        imageLoader.loadImage(uri, new ImageSize(avatar.getWidth(), avatar.getWidth()), new SimpleOnImageLoadedCallback() {
            @Override
            public void onImageLoaded(Bitmap loadedImage) {
                Drawable drawable = circularDrawableFactory.from(loadedImage);
                avatar.setImageDrawable(drawable);
            }
        });

        animateAvatarWithBounce();
        avatar.setVisibility(View.VISIBLE);
        helloView.setText(getString(R.string.facebook_hi, userCredentials.getName()));
    }

    private void animateAvatarWithBounce() {
        final Animation animation = AnimationUtils.loadAnimation(FacebookImportActivity.this, R.anim.bounce);
        animation.setInterpolator(new BounceInterpolator());
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar.startAnimation(animation);
            }
        });
        avatar.startAnimation(animation);
    }

    @Override
    public void showError() {
        avatar.setVisibility(View.VISIBLE);
        avatar.setImageResource(R.drawable.ic_facebook_sad);

        helloView.setVisibility(View.VISIBLE);
        helloView.setText(R.string.facebook_error);
        closeButton.setVisibility(View.VISIBLE);
        moreText.setVisibility(View.VISIBLE);
        moreText.setText(R.string.facebook_try_again);

        progress.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);
    }

    private void broadcastUserSignedIn() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(FacebookConstants.ACTION_SIGNED_IN));
        // TODO start update service
    }
}
