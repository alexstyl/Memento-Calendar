package com.alexstyl.specialdates.addevent.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.alexstyl.specialdates.R;

public class AccountSelectionView extends LinearLayout {

    public AccountSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Changing orientation is not permitted");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.row_account, this);
    }
}
