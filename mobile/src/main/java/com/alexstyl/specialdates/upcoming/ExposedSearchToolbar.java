package com.alexstyl.specialdates.upcoming;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.alexstyl.specialdates.R;

public class ExposedSearchToolbar extends Toolbar {

    public ExposedSearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(context.getResources().getColor(android.R.color.white));
        setNavigationIcon(R.drawable.ic_action_search);
    }

}
