package com.alexstyl.specialdates.upcoming.widget.today;

import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.support.annotation.Px;
import android.widget.RemoteViews;

import com.alexstyl.android.widget.AppWidgetId;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.SimpleImageLoadedConsumer;

import java.util.List;

public class WidgetImageLoader {

    private final AppWidgetManager appWidgetManager;
    private final ImageLoader imageLoader;

    public WidgetImageLoader(AppWidgetManager appWidgetManager, ImageLoader imageLoader) {
        this.appWidgetManager = appWidgetManager;
        this.imageLoader = imageLoader;
    }

    void loadPicture(List<Contact> contacts, final int appWidgetId, final RemoteViews views, @Px int size) {
        tryToFetchImageFor(contacts, 0, appWidgetId, views, size);
    }

    private void tryToFetchImageFor(final List<Contact> contacts,
                                    final int contactIndex,
                                    @AppWidgetId final int appWidgetId,
                                    final RemoteViews views,
                                    @Px final int size) {
        if (contacts.size() == 0) {
            return; //TODO temp fix. Don't merge this
        }
        imageLoader
                .load(contacts.get(contactIndex).getImagePath())
                .withSize(size, size)
                .into(new SimpleImageLoadedConsumer() {

                    @Override
                    public void onLoadingFailed() {
                        handleImageNotLoaded();
                    }

                    private void handleImageNotLoaded() {
                        int contactSize = contacts.size();
                        if (contactIndex + 1 < contactSize) {
                            tryToFetchImageFor(contacts, contactIndex + 1, appWidgetId, views, size);
                        } else {
                            // no more pictures to load
                            views.setImageViewResource(R.id.widget_avatar, R.drawable.ic_contact_picture);
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    }

                    @Override
                    public void onImageLoaded(Bitmap loadedImage) {
                        if (loadedImage == null) {
                            handleImageNotLoaded();
                        } else {
                            views.setImageViewBitmap(R.id.widget_avatar, loadedImage);
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    }
                });
    }
}
