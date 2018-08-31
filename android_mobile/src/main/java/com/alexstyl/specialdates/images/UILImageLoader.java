package com.alexstyl.specialdates.images;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.view.View;
import android.widget.ImageView;

import com.alexstyl.specialdates.Optional;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

class UILImageLoader implements com.alexstyl.specialdates.images.ImageLoader {

    private final com.nostra13.universalimageloader.core.ImageLoader uil;
    private final BitmapDisplayer defaultDisplayer;
    private final BitmapDisplayer defaultCircleDisplayer;

    UILImageLoader(CrossFadeBitmapDisplayer defaultDisplayer, CrossFadeCircleBitmapDisplayer circleDefaultDisplayer) {
        this.defaultDisplayer = defaultDisplayer;
        this.defaultCircleDisplayer = circleDefaultDisplayer;
        this.uil = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    }

    @NonNull
    @Override
    public Request load(@NonNull final String imagePath) {
        final DisplayImageOptions.Builder builder = standardBuilder();
        return new Request() {

            @Override
            public void into(@NonNull ImageView imageView) {
                uil.displayImage(imagePath, imageView, builder.build());
            }

            @NonNull
            @Override
            public Request asCircle() {
                builder.displayer(defaultCircleDisplayer);
                return this;
            }

            @NonNull
            @Override
            public FixedSizeRequest withSize(@Px final int width, @Px final int height) {
                builder.displayer(DefaultConfigurationFactory.createBitmapDisplayer());
                return new FixedSizeRequest() {
                    @NonNull
                    @Override
                    public Optional<Bitmap> synchronously() {
                        Bitmap bitmap = uil.loadImageSync(imagePath, new ImageSize(width, height), builder.build());
                        if (bitmap == null) {
                            return Optional.Companion.absent();
                        } else {
                            return new Optional<>(bitmap);
                        }
                    }

                    @Override
                    public void into(@NonNull final ImageLoadedConsumer consumer) {
                        builder.displayer(DefaultConfigurationFactory.createBitmapDisplayer());
                        uil.loadImage(imagePath, builder.build(), new SimpleImageLoadingListener() {
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
                .displayer(defaultDisplayer)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true);
    }

}
