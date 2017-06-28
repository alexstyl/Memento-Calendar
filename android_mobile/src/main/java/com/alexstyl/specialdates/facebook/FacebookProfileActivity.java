package com.alexstyl.specialdates.facebook;

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
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.alexstyl.specialdates.events.database.EventColumns.SOURCE_FACEBOOK;
import static com.novoda.notils.caster.Views.findById;

public class FacebookProfileActivity extends ThemedMementoActivity {

    private static final int LOGOUT_ID = 40444;

    private ExternalNavigator navigator;
    private FacebookProfilePresenter presenter;
    private Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = AnalyticsProvider.getAnalytics(this);
        analytics.trackScreen(Screen.FACEBOOK_PROFILE);
        setContentView(R.layout.activity_facebook_profile);

        setupToolbar();
        ImageView profilePicture = findById(this, R.id.facebook_profile_avatar);
        TextView userName = findById(this, R.id.facebook_profile_name);

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
        presenter = new FacebookProfilePresenter(
                service,
                profilePicture,
                userName,
                UILImageLoader.createCircleLoaderWithBorder(getResources()),
                preferences
        );
        presenter.startPresenting();
    }

    private void setupToolbar() {
        MementoToolbar toolbar = findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationAsClose();
        setTitle(null);
    }

    private OnFacebookLogOutCallback onLogOut() {
        return new OnFacebookLogOutCallback() {
            @Override
            public void onUserLogOut() {
                finish();
            }
        };
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
