package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.util.GreekNameUtils;
import com.alexstyl.specialdates.util.Utils;

/**
 * Created by Alex on 7/11/2014.
 */
public class LetterPainter {


    private static final String UKNOWN_CHARACTER = "?";

    /**
     * The different color variants to draw
     */
    private static final int[] mVariants = {
            R.color.avatar_variant_1,
            R.color.avatar_variant_2,
            R.color.avatar_variant_3,
            R.color.avatar_variant_4,
            R.color.avatar_variant_5,
    };

    private static final int VARIANT_COUNT;

    static {
        VARIANT_COUNT = mVariants.length;
    }


    private Resources mRes;
    private String mCharacter = UKNOWN_CHARACTER;

    private Paint mLetterPaint;
    private Paint mBackgroundPaint;


    private Rect rectText = new Rect();
    private int mBackgroundColor = 0;


    final private View mView;
    final private boolean isCircularView;


    public LetterPainter(View view) {
        this(view, false);
    }

    public LetterPainter(View view, boolean isCircularView) {
        this.mView = view;
        this.mRes = view.getResources();
        this.isCircularView = isCircularView;
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLetterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLetterPaint.setColor(mRes.getColor(android.R.color.white));
        mLetterPaint.setTextSize(mRes.getDimensionPixelSize(R.dimen.initials_textSize));
        mLetterPaint.setStyle(Paint.Style.FILL);
        mLetterPaint.setTextAlign(Paint.Align.CENTER);


        if (Utils.hasJellyBean()) {
            mLetterPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        }

    }


    public static int getVariant(Resources res, int i) {
        if (i < 0) {
            i = i * (-1);
        }
        if (i >= VARIANT_COUNT) {
            i = (i % VARIANT_COUNT);
            if (i >= VARIANT_COUNT)
                i = i / VARIANT_COUNT;
        }
        return res.getColor(mVariants[i]);
    }


    public boolean setLetter(String a, boolean firstOnly) {
        return setLetterInternal(a, firstOnly);
    }


    private boolean setLetterInternal(String word, boolean firstChar) {
        word = (word == null ? "" : word.trim().toUpperCase());
        if (!mCharacter.equals(word)) {
            if (TextUtils.isEmpty(word)) {
                mCharacter = UKNOWN_CHARACTER;
            } else {
                if (firstChar) {
                    mCharacter = GreekNameUtils.removeAccents(word.substring(0, 1));
                } else {
                    mCharacter = GreekNameUtils.removeAccents(word);
                }
            }

            // cause the view to redraw itself if the later changed
            if (mToDraw != null) {
                mToDraw.recycle();
                mToDraw = null;
            }
//            mView.setImageDrawable(null);
            return true;
        }
        return false;
    }


    public String getLetter() {
        return mCharacter;
    }


    public void setBackgroundVariant(int i) {
        this.mBackgroundColor = LetterPainter.getVariant(mRes, i);
        this.mBackgroundPaint.setColor(mBackgroundColor);
    }


    private Bitmap mToDraw;


    /**
     * Fills the canvas with the color variation selected and the selected letter
     *
     * @param canvas The canvas to draw on top of
     */
    public void drawTextToCanvas(Canvas canvas) {

        int w = mView.getWidth();
        int h = mView.getHeight();

        if (w == 0 || h == 0) {
            // nothing to draw
            return;
        }


        if (mToDraw == null) {
            int canvasSize = Math.max(w, h);
            mToDraw = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888);

            Canvas bmpCanvas = new Canvas(mToDraw);
            // we are drawing the character at the background

            int xPos = (w / 2);
            int yPos = (int) ((h / 2) - ((mLetterPaint.descent() + mLetterPaint.ascent()) / 2));

            if (isCircularView) {
                bmpCanvas.drawCircle(xPos, yPos, xPos, mBackgroundPaint);
            } else {
                bmpCanvas.drawColor(mBackgroundColor);
            }

            if (!TextUtils.isEmpty(mCharacter)) {
                mLetterPaint.getTextBounds(mCharacter, 0, 1, rectText);
                bmpCanvas.drawText(mCharacter, xPos, yPos, mLetterPaint);
            }
        }
        canvas.drawBitmap(mToDraw, 0, 0, mBackgroundPaint);
    }


    public Drawable getDrawable() {
        if (mToDraw == null) {
            return null;
        }
        return new BitmapDrawable(mRes, mToDraw);
    }

    public static Bitmap buildLetterBitmap(Context context, String mCharacter, int w, int h, int color, boolean circular) {
        if (w == 0 || h == 0) {
            return null;
        }

        int canvasSize = Math.max(w, h);
        Bitmap mToDraw = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888);


        Paint mLetterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources mRes = context.getResources();
        mLetterPaint.setColor(mRes.getColor(android.R.color.white));
        mLetterPaint.setTextSize(mRes.getDimensionPixelSize(R.dimen.initials_textSize));
        mLetterPaint.setStyle(Paint.Style.FILL);
        mLetterPaint.setTextAlign(Paint.Align.CENTER);


        if (Utils.hasJellyBean()) {
            mLetterPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        }
        int bgColor = mRes.getColor(color);
        Canvas bmpCanvas = new Canvas(mToDraw);
        // we are drawing the character at the background

        int xPos = (w / 2);
        int yPos = (int) ((h / 2) - ((mLetterPaint.descent() + mLetterPaint.ascent()) / 2));

        if (circular) {
            Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBackgroundPaint.setColor(bgColor);
            bmpCanvas.drawCircle(xPos, yPos, xPos, mBackgroundPaint);
        } else {
            bmpCanvas.drawColor(bgColor);
        }

        if (!TextUtils.isEmpty(mCharacter)) {
            mLetterPaint.getTextBounds(mCharacter, 0, 1, new Rect());
            bmpCanvas.drawText(mCharacter, xPos, yPos, mLetterPaint);
        }


        return mToDraw;
    }
}

