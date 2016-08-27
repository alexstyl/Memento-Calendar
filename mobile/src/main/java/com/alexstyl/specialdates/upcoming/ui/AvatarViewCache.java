package com.alexstyl.specialdates.upcoming.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

import java.util.ArrayList;
import java.util.List;

class AvatarViewCache {

    private final List<ColorImageView> freeAvatarViews = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    static AvatarViewCache newInstance(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new AvatarViewCache(layoutInflater);
    }

    AvatarViewCache(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    ColorImageView getAvatar(ViewGroup viewGroup) {
        if (freeAvatarViews.isEmpty()) {
            return inflate(viewGroup);
        } else {
            return popList();
        }
    }

    private ColorImageView inflate(ViewGroup viewGroup) {
        return (ColorImageView) layoutInflater.inflate(R.layout.row_avatar, viewGroup, false);
    }

    private ColorImageView popList() {
        return freeAvatarViews.remove(freeAvatarViews.size() - 1);
    }

    void removeAllFrom(LinearLayout avatars) {
        int viewCount = avatars.getChildCount();
        for (int i = viewCount - 1; i >= 0; i--) {
            ColorImageView view = (ColorImageView) avatars.getChildAt(i);
            freeAvatarViews.add(view);
        }
        avatars.removeAllViews();
    }
}
