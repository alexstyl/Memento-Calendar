package com.alexstyl.specialdates.facebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.android.AndroidDimensionResources;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsMarshaller;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsPersister;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.alexstyl.specialdates.events.database.EventColumns.SOURCE_FACEBOOK;
import static com.novoda.notils.caster.Views.findById;

public class FacebookProfileActivity extends ThemedMementoActivity {

    private static final int LOGOUT_ID = 40444;

    private ImageView profilePicture;
    private TextView userName;
    private BorderImageLoader imageLoader;
    private ExternalNavigator navigator;

    private FacebookLogoutService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_profile);

        MementoToolbar toolbar = findById(this, R.id.memento_toolbar);
        toolbar.setNavigationAsClose();

        profilePicture = findById(this, R.id.facebook_profile_avatar);
        userName = findById(this, R.id.facebook_profile_name);
        findById(this, R.id.facebook_profile_fb_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.toFacebookPage();
            }
        });

        FacebookPreferences preferences = FacebookPreferences.newInstance(this);
        UserCredentials userCredentials = new UserCredentials(1499868106L, "", "Alexandros"); // preferences.retrieveCredentials();
        Analytics analytics = AnalyticsProvider.getAnalytics(this);
        navigator = new ExternalNavigator(this, analytics);

        imageLoader = BorderImageLoader.newInstance(getResources(), new AndroidDimensionResources(getResources()), UILImageLoader.createLoader(getResources()));

        loadProfilePicture(userCredentials);
        displayName(userCredentials);

        ContactEventsMarshaller marshaller = new ContactEventsMarshaller(SOURCE_FACEBOOK);
        FacebookFriendsPersister persister = new FacebookFriendsPersister(new PeopleEventsPersister(new EventSQLiteOpenHelper(this)), marshaller);
        service = new FacebookLogoutService(AndroidSchedulers.mainThread(), preferences, persister, PeopleEventsViewRefresher.get(this), new OnFacebookLogOutCallback() {
            @Override
            public void onUserLogOut() {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, LOGOUT_ID, 0, R.string.log_out);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == LOGOUT_ID) {
            service.logOut();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(thisActivity(), UpcomingEventsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayName(UserCredentials userCredentials) {
        userName.setText(userCredentials.getName());
    }

    private void loadProfilePicture(UserCredentials userCredentials) {
        Uri uri = FacebookImagePathCreator.INSTANCE.forUid(userCredentials.getUid());
        imageLoader.loadImage(profilePicture, uri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.dispose();
    }
}
