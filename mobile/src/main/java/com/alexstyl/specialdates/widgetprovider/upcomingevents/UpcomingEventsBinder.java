package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.Px;
import android.widget.RemoteViews;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.BankHolidayViewModel;
import com.alexstyl.specialdates.upcoming.ContactEventViewModel;
import com.alexstyl.specialdates.upcoming.NamedaysViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingEventsViewModel;
import com.alexstyl.specialdates.widgetprovider.AppWidgetId;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

import static android.graphics.Shader.TileMode.CLAMP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UpcomingEventsBinder implements UpcomingEventViewBinder<UpcomingEventsViewModel> {

    private final RemoteViews remoteViews;
    private final ImageLoader imageLoader;
    private final DimensionResources dimensResources;
    private final Context context;
    @AppWidgetId
    private final int appWidgetId;
    private final AppWidgetManager appWidgetManager;
    private final ColorResources colorResources;

    public UpcomingEventsBinder(RemoteViews remoteViews,
                                ImageLoader imageLoader,
                                DimensionResources dimensResources,
                                Context context,
                                @AppWidgetId int appWidgetId,
                                AppWidgetManager appWidgetManager,
                                ColorResources colorResources) {
        this.remoteViews = remoteViews;
        this.imageLoader = imageLoader;
        this.dimensResources = dimensResources;
        this.context = context;
        this.appWidgetId = appWidgetId;
        this.appWidgetManager = appWidgetManager;
        this.colorResources = colorResources;
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

    private void bindContact(final ContactEventViewModel contactEventViewModel,
                             @IdRes int contactNameViewId,
                             @IdRes int eventTypeViewId,
                             @IdRes final int avatarViewId) {
        remoteViews.setTextViewText(contactNameViewId, contactEventViewModel.getContactName());
        remoteViews.setTextViewText(eventTypeViewId, contactEventViewModel.getEventLabel());
        remoteViews.setTextColor(eventTypeViewId, contactEventViewModel.getEventColor());
        Bitmap image = createAvatarFor(contactEventViewModel.getContact());
        remoteViews.setImageViewBitmap(avatarViewId, image);
    }

    private Bitmap createAvatarFor(Contact contact) {
        @Px int targetSize = dimensResources.getPixelSize(R.dimen.widget_upcoming_avatar_size);
        Optional<Bitmap> avatarBitmap = imageLoader.loadBitmap(contact.getImagePath(), new ImageSize(targetSize, targetSize));
        if (avatarBitmap.isPresent()) {
            return circularAvatarFrom(avatarBitmap.get(), targetSize);
        } else {
            return createLetterAvatarFor(contact, targetSize);
        }
    }

    private Bitmap circularAvatarFrom(Bitmap avatar, @Px int viewSize) {
        Bitmap drawingBitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(drawingBitmap);
        Paint paint = createPaintFrom(avatar);

        float radius = (viewSize / 2f);
        canvas.drawCircle(radius, radius, radius, paint);
        return drawingBitmap;
    }

    private Paint createPaintFrom(Bitmap avatar) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader shader = new BitmapShader(avatar, CLAMP, CLAMP);
        paint.setShader(shader);
        return paint;
    }

    private Bitmap createLetterAvatarFor(Contact contact, @Px int viewSize) {
        Bitmap drawingBitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(drawingBitmap);
        Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float radius = (viewSize / 2f);
        backgroundPaint.setColor(colorResources.getColor(R.color.teal));
        canvas.drawCircle(radius, radius, radius, backgroundPaint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(dimensResources.getPixelSize(R.dimen.widget_upcoming_avatar_size));
        canvas.drawText(firstLetterOf(contact.getDisplayName()), radius, radius, textPaint);

        return drawingBitmap;
    }

    private String firstLetterOf(DisplayName displayName) {
        String rawDisplayName = displayName.toString();
        if (rawDisplayName.length() == 0) {
            return ":)";
        }
        return rawDisplayName.substring(0, 1);
    }

    @Override
    public RemoteViews getViews() {
        return remoteViews;
    }
}
