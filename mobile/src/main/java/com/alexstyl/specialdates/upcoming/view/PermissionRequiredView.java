package com.alexstyl.specialdates.upcoming.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

public class PermissionRequiredView extends FrameLayout {

    private Button grantButton;

    public PermissionRequiredView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.merge_contact_permission_required, this);

        grantButton = Views.findById(this, R.id.contact_permission_grant_button);
    }

    public void setOnGrantButtonPressedListener(OnClickListener listener) {
        grantButton.setOnClickListener(listener);
    }

}
