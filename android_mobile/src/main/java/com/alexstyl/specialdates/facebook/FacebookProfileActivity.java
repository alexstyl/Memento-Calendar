package com.alexstyl.specialdates.facebook;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsMarshaller;
import com.alexstyl.specialdates.events.peopleevents.AndroidPeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsPersister;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;

import javax.inject.Inject;
import java.net.URI;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.novoda.notils.caster.Views.findById;

public class FacebookProfileActivity extends ThemedMementoActivity implements FacebookProfileView {

    private static final int LOGOUT_ID = 40444;

    private ExternalNavigator navigator;
    private FacebookProfilePresenter presenter;
    private ImageView profilePicture;
    private TextView userName;
    @Inject Analytics analytics;
    @Inject ImageLoader imageLoader;
    @Inject PeopleEventsViewRefresher uiRefresher;
    @Inject CrashAndErrorTracker tracker;
    @Inject FacebookUserSettings facebookSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);
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

        ContactEventsMarshaller marshaller = new ContactEventsMarshaller();
        FacebookFriendsPersister persister = new FacebookFriendsPersister(
                new AndroidPeopleEventsPersister(new EventSQLiteOpenHelper(this), marshaller, tracker));
        navigator = new ExternalNavigator(this, analytics, tracker);

        FacebookLogoutService service = new FacebookLogoutService(
                AndroidSchedulers.mainThread(),
                facebookSettings,
                persister,
                uiRefresher,
                onLogOut()
        );
        presenter = new FacebookProfilePresenter(
                service,
                this,
                facebookSettings
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
        toolbar.displayNavigationIconAsClose();
        setTitle(null);
    }

    @Override
    public void display(UserCredentials userCredentials) {
        userName.setText(userCredentials.getName());
        URI uri = FacebookImagePath.forUid(userCredentials.getUid());
        imageLoader
                .load(uri)
                .asCircle()
                .into(profilePicture);
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
