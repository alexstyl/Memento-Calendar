package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.widget.RemoteViews;

import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.upcoming.BankHolidayViewModel;
import com.alexstyl.specialdates.upcoming.ContactEventViewModel;
import com.alexstyl.specialdates.upcoming.NamedaysViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingEventsViewModel;
import com.alexstyl.specialdates.widgetprovider.WidgetImageLoader;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UpcomingEventsBinder implements UpcomingEventViewBinder<UpcomingEventsViewModel> {

    private final RemoteViews remoteViews;
    private final WidgetImageLoader imageLoader;
    private final DimensionResources dimensResources;
    private final Context context;

    public static UpcomingEventViewBinder buildFor(String packageName,
                                                   WidgetImageLoader imageLoader,
                                                   DimensionResources dimensResources,
                                                   Context context
    ) {
        RemoteViews view = new RemoteViews(packageName, R.layout.row_widget_upcoming_event);
        return new UpcomingEventsBinder(view, imageLoader, dimensResources, context);
    }

    private UpcomingEventsBinder(RemoteViews remoteViews, WidgetImageLoader imageLoader, DimensionResources dimensResources, Context context) {
        this.remoteViews = remoteViews;
        this.imageLoader = imageLoader;
        this.dimensResources = dimensResources;
        this.context = context;
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
        Intent startIntent = DateDetailsActivity.getStartIntent(context, date);
        startIntent.setData(Uri.parse(String.valueOf(date.hashCode())));
        return startIntent;
    }

    private void bind(BankHolidayViewModel viewModel) {
        remoteViews.setTextViewText(R.id.row_widget_upcoming_event_bankholidays, viewModel.getBankHolidayName());
        remoteViews.setViewVisibility(R.id.row_widget_upcoming_event_bankholidays, viewModel.getBankHolidaysVisibility());
    }

    private void bind(NamedaysViewModel viewModel) {
        remoteViews.setTextViewText(R.id.row_widget_upcoming_event_namedays, viewModel.getNamesLabel());
        remoteViews.setViewVisibility(R.id.row_widget_upcoming_event_namedays, viewModel.getNamedaysVisibility());
    }

    private void bind(List<ContactEventViewModel> viewModels) {
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

    private void bindContact(ContactEventViewModel contactEventViewModel,
                             @IdRes int contactNameViewId,
                             @IdRes int eventTypeViewId,
                             @IdRes int avatarViewId) {
        remoteViews.setTextViewText(contactNameViewId, contactEventViewModel.getContactName());
        remoteViews.setTextViewText(eventTypeViewId, contactEventViewModel.getEventLabel());
        remoteViews.setTextColor(eventTypeViewId, contactEventViewModel.getEventColor());
        imageLoader.loadPicture(
                contactEventViewModel.getContactImagePath(),
                avatarViewId,
                remoteViews,
                dimensResources.getPixelSize(R.dimen.avatar_size)
        );
    }

    @Override
    public RemoteViews getViews() {
        return remoteViews;
    }
}
