package com.alexstyl.specialdates.facebook;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsMarshaller;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsPersister;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.alexstyl.specialdates.events.database.EventColumns.SOURCE_FACEBOOK;
import static com.novoda.notils.caster.Views.findById;

public class FacebookProfileActivity extends ThemedMementoActivity implements FacebookProfileView {

    private static final int LOGOUT_ID = 40444;

    private ExternalNavigator navigator;
    private FacebookProfilePresenter presenter;
    private Analytics analytics;
    private ImageLoader imageLoader;
    private ImageView profilePicture;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = AnalyticsProvider.getAnalytics(this);
        analytics.trackScreen(Screen.FACEBOOK_PROFILE);
        setContentView(R.layout.activity_facebook_profile);

        setupToolbar();
        profilePicture = findById(this, R.id.facebook_profile_avatar);
        userName = findById(this, R.id.facebook_profile_name);

        findById(this, R.id.facebook_profile_fb_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.toFacebookPage();
            }
        });

        ContactEventsMarshaller marshaller = new ContactEventsMarshaller(SOURCE_FACEBOOK);
        FacebookFriendsPersister persister = new FacebookFriendsPersister(new PeopleEventsPersister(new EventSQLiteOpenHelper(this)), marshaller);
        FacebookPreferences preferences = FacebookPreferences.newInstance(this);
        navigator = new ExternalNavigator(this, analytics);

        FacebookLogoutService service = new FacebookLogoutService(
                AndroidSchedulers.mainThread(),
                preferences,
                persister,
                PeopleEventsViewRefresher.get(this), onLogOut()
        );
        imageLoader = UILImageLoader.createCircleLoaderWithBorder(getResources());
        presenter = new FacebookProfilePresenter(
                service,
                this,
                preferences
        );
        presenter.startPresenting();
    }

    private OnFacebookLogOutCallback onLogOut() {
        return new OnFacebookLogOutCallback() {
            @Override
            public void onUserLogOut() {
                finish();
            }
        };
    }

    private void setupToolbar() {
        MementoToolbar toolbar = findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationAsClose();
        setTitle(null);
    }

    @Override
    public void display(UserCredentials userCredentials) {
        userName.setText(userCredentials.getName());
        Uri uri = FacebookImagePath.forUid(userCredentials.getUid());
        imageLoader.loadImage(uri, profilePicture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, LOGOUT_ID, 0, R.string.log_out);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == LOGOUT_ID) {
            presenter.logOut();
            analytics.trackFacebookLoggedOut();
            return true;
        } else if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }
}
