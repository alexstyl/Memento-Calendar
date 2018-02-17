package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;

public class LogoView extends RelativeLayout {

    public LogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.merge_logo_view, this, true);
        if (isInEditMode()) {
            return;
        }

        TextView appversionView = findViewById(R.id.logo_view_app_version);
        String appVersion = getVersionName(getContext());
        appversionView.setText(appVersion);
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0
            );
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

}
