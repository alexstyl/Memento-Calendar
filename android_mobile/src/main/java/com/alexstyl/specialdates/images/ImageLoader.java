package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;
import android.support.annotation.Px;
import android.widget.ImageView;

import com.alexstyl.specialdates.Optional;

import java.net.URI;

public interface ImageLoader {

    Request load(URI imagePath);

    interface Request {

        FixedSizeRequest withSize(@Px int width, @Px int height);

        void into(ImageView imageView);

        Request asCircle();
    }

    interface FixedSizeRequest {
        Optional<Bitmap> synchronously();

        void into(ImageLoadedConsumer consumer);
    }

}
