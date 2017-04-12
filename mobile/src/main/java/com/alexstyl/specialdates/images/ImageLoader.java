package com.alexstyl.specialdates.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.alexstyl.specialdates.BuildConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;

public class ImageLoader {

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void init(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(10)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDecoder(new NutraBaseImageDecoder())
                .imageDownloader(new ImageDownloader(context));

        L.writeLogs(DEBUG);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

    private final DisplayImageOptions displayImageOptions;
    private final com.nostra13.universalimageloader.core.ImageLoader loader;

    private ImageLoader(DisplayImageOptions circleImageOptions) {
        this.displayImageOptions = circleImageOptions;
        loader = UniversalImageLoader.newInstance();
    }

    public static ImageLoader createWidgetThumbnailLoader() {
        DisplayImageOptions options = UniversalImageLoader.buildWidgetImageOptions();
        return new ImageLoader(options);
    }

    public static ImageLoader createCircleThumbnailLoader(Resources resources) {
        DisplayImageOptions options = UniversalImageLoader.buildCircleImageOptions(resources);
        return new ImageLoader(options);
    }

    public static ImageLoader createSquareThumbnailLoader(Resources resources) {
        DisplayImageOptions options = UniversalImageLoader.buildSquareImageOptions(resources);
        return new ImageLoader(options);
    }

    public void displayThumbnail(Uri imagePath, ImageView imageView) {
        loader.displayImage(imagePath.toString(), imageView, displayImageOptions);
    }

    public void loadImage(Uri imagePath, ImageAware avatarView, final OnImageLoadedCallback callback) {
        loader.loadImage(imagePath.toString(), new ImageSize(avatarView.getWidth(), avatarView.getHeight()), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                callback.onImageLoaded(loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                callback.onLoadingFailed();
            }
        });
    }

    public Bitmap loadBitmap(Uri imagePath, int width, int height) {
        ImageSize imageSize = new ImageSize(width, height);
        return loader.loadImageSync(imagePath.toString(), imageSize);
    }

    public void loadBitmapAsync(Uri imagePath, int size, SimpleImageLoadingListener listener) {
        ImageSize imageSize = new ImageSize(size, size);
        loader.loadImage(imagePath.toString(), imageSize, displayImageOptions, listener);
    }

    void resume() {
        loader.resume();
    }

    void pause() {
        loader.pause();
    }

}
