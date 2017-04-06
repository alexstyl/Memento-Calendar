package com.alexstyl.specialdates.widgetprovider;

import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RemoteViews;

import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

public class WidgetImageLoader {

    private final AppWidgetManager appWidgetManager;
    private final int avatarSize;
    private ImageLoader imageLoader;

    public static WidgetImageLoader newInstance(DimensionResources resources, AppWidgetManager appWidgetManager, ImageLoader imageLoader) {
        int size = resources.getPixelSize(R.dimen.widget_avatar_size);
        return new WidgetImageLoader(appWidgetManager, imageLoader, size);
    }

    private WidgetImageLoader(AppWidgetManager appWidgetManager, ImageLoader imageLoader, int avatarSize) {
        this.appWidgetManager = appWidgetManager;
        this.imageLoader = imageLoader;
        this.avatarSize = avatarSize;
    }

    public void loadPicture(List<Contact> contacts, final int appWidgetId, final RemoteViews views) {
        tryToFetchImageFor(contacts, 0, appWidgetId, views);
    }

    private void tryToFetchImageFor(final List<Contact> contacts, final int contactIndex, final int appWidgetId, final RemoteViews views) {
        imageLoader.loadBitmapAsync(
                contacts.get(contactIndex).getImagePath(), avatarSize, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        handleImageNotLoaded();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (loadedImage == null) {
                            handleImageNotLoaded();
                        } else {
                            views.setImageViewBitmap(R.id.widget_avatar, loadedImage);
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    }

                    private void handleImageNotLoaded() {
                        int contactSize = contacts.size();
                        if (contactIndex + 1 < contactSize) {
                            tryToFetchImageFor(contacts, contactIndex + 1, appWidgetId, views);
                        } else {
                            // no more pictures to load
                            views.setImageViewResource(R.id.widget_avatar, R.drawable.ic_contact_picture);
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    }
                }
        );
    }

    public void loadPicture(Uri imagePath, @IdRes final int appWidgetId, final RemoteViews views, final @IdRes int targetViewId) {
        imageLoader.loadBitmapAsync(
                imagePath,
                avatarSize,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        views.setImageViewBitmap(targetViewId, null);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        views.setImageViewBitmap(targetViewId, loadedImage);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                }
        );
    }
}
