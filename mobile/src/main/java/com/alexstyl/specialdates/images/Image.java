package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.ByteArrayOutputStream;

public final class Image {

    private final Uri imagePath;

    public Image(Uri imagePath) {
        this.imagePath = imagePath;
    }

    public Uri getUri() {
        return imagePath;
    }

    public byte[] asByteArray(ImageLoader imageLoader) {
        Bitmap bitmap = imageLoader.loadBitmap(imagePath, 500, 500);
        return toByteArray(bitmap);
    }

    private byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

}
