package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Size;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.util.GreekNameUtils;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;

public class ColorImageView extends FrameLayout {

    private static final String UKNOWN_CHARACTER = "?";

    @Inject
    AndroidLetterPainter letterPainter;

    private boolean drawCircle = true;

    private TextView letter;

    private ImageView image;
    private Paint paint;

    private int backgroundVariant;

    public ColorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        AppComponent applicationModule = ((MementoApplication) context.getApplicationContext()).getApplicationModule();
        applicationModule.inject(this);

        View view = inflate(context, R.layout.merge_color_imageview, this);
        this.letter = Views.findById(view, android.R.id.text1);
        this.image = Views.findById(view, android.R.id.icon);

        Resources resources = getResources();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(resources.getColor(android.R.color.white));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);

        if (Version.INSTANCE.hasJellyBean()) {
            paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        }

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColorImageView,
                0, 0
        );

        try {
            drawCircle = a.getBoolean(R.styleable.ColorImageView_isCircle, false);
            float textSize = a.getDimension(R.styleable.ColorImageView_letterSize, resources.getDimensionPixelSize(R.dimen.initials_textSize));
            letter.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } finally {
            a.recycle();
        }

        paint.setAntiAlias(true);
        setWillNotDraw(false);
        invalidate();
    }

    public void setLetter(String word, boolean firstChar) {
        word = (word == null ? "" : word.trim().toUpperCase());
        String mCharacter = letter.getText().toString();
        if (!mCharacter.equals(word)) {
            if (TextUtils.isEmpty(word)) {
                letter.setText(UKNOWN_CHARACTER);
            } else {
                if (firstChar) {
                    letter.setText(GreekNameUtils.removeAccents(word.substring(0, 1)));
                } else {
                    letter.setText(GreekNameUtils.removeAccents(word));
                }
            }

        }
    }

    public ImageView getImageView() {
        return image;
    }

    /**
     * Returns the currently selected background variant of the view
     */
    public void setCircleColorVariant(@Size(min = 1, max = 5) int i) {
        int variant = letterPainter.getVariant(i);
        if (backgroundVariant != variant) {
            backgroundVariant = variant;
            paint.setColor(backgroundVariant);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (width > 0 && height > 0) {
            if (drawCircle) {
                float minSide = (float) Math.min(width, height);
                canvas.drawCircle(width / 2, height / 2, minSide / 2, paint);
            } else {
                canvas.drawPaint(paint);
            }
        }
        super.onDraw(canvas);
    }

    public void setLetter(String letter) {
        setLetter(letter, true);
    }

    public void setText(String text) {
        setLetter(text);
    }
}
