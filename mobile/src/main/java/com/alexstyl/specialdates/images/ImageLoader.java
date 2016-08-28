package com.alexstyl.specialdates.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.format.DateUtils;
import android.widget.ImageView;

import com.alexstyl.specialdates.BuildConfig;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageLoader {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final long FB_LIFE_TIME = DateUtils.DAY_IN_MILLIS * 3;


    public static void init(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(10)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new LimitedAgeDiskCache(StorageUtils.getCacheDirectory(context), FB_LIFE_TIME))
                .imageDecoder(new NutraBaseImageDecoder(BuildConfig.DEBUG))
                .imageDownloader(new ImageDownloader(context));

        L.writeLogs(DEBUG);
        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

    private final DisplayImageOptions displayImageOptions;
    private final com.nostra13.universalimageloader.core.ImageLoader loader;

    ImageLoader(DisplayImageOptions circleImageOptions) {
        this.displayImageOptions = circleImageOptions;
        loader = UniversalImageLoader.newInstance();
    }

    public static ImageLoader createWidgetThumbnailLoader(Resources resources) {
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

    public void loadThumbnail(String imagePath, ImageView view) {
        loader.displayImage(imagePath, view, displayImageOptions);
    }

    public Bitmap loadBitmap(String imagePath, int width, int height) {

        ImageSize imageSize = new ImageSize(width, height);
        return loader.loadImageSync(imagePath, imageSize);
    }

    public void loadBitmapAsync(String imagePath, int size, SimpleImageLoadingListener listener) {
        ImageSize imageSize = new ImageSize(size, size);
        loader.loadImage(imagePath, imageSize, displayImageOptions, listener);
    }

    public void resume() {
        loader.resume();
    }

    public void pause() {
        loader.pause();
    }
}
