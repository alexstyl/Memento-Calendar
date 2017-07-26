package com.alexstyl.specialdates.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.handle;

public class PersonActivity extends ThemedMementoActivity implements PersonView {

    private static final String EXTRA_CONTACT_SOURCE = "extra:source";
    private static final String EXTRA_CONTACT_ID = "extra:id";

    private PersonPresenter presenter;
    private ImageView avatarView;
    private ImageLoader imageLoader;
    private TextView personNameView;
    private TextView ageAndSignView;
    private ViewPager viewPager;
    private ContactItemsAdapter adapter;
    private ExternalNavigator navigator;

    private Optional<Contact> displayingContact = Optional.absent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        navigator = new ExternalNavigator(this, AnalyticsProvider.getAnalytics(this));
        StringResources stringResources = new AndroidStringResources(getResources());// TODO inject this

        ContactActionsFactory actionsFactory = new AndroidContactActionsFactory(thisActivity());
        presenter = new PersonPresenter(
                this,
                PeopleEventsProvider.newInstance(this),
                new PersonCallProvider(
                        new AndroidContactActionsProvider(getContentResolver(), stringResources, thisActivity(), getPackageManager(), actionsFactory),
                        new FacebookContactActionsProvider(stringResources, getResources(), actionsFactory)
                ),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                new PersonDetailsViewModelFactory(stringResources),
                new EventViewModelFactory(stringResources)
        );

        Toolbar toolbar = Views.findById(this, R.id.toolbar);
        if (wasCalledFromMemento()) {
            toolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_close_white);
        }
        setSupportActionBar(toolbar);
        setTitle(null);
        avatarView = Views.findById(this, R.id.person_avatar);
        personNameView = Views.findById(this, R.id.person_name);
        ageAndSignView = Views.findById(this, R.id.person_age_and_sign);
        viewPager = Views.findById(this, R.id.person_viewpager);

        adapter = new ContactItemsAdapter(LayoutInflater.from(thisActivity()), onEventPressed);
        imageLoader = UILImageLoader.createLoader(getResources()); // TODO inject this

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = Views.findById(this, R.id.person_tabs);
        tabLayout.setupWithViewPager(viewPager, false);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gift);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_call);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_message);

        displayingContact = extractContactFrom(getIntent());
        if (displayingContact.isPresent()) {
            presenter.startPresenting(displayingContact.get());
        } else {
            ErrorTracker.track(new IllegalArgumentException("No contact to display"));
            finish();
        }
    }

    private boolean wasCalledFromMemento() {
        Bundle extras = getIntent().getExtras();
        return extras != null && getIntent().getExtras().containsKey(EXTRA_CONTACT_ID);
    }

    private Optional<Contact> extractContactFrom(Intent intent) {
        long contactID = intent.getLongExtra(EXTRA_CONTACT_ID, -1);
        if (contactID == -1) {
            throw new IllegalArgumentException("Intent must contain a contact ID");
        }
        @ContactSource int contactSource = intent.getIntExtra(EXTRA_CONTACT_SOURCE, -1);
        if (contactSource == -1) {
            throw new IllegalArgumentException("Intent must contain a contact source");
        }
        try {
            return new Optional<>(ContactsProvider.get(this).getContact(contactID, contactSource));
        } catch (ContactNotFoundException e) {
            ErrorTracker.track(e);
        }
        return Optional.absent();
    }

    public static Intent buildIntentFor(Context context, Contact contact) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contact.getContactID());
        intent.putExtra(EXTRA_CONTACT_SOURCE, contact.getSource());
        return intent;
    }

    @Override
    public void displayPersonInfo(PersonInfoViewModel viewModel) {
        imageLoader.loadImage(viewModel.getImage(), avatarView);
        personNameView.setText(viewModel.getDisplayName());
        ageAndSignView.setText(viewModel.getAgeAndStarSignlabel());
    }

    @Override
    public void displayAvailableActions(PersonAvailableActionsViewModel viewModel) {
        adapter.displayEvents(viewModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (wasCalledFromMemento()) {
                    Intent intent = getParentActivityIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
                return true;
            case R.id.action_view_contact:
                if (displayingContact.isPresent()) {
                    navigator.toContactDetails(displayingContact.get());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }

    private EventPressedListener onEventPressed = new EventPressedListener() {
        @Override
        public void onContactActionPressed(ContactActionViewModel intent) {
            intent.getAction().run();
        }
    };

}
