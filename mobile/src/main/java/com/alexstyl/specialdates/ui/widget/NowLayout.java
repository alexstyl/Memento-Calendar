
package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.alexstyl.specialdates.R;

public class NowLayout extends LinearLayout implements OnGlobalLayoutListener {

    private boolean animationEnabled = true;

    public NowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayoutObserver();

    }

    public NowLayout(Context context) {
        super(context);
        initLayoutObserver();
    }

    private void initLayoutObserver() {
        setOrientation(LinearLayout.VERTICAL);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * Enables or disables the animation when adding new children to the layout
     * 
     * @param enabled Set true to enable the animation, false to disable it
     */
    public void setAnimationEnabled(boolean enabled) {
        this.animationEnabled = enabled;

    }



    public interface NowLayoutListener {
        public void onAnimationFinished();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
        if (animationEnabled) {
            final int heightPx = getContext().getResources().getDisplayMetrics().heightPixels;

            boolean inversed = false;
            final int childCount = getChildCount();

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);

                int[] location = new int[2];

                child.getLocationOnScreen(location);

                if (location[1] > heightPx) {
                    break;
                }

                if (!inversed) {
                    child.startAnimation(AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_up_left));
                } else {
                    child.startAnimation(AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_up_right));
                }

                inversed = !inversed;
            }

        }

    }

}
