package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Px;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import static android.graphics.Shader.TileMode.CLAMP;

public final class CircularAvatarFactory {

    private static final String SMILEY_FACE = ":)";
    private static final Typeface ROBOTO_LIGHT = Typeface.create("sans-serif-light", Typeface.NORMAL);

    private final ImageLoader imageLoader;
    private final ColorResources colorResources;

    public CircularAvatarFactory(ImageLoader imageLoader, ColorResources colorResources) {
        this.imageLoader = imageLoader;
        this.colorResources = colorResources;
    }

    Optional<Bitmap> circularAvatarFor(Contact contact, @Px int targetSize) {
        Optional<Bitmap> bitmapOptional = imageLoader.loadBitmap(contact.getImagePath(), new ImageSize(targetSize, targetSize));
        if (!bitmapOptional.isPresent()) {
            return Optional.absent();
        }

        Bitmap avatar = bitmapOptional.get();
        Bitmap circleBitmap = Bitmap.createBitmap(avatar.getWidth(), avatar.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        Paint paint = createPaintFrom(avatar);

        canvas.drawCircle(
                circleBitmap.getWidth() / 2,
                circleBitmap.getHeight() / 2,
                circleBitmap.getWidth() / 2f,
                paint
        );
        return new Optional<>(circleBitmap);
    }

    private Paint createPaintFrom(Bitmap avatar) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader shader = new BitmapShader(avatar, CLAMP, CLAMP);
        paint.setShader(shader);
        return paint;
    }

    Bitmap createLetterAvatarFor(DisplayName displayName, @Px int viewSize, @Px int letterSize) {
        Bitmap drawingBitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(drawingBitmap);

        Paint backgroundPaint = createBackgroundPaint();
        float radius = canvas.getWidth() / 2;
        canvas.drawCircle(radius, radius, radius, backgroundPaint);

        Paint textPaint = createTextPaint(letterSize);
        int xPos = canvas.getWidth() / 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(firstLetterOf(displayName), xPos, yPos, textPaint);
        return drawingBitmap;
    }

    private Paint createBackgroundPaint() {
        Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(colorResources.getColor(R.color.widget_upcoming_avatar_background_color));
        return backgroundPaint;
    }

    private static Paint createTextPaint(@Px int letterSize) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(letterSize);
        paint.setTypeface(ROBOTO_LIGHT);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    private static String firstLetterOf(DisplayName displayName) {
        String rawDisplayName = displayName.toString();
        if (rawDisplayName.length() == 0) {
            return SMILEY_FACE;
        }
        return rawDisplayName.substring(0, 1).toUpperCase();
    }
}
