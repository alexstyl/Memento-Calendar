package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;

public interface OnImageLoadedCallback {

    void onImageLoaded(Bitmap loadedImage);

    void onLoadingFailed();
}
