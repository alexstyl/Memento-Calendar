package com.alexstyl.specialdates.upcoming.widget.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.Px;
import android.widget.RemoteViews;

import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.upcoming.BankHolidayViewModel;
import com.alexstyl.specialdates.upcoming.NamedaysViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingContactEventViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;
import com.alexstyl.specialdates.upcoming.UpcomingEventsViewModel;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UpcomingEventsBinder implements UpcomingEventViewBinder<UpcomingEventsViewModel> {

    private final RemoteViews remoteViews;
    private final CircularAvatarFactory avatarFactory;
    private final DimensionResources dimenResources;

    private final Context context;

    public UpcomingEventsBinder(RemoteViews remoteViews,
                                Context context,
                                CircularAvatarFactory avatarFactory,
                                DimensionResources dimensionResources) {
        this.remoteViews = remoteViews;
        this.context = context;
        this.avatarFactory = avatarFactory;
        this.dimenResources = dimensionResources;
    }

    @Override
    public void bind(UpcomingEventsViewModel viewModel) {
        Intent startIntent = buildDateDetailsIntentFor(viewModel.getDate());

        remoteViews.setOnClickFillInIntent(R.id.upcoming_events_background, startIntent);
        remoteViews.setTextViewText(R.id.row_widget_upcoming_event_date, viewModel.getDisplayDateLabel());

        bind(viewModel.getBankHolidayViewModel());
        bind(viewModel.getNamedaysViewModel());

        bind(viewModel.getContactViewModels());

        remoteViews.setViewVisibility(R.id.row_widget_upcoming_event_more, viewModel.getMoreButtonVisibility());
        remoteViews.setTextViewText(R.id.row_widget_upcoming_event_more, viewModel.getMoreButtonLabe());
    }

    private Intent buildDateDetailsIntentFor(Date date) {
        Intent startIntent = UpcomingEventsActivity.getStartIntent(context, date);
        startIntent.setData(Uri.parse(String.valueOf(date.hashCode())));
        return startIntent;
    }

    private void bind(BankHolidayViewModel viewModel) {
        remoteViews.setTextViewText(R.id.row_widget_upcoming_event_bankholidays, viewModel.getBankHolidayName());
    }

    private void bind(NamedaysViewModel viewModel) {
        remoteViews.setTextViewText(R.id.row_widget_upcoming_event_namedays, viewModel.getNamesLabel());
    }

    private void bind(List<UpcomingContactEventViewModel> viewModels) {
        if (viewModels.size() >= 1) {
            bindContact(viewModels.get(0), R.id.row_widget_upcoming_event_contact_name_1, R.id.row_widget_upcoming_event_contact_event_type_1, R.id.widget_upcoming_events_avatar_1);
            remoteViews.setViewVisibility(R.id.row_widget_upcoming_contact_1, VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.row_widget_upcoming_contact_1, GONE);
        }

        if (viewModels.size() >= 2) {
            bindContact(viewModels.get(1), R.id.row_widget_upcoming_event_contact_name_2, R.id.row_widget_upcoming_event_contact_event_type_2, R.id.widget_upcoming_events_avatar_2);
            remoteViews.setViewVisibility(R.id.row_widget_upcoming_contact_2, VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.row_widget_upcoming_contact_2, GONE);
        }
    }

    private void bindContact(final UpcomingContactEventViewModel upcomingContactEventViewModel,
                             @IdRes int contactNameViewId,
                             @IdRes int eventTypeViewId,
                             @IdRes final int avatarViewId) {
        remoteViews.setTextViewText(contactNameViewId, upcomingContactEventViewModel.getContactName());
        remoteViews.setTextViewText(eventTypeViewId, upcomingContactEventViewModel.getEventLabel());
        remoteViews.setTextColor(eventTypeViewId, upcomingContactEventViewModel.getEventColor());
        Bitmap image = createAvatarFor(upcomingContactEventViewModel.getContact());
        remoteViews.setImageViewBitmap(avatarViewId, image);
    }

    private Bitmap createAvatarFor(Contact contact) {
        @Px int targetSize = dimenResources.getPixelSize(R.dimen.widget_upcoming_avatar_size);

        Optional<Bitmap> avatarBitmap = avatarFactory.circularAvatarFor(contact, targetSize);
        if (avatarBitmap.isPresent()) {
            return avatarBitmap.get();
        } else {
            int textSize = dimenResources.getPixelSize(R.dimen.widget_upcoming_avatar_text_size);
            return avatarFactory.createLetterAvatarFor(contact.getDisplayName(), targetSize, textSize);
        }
    }

    @Override
    public RemoteViews getViews() {
        return remoteViews;
    }
}
