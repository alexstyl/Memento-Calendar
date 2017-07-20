package com.alexstyl.specialdates.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
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
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        StringResources stringResources = new AndroidStringResources(getResources());// TODO inject this
        presenter = new PersonPresenter(
                this,
                PeopleEventsProvider.newInstance(thisActivity()),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                new PersonDetailsViewModelFactory(stringResources),
                new EventViewModelFactory(stringResources)
        );

        Toolbar toolbar = Views.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        avatarView = Views.findById(this, R.id.person_avatar);
        personNameView = Views.findById(this, R.id.person_name);
        ageAndSignView = Views.findById(this, R.id.person_age_and_sign);
        viewPager = Views.findById(this, R.id.person_viewpager);

        adapter = new ContactItemsAdapter(LayoutInflater.from(thisActivity()), new EventPressedListener() {
            @Override
            public void onEventPressed(Date date) {
                Log.d("onEventPressed: " + date);
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        imageLoader = UILImageLoader.createLoader(getResources()); // TODO inject this

        setTitle(null);

        Optional<Contact> contact = extractContactFrom(getIntent());
        if (contact.isPresent()) {
            presenter.startPresenting(contact.get());
        } else {
            ErrorTracker.track(new IllegalArgumentException("No contact to display"));
            finish();
        }
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
    public void displayInfoFor(PersonInfoViewModel viewModel) {
        imageLoader.loadImage(viewModel.getImage(), avatarView);
        personNameView.setText(viewModel.getDisplayName());
        ageAndSignView.setText(viewModel.getAgeAndStarSignlabel());
    }

    @Override
    public void displayContactMethods(PersonContactViewModel viewModel) {
        adapter.displayEvents(viewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }
}
