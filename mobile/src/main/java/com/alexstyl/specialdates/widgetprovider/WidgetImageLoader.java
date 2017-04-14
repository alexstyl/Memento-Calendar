package com.alexstyl.specialdates.widgetprovider;

import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.support.annotation.Px;
import android.widget.RemoteViews;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.OnImageLoadedCallback;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

class WidgetImageLoader {

    private final AppWidgetManager appWidgetManager;
    private final ImageLoader imageLoader;

    WidgetImageLoader(AppWidgetManager appWidgetManager, ImageLoader imageLoader) {
        this.appWidgetManager = appWidgetManager;
        this.imageLoader = imageLoader;
    }

    void loadPicture(List<Contact> contacts, final int appWidgetId, final RemoteViews views, @Px int size) {
        tryToFetchImageFor(contacts, 0, appWidgetId, views, size);
    }

    private void tryToFetchImageFor(final List<Contact> contacts,
                                    final int contactIndex,
                                    final int appWidgetId,
                                    final RemoteViews views,
                                    @Px final int size) {
        imageLoader.loadImage(contacts.get(contactIndex).getImagePath(), new ImageSize(size, size), new OnImageLoadedCallback() {

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
