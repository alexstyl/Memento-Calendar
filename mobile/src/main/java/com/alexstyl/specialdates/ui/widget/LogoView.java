package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.MementoApp;
import com.alexstyl.specialdates.R;

public class LogoView extends RelativeLayout {

    public LogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.merge_logo_view, this, true);

        if (isInEditMode()) {
            return;
        }
        TextView appversionView = (TextView) findViewById(R.id.app_version);
        String appVersion = MementoApp.getVersionName(getContext());
        appversionView.setText(appVersion);
    }

}
