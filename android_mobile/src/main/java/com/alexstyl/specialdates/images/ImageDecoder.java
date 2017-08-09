package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;

import com.novoda.notils.logger.simple.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ImageDecoder {

    private static final int FULL_QUALITY = 100;
    private static final int SIZE_MULTIPLIER = 4;

    public DecodedImage decodeFrom(Bitmap bitmap) {
        int size = bitmap.getWidth() * bitmap.getHeight() * SIZE_MULTIPLIER;
        ByteArrayOutputStream stream = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, FULL_QUALITY, stream);
            stream.flush();
            stream.close();
            byte[] bytes = stream.toByteArray();
            return new DecodedImage(bytes);
        } catch (IOException e) {
            Log.w("Unable to serialize photo: " + e.toString());
            return null;
        }
    }

}
