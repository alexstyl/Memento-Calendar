package com.alexstyl.specialdates.facebook.login;

import android.app.AlarmManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.alexstyl.specialdates.facebook.FacebookImagePathCreator;
import com.alexstyl.specialdates.facebook.ScreenOrientationLock;
import com.alexstyl.specialdates.facebook.UserCredentials;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsIntentService;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsScheduler;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;

public class FacebookLogInActivity extends ThemedMementoActivity implements FacebookImportView {

    private FacebookFriendsScheduler facebookFriendsScheduler;
    private final FacebookImagePathCreator imagePathCreator = FacebookImagePathCreator.INSTANCE;

    private FacebookWebView webView;
    private ImageView avatar;
    private TextView helloView;
    private TextView moreText;
    private ScreenOrientationLock orientationLock;
    private ProgressBar progress;
    private Button shareButton;
    private Button closeButton;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_log_in);

        orientationLock = new ScreenOrientationLock();
        imageLoader = UILImageLoader.createCircleLoaderWithBorder(getResources());
        facebookFriendsScheduler = new FacebookFriendsScheduler(
                thisActivity(),
                (AlarmManager) getSystemService(ALARM_SERVICE)
        );

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
        webView.setCallback(facebookCallback);
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

    private final FacebookLogInCallback facebookCallback = new FacebookLogInCallback() {
        @Override
        public void onUserCredentialsSubmitted() {
            AndroidUtils.requestHideKeyboard(thisActivity(), webView);
            showLoading();
        }

        @Override
        public void onUserLoggedIn(UserCredentials credentials) {
            fetchFacebookFriends();
            showData(credentials);
        }

        private void fetchFacebookFriends() {
            facebookFriendsScheduler.scheduleNext();

            startFacebookFetchService();
        }

        private void startFacebookFetchService() {
            Intent intent = new Intent(thisActivity(), FacebookFriendsIntentService.class);
            startService(intent);
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
        //  TODO webView.setVisibility(View.GONE);
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
        imageLoader.loadImage(uri, avatar);

        animateAvatarWithBounce();
        avatar.setVisibility(View.VISIBLE);
        helloView.setText(getString(R.string.facebook_hi, userCredentials.getName()));
    }

    private void animateAvatarWithBounce() {
        final Animation animation = AnimationUtils.loadAnimation(FacebookLogInActivity.this, R.anim.bounce);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
