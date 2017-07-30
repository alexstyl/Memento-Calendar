package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;
import android.support.annotation.Px;
import android.view.View;
import android.widget.ImageView;

import com.alexstyl.specialdates.Optional;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.net.URI;

class UILImageLoader implements com.alexstyl.specialdates.images.ImageLoader {

    private final com.nostra13.universalimageloader.core.ImageLoader uil;

    UILImageLoader() {
        this.uil = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    }

    @Override
    public Request load(final URI imagePath) {
        final DisplayImageOptions.Builder builder = standardBuilder();
        return new Request() {

            @Override
            public void into(ImageView imageView) {
                uil.displayImage(imagePath.toString(), imageView, builder.build());
            }

            @Override
            public FixedSizeRequest withSize(@Px final int width, @Px final int height) {
                return new FixedSizeRequest() {
                    @Override
                    public Optional<Bitmap> async() {
                        Bitmap bitmap = uil.loadImageSync(imagePath.toString(), new ImageSize(width, height), builder.build());
                        if (bitmap == null) {
                            return Optional.absent();
                        } else {
                            return new Optional<>(bitmap);
                        }
                    }

                    @Override
                    public void into(final ImageLoadedConsumer consumer) {
                        uil.loadImage(imagePath.toString(), builder.build(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                consumer.onImageLoaded(loadedImage);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                consumer.onLoadingFailed();
                            }
                        });
                    }
                };
            }
        };
    }

    private DisplayImageOptions.Builder standardBuilder() {
        return new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(400, true, true, false))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                ;
    }

}
