package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.alexstyl.specialdates.Optional;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.net.URI;

public interface ImageLoader {
    void loadImage(URI imagePath, ImageView imageView);

    void loadImage(URI imagePath, ImageAware avatarView, OnImageLoadedCallback callback);

    void loadImage(URI imagePath, ImageView avatarView, OnImageLoadedCallback callback);

    void loadImage(URI imagePath, ImageSize targetImageSize, OnImageLoadedCallback callback);

    Optional<Bitmap> loadBitmapSync(URI imagePath, ImageSize imageSize);

    void resume();

    void pause();

}
