package com.alexstyl.specialdates.person;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.images.ImageLoadedConsumer;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;

public class PersonActivity extends ThemedMementoActivity implements PersonView {

    private static final String EXTRA_CONTACT_SOURCE = "extra:source";
    private static final String EXTRA_CONTACT_ID = "extra:id";

    private PersonPresenter presenter;
    private ImageView avatarView;
    private TextView personNameView;
    private TextView ageAndSignView;
    private ContactItemsAdapter adapter;
    private ExternalNavigator navigator;
    private ImageView toolbarGradient;
    @Inject Analytics analytics;
    @Inject Strings strings;
    @Inject ImageLoader imageLoader;
    @Inject NamedayUserSettings namedayUserSettings;
    @Inject ContactsProvider contactsProvider;
    @Inject DateLabelCreator dateLabelCreator;

    private Optional<Contact> displayingContact = Optional.absent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);

        navigator = new ExternalNavigator(this, analytics);
        ContactActionsFactory actionsFactory = new AndroidContactActionsFactory(thisActivity());
        presenter = new PersonPresenter(
                this,
                PeopleEventsProvider.newInstance(this, namedayUserSettings, contactsProvider),
                new PersonCallProvider(
                        new AndroidContactActionsProvider(getContentResolver(), getResources(), thisActivity(), getPackageManager(), actionsFactory),
                        new FacebookContactActionsProvider(strings, getResources(), actionsFactory)
                ),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                new PersonDetailsViewModelFactory(strings, new AgeCalculator(Date.Companion.today())),
                new EventViewModelFactory(strings, dateLabelCreator)
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
        ViewPager viewPager = Views.findById(this, R.id.person_viewpager);
        toolbarGradient = Views.findById(this, R.id.person_toolbar_gradient);
        adapter = new ContactItemsAdapter(LayoutInflater.from(thisActivity()), onEventPressed);

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
        Uri data = intent.getData();
        if (data != null) {
            Long contactId = Long.valueOf(data.getLastPathSegment());
            return contactFor(contactId, SOURCE_DEVICE);
        }

        long contactID = intent.getLongExtra(EXTRA_CONTACT_ID, -1);
        if (contactID == -1) {
            return Optional.absent();
        }
        @ContactSource int contactSource = intent.getIntExtra(EXTRA_CONTACT_SOURCE, -1);
        //noinspection WrongConstant
        if (contactSource == -1) {
            return Optional.absent();
        }
        return contactFor(contactID, contactSource);
    }

    private Optional<Contact> contactFor(long contactID, int contactSource) {
        try {
            return new Optional<>(contactsProvider.getContact(contactID, contactSource));
        } catch (ContactNotFoundException e) {
            ErrorTracker.track(e);
            return Optional.absent();
        }
    }

    public static Intent buildIntentFor(Context context, Contact contact) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contact.getContactID());
        intent.putExtra(EXTRA_CONTACT_SOURCE, contact.getSource());
        return intent;
    }

    @Override
    public void displayPersonInfo(PersonInfoViewModel viewModel) {
        imageLoader.load(viewModel.getImage())
                .withSize(avatarView.getWidth(), avatarView.getHeight())
                .into(new ImageLoadedConsumer() {
                    private static final int ANIMATION_DURATION = 400;

                    @Override
                    public void onImageLoaded(Bitmap loadedImage) {
                        new FadeInBitmapDisplayer(ANIMATION_DURATION).display(loadedImage, new ImageViewAware(avatarView), LoadedFrom.DISC_CACHE);
                        Drawable[] layers = new Drawable[2];
                        layers[0] = new ColorDrawable(getResources().getColor(android.R.color.transparent));
                        layers[1] = getResources().getDrawable(R.drawable.black_to_transparent_gradient_facing_down);
                        TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
                        toolbarGradient.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(ANIMATION_DURATION);
                        toolbarGradient.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed() {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = new ColorDrawable(getResources().getColor(android.R.color.transparent));
                        layers[1] = getResources().getDrawable(R.drawable.ic_person_96dp);
                        TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
                        avatarView.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(ANIMATION_DURATION);
                        toolbarGradient.setVisibility(View.GONE);
                    }
                });

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
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    finish();
                }
                return true;
            case R.id.action_view_contact:
                if (displayingContact.isPresent()) {
                    navigator.toContactDetails(displayingContact.get());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }

    private EventPressedListener onEventPressed = new EventPressedListener() {
        @Override
        public void onContactActionPressed(ContactActionViewModel intent) {
            try {
                intent.getAction().run();
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(thisActivity(), R.string.no_app_found, Toast.LENGTH_SHORT).show();
                ErrorTracker.track(ex);
            }
        }
    };

}
