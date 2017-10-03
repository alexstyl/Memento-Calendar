package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;

public interface ImageLoadedConsumer {
    void onImageLoaded(Bitmap loadedImage);
    void onLoadingFailed();
}
