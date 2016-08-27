package com.alexstyl.specialdates.views;


import android.content.res.Resources;
import android.view.View;

import com.alexstyl.specialdates.R;

public class FabPaddingSetter {

    public void setBottomPaddingTo(View view) {
        // set padding so that we don't cover the FAB doesn't cover the last row
        Resources resources = view.getResources();
        int fabHeight = resources.getDimensionPixelSize(R.dimen.fab_size_normal)
                + resources.getDimensionPixelSize(R.dimen.fab_margin);

        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                view.getPaddingRight(), view.getPaddingBottom() + fabHeight);

    }
}
