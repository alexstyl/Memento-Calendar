package com.alexstyl.specialdates.upcoming.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.alexstyl.specialdates.R;

public class ExposedSearchToolbar extends Toolbar {

    public ExposedSearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.drawable.card_noshadow);
        setNavigationIcon(R.drawable.ic_search_black_24dp);
    }

}
