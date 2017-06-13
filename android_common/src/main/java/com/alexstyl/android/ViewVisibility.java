package com.alexstyl.android;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        View.VISIBLE,
        View.INVISIBLE,
        View.GONE
})
public @interface ViewVisibility {
}
