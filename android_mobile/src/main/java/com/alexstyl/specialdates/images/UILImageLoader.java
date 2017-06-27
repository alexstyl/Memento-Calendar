package com.alexstyl.specialdates.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.alexstyl.specialdates.Optional;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import static com.alexstyl.specialdates.Optional.absent;

public class UILImageLoader implements ImageLoader {

    private final DisplayImageOptions displayImageOptions;
    private final com.nostra13.universalimageloader.core.ImageLoader uil;

    public static ImageLoader createLoader(Resources resources) {
        return createLoader(new CustomFadeInDisplayer(resources));
    }

    public static UILImageLoader createCircleLoader(Resources resources) {
        BitmapDisplayer displayer = new FadeInRoundedBitmapDisplayer(resources);
        return createLoader(displayer);
    }

    private static UILImageLoader createLoader(BitmapDisplayer displayer) {
        return new UILImageLoader(new DisplayImageOptions.Builder()
                                          .displayer(displayer)
                                          .showImageOnLoading(android.R.color.transparent)
                                          .cacheInMemory(true)
                                          .cacheOnDisk(false)
                                          .build());
    }

    private UILImageLoader(DisplayImageOptions circleImageOptions) {
        this.displayImageOptions = circleImageOptions;
        this.uil = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    }

    @Override
    public void loadImage(Uri imagePath, ImageView imageView) {
        uil.displayImage(imagePath.toString(), imageView, displayImageOptions);
    }

    @Override
    public void loadImage(Uri imagePath, ImageAware imageAware, final OnImageLoadedCallback callback) {
        loadImage(imagePath, new ImageSize(imageAware.getWidth(), imageAware.getHeight()), callback);
    }

    @Override
    public void loadImage(Uri imagePath, ImageView avatarView, OnImageLoadedCallback callback) {
        loadImage(imagePath, new ImageViewAware(avatarView), callback);
    }

    @Override
    public void loadImage(Uri imagePath, ImageSize targetImageSize, final OnImageLoadedCallback callback) {
        uil.loadImage(imagePath.toString(), targetImageSize, new SimpleImageLoadingListener() {
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

    @Override
    public Optional<Bitmap> loadBitmapSync(Uri imagePath, ImageSize imageSize) {
        Bitmap bitmap = uil.loadImageSync(imagePath.toString(), imageSize);
        if (bitmap == null) {
            return absent();
        } else {
            return new Optional<>(bitmap);
        }
    }

    @Override
    public void resume() {
        uil.resume();
    }

    @Override
    public void pause() {
        uil.pause();
    }
}
