package com.alexstyl.specialdates.addevent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alexstyl.android.AndroidDateLabelCreator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;

public class AddEventActivity extends ThemedActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);

        // TODO analytics
        ImageLoader imageLoader = ImageLoader.createSquareThumbnailLoader(getResources());

        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.displayAsUp();

        ColorImageView avatarView = Views.findById(this, R.id.add_event_avatar);
        ContactSuggestionView contactSuggestionView = Views.findById(this, R.id.add_event_contact_autocomplete);
        RecyclerView eventsView = Views.findById(this, R.id.add_event_events);
        eventsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsView.setHasFixedSize(true);
        ContactEventsAdapter adapter = new ContactEventsAdapter();
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
        EventsListPresenter presenter = new EventsListPresenter(imageLoader, contactEventsFetcher, adapter, contactSuggestionView, avatarView);
        presenter.startPresenting();
    }

}
