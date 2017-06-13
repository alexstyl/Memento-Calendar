package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;

import com.novoda.notils.logger.simple.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

final public class ImageDecoder {

    public DecodedImage decodeFrom(Bitmap bitmap) {
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream stream = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
