package com.alexstyl.specialdates.addevent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;

import com.alexstyl.android.AndroidDateLabelCreator;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;

public class AddEventActivity extends ThemedActivity {

    private EventsListPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_from_below, R.anim.stay);
        setContentView(R.layout.activity_add_event);

        // TODO analytics
        // TODO black and white icons for X
        ImageLoader imageLoader = ImageLoader.createSquareThumbnailLoader(getResources());

        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        ImageView avatarView = Views.findById(this, R.id.add_event_avatar);
        ContactSuggestionView contactSuggestionView = Views.findById(this, R.id.add_event_contact_autocomplete);
        RecyclerView eventsView = Views.findById(this, R.id.add_event_events);
        eventsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsView.setHasFixedSize(true);
        ContactEventsAdapter adapter = new ContactEventsAdapter(onEventTappedListener);
        eventsView.setAdapter(adapter);

        PeopleEventsProvider peopleEventsProvider = PeopleEventsProvider.newInstance(this);
        ContactEventViewModelFactory factory = new ContactEventViewModelFactory(new AndroidDateLabelCreator(this));
        AddEventViewModelFactory newEventFactory = new AddEventViewModelFactory(new AndroidStringResources(getResources()));
        ContactEventsFetcher contactEventsFetcher = new ContactEventsFetcher(
                getSupportLoaderManager(),
                this,
                peopleEventsProvider,
                factory,
                newEventFactory
        );

        presenter = new EventsListPresenter(
                imageLoader,
                contactEventsFetcher,
                adapter,
                contactSuggestionView,
                avatarView,
                toolbar
        );
        presenter.startPresenting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cancelActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        overridePendingTransition(0, R.anim.slide_out_from_below);
        super.finish();
    }

    private void cancelActivity() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private final OnEventTappedListener onEventTappedListener = new OnEventTappedListener() {
        @Override
        public void onEventTapped(ContactEventViewModel viewModel) {
            final EventType eventType = viewModel.getEventType();
            final Optional<Date> initialDate = viewModel.getDate();
            EventDatePickerDialogFragment dialog = EventDatePickerDialogFragment.newInstance(eventType, initialDate, new EventDatePickerDialogFragment.OnBirthdaySelectedListener() {
                @Override
                public void onDatePicked(Date date) {
                    if (hasSelectedNewDate(date)) {
                        presenter.onEventDatePicked(eventType, date);
                    }
                }

                private boolean hasSelectedNewDate(Date date) {
                    return !initialDate.isPresent() || !initialDate.get().equals(date);
                }
            });
            dialog.show(getSupportFragmentManager(), "pick_event");
        }
    };
}
