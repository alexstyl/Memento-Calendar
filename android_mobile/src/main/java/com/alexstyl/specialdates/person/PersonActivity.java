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
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;
import com.alexstyl.specialdates.images.ImageLoadedConsumer;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.HideStatusBarListener;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;

public class PersonActivity extends ThemedMementoActivity implements PersonView, BottomSheetIntentListener {

    private static final String EXTRA_CONTACT_SOURCE = "extra:source";
    private static final String EXTRA_CONTACT_ID = "extra:id";

    @Inject Analytics analytics;
    @Inject Strings strings;
    @Inject ImageLoader imageLoader;
    @Inject NamedayUserSettings namedayUserSettings;
    @Inject ContactsProvider contactsProvider;
    @Inject DateLabelCreator dateLabelCreator;
    @Inject PeopleEventsProvider peopleEventsProvider;
    @Inject PeopleEventsPersister peoplePersister;
    @Inject CrashAndErrorTracker tracker;

    private static final int ID_TOGGLE_VISIBILITY = 1023;
    
    private AppBarLayout appBarLayout;
    private ImageView toolbarGradient;
    private ImageView avatarView;
    private TextView personNameView;
    private TextView ageAndSignView;
    private TabLayout tabLayout;

    private Optional<Contact> displayingContact = Optional.absent();
    private PersonPresenter presenter;
    private PersonDetailsNavigator navigator;
    private ContactItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        appBarLayout = findViewById(R.id.person_appbar);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);
        analytics.trackScreen(Screen.PERSON);
        navigator = new PersonDetailsNavigator(new ExternalNavigator(this, analytics, tracker));
        ContactActionsFactory actionsFactory = new AndroidContactActionsFactory(thisActivity());
        presenter = new PersonPresenter(
                this,
                peopleEventsProvider,
                new PersonCallProvider(
                        new AndroidContactActionsProvider(
                                getContentResolver(), getResources(), thisActivity(), getPackageManager(), actionsFactory, tracker
                        ),
                        new FacebookContactActionsProvider(strings, getResources(), actionsFactory)
                ),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                new PersonDetailsViewModelFactory(strings, new AgeCalculator(Date.Companion.today())),
                new EventViewModelFactory(strings, dateLabelCreator),
                peoplePersister
        );

        MementoToolbar toolbar = Views.findById(this, R.id.person_toolbar);
        if (wasCalledFromMemento()) {
            toolbar.displayNavigationIconAsUp();
        } else {
            toolbar.displayNavigationIconAsClose();
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

        tabLayout = Views.findById(this, R.id.person_tabs);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        displayingContact = extractContactFrom(getIntent());
        if (displayingContact.isPresent()) {
            presenter.startPresenting(displayingContact.get());
        } else {
            tracker.track(new IllegalArgumentException("No contact to display"));
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
            tracker.track(e);
            return Optional.absent();
        }
    }

    @Override
    public void displayPersonInfo(PersonInfoViewModel viewModel) {
        imageLoader.load(viewModel.getImage())
                .withSize(avatarView.getWidth(), avatarView.getHeight())
                .into(new ImageLoadedConsumer() {
                    private static final int ANIMATION_DURATION = 400;

                    @Override
                    public void onImageLoaded(Bitmap loadedImage) {
                        if (Version.hasLollipop()) {
                            appBarLayout.addOnOffsetChangedListener(new HideStatusBarListener(getWindow()));
                        }
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
        ageAndSignView.setVisibility(viewModel.getAgeAndStarSignVisibility());
        // TODO coming up
        //        if (viewModel.isVisible()) {
        //            showPersonAsVisible();
        //        } else {
        //            showPersonAsHidden();
        //        }
    }

    @Override
    public void displayAvailableActions(PersonAvailableActionsViewModel viewModel) {
        adapter.displayEvents(viewModel);

        updateTabIfNeeded(0, R.drawable.ic_gift);
        updateTabIfNeeded(1, R.drawable.ic_call);
        updateTabIfNeeded(2, R.drawable.ic_message);

        if (tabLayout.getTabCount() <= 1) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateTabIfNeeded(int index, @DrawableRes int iconResId) {
        if (tabLayout.getTabAt(index) != null) {
            tabLayout.getTabAt(index).setIcon(getTintedDrawable(iconResId));
        }
    }

    @Override
    public void showPersonAsVisible() {
        throw new UnsupportedOperationException("Visibility is not currently available");
//        isVisibleContactOptional = new Optional<>(true);
//        avatarView.setColorFilter(Color.TRANSPARENT);
//        invalidateOptionsMenu();
    }

    @Override
    public void showPersonAsHidden() {
        throw new UnsupportedOperationException("Visibility is not currently available");
//        isVisibleContactOptional = new Optional<>(false);
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);
//
//        avatarView.setColorFilter(new ColorMatrixColorFilter(matrix));
//        invalidateOptionsMenu();
    }

    private Optional<Boolean> isVisibleContactOptional = Optional.absent();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person_details, menu);
        // TODO coming up in a follow up PR
//        if (isVisibleContactOptional.isPresent()) {
//            if (isVisibleContactOptional.get()) {
//                menu.add(0, ID_TOGGLE_VISIBILITY, 0, R.string.person_hide);
//            } else {
//                menu.add(0, ID_TOGGLE_VISIBILITY, 0, R.string.person_show);
//            }
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home && !wasCalledFromMemento()) {
            finish();
            return true;
        } else if (itemId == R.id.menu_view_contact) {
            navigator.toViewContact(displayingContact);
        } else if (itemId == ID_TOGGLE_VISIBILITY) {
            Boolean isVisible = isVisibleContactOptional.get();
            if (isVisible) {
                presenter.hideContact();
            } else {
                presenter.showContact();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopPresenting();
    }

    private EventPressedListener onEventPressed = new EventPressedListener() {
        @Override
        public void onContactActionPressed(ContactActionViewModel intent) {
            try {
                intent.getAction().run();
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(thisActivity(), R.string.no_app_found, Toast.LENGTH_SHORT).show();
                tracker.track(ex);
            }
        }
    };

    public static Intent buildIntentFor(Context context, Contact contact) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contact.getContactID());
        intent.putExtra(EXTRA_CONTACT_SOURCE, contact.getSource());
        return intent;
    }

    @Override
    public void onActivitySelected(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show();
        }
    }
}
