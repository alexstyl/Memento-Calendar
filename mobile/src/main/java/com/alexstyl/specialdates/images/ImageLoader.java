package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public interface ImageLoader {
    void loadImage(Uri imagePath, ImageView imageView);

    void loadImage(Uri imagePath, ImageAware avatarView, OnImageLoadedCallback callback);

    void loadImage(Uri imagePath, ImageSize targetImageSize, OnImageLoadedCallback callback);

    Bitmap loadBitmap(Uri imagePath, ImageSize imageSize);

    void resume();

    void pause();

}
