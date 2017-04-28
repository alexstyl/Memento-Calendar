package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;

public class LogoView extends RelativeLayout {

    public LogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.merge_logo_view, this, true);

        if (isInEditMode()) {
            return;
        }
        TextView appversionView = (TextView) findViewById(R.id.logo_view_app_version);
        String appVersion = MementoApplication.getVersionName(getContext());
        appversionView.setText(appVersion);
    }

}
